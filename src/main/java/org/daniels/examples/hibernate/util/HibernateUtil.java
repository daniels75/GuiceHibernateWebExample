package org.daniels.examples.hibernate.util;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.exceptions.InfrastructureException;

import com.google.inject.Inject;

import javax.naming.*;

/**
 * Hibernate helper class, handles SessionFactory, Session and Transaction.
 * 
 */
public class HibernateUtil {

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    private static final Log logger = LogFactory.getLog(HibernateUtil.class);

    private Configuration configuration;
    @Inject
    private SessionFactory sessionFactory;
    private final ThreadLocal threadSession = new ThreadLocal();
    private final ThreadLocal threadTransaction = new ThreadLocal();

    /**
     * Create the initial SessionFactory from hibernate.xml.cfg
     */
    public void configure() {
        logger.debug("Trying to initialize Hibernate.");
        try {
            sessionFactory = getSessionFactory();
        } catch (Throwable x) {
            logger.error("Building SessionFactory failed.", x);
            throw new ExceptionInInitializerError(x);
        }
    }

    public SessionFactory getSessionFactory() {
        // sessionFactory provided by Injection
//        if (sessionFactory == null) {
//            try {
//                final Configuration configuration = new Configuration();
//                configuration.configure(HIBERNATE_CFG_XML);
//                logger.debug("Hibernate Annotation Configuration loaded");
//
//                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
//                        configuration.getProperties()).build();
//                logger.debug("Hibernate Annotation serviceRegistry created");
//
//                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//
//                return sessionFactory;
//            } catch (Throwable ex) {
//                logger.error("Initial SessionFactory creation failed." + ex);
//                throw new ExceptionInInitializerError(ex);
//            }
//        }
//
//        if (sessionFactory == null) {
//            throw new IllegalStateException("SessionFactory not available.");
//        }

        return sessionFactory;

    }

    /**
     * Sets the given SessionFactory.
     * 
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Destroy this SessionFactory and release all resources (caches, connection pools, etc).
     * 
     */
    public void closeSessionFactory() throws InfrastructureException {
        synchronized (sessionFactory) {
            try {
                logger.debug("Destroy the current SessionFactory.");
                closeSession();
                sessionFactory.close();
                // Clear variables
                configuration = null;
                sessionFactory = null;
            } catch (Exception x) {
                throw new InfrastructureException(
                        "HibernateUtil.closeSessionFactory() - Error destroying the current SessionFactory", x);
            }
        }
    }

    /**
     * Retrieves the current Session local to the thread.
     * <p/>
     * 
     * If no Session is open, opens a new Session for the running thread.
     * 
     * @return Session
     */
    public Session getSession() throws HibernateException {
        Session session = (Session) threadSession.get();
        if (session == null || !session.isOpen()) {
            logger.debug("Opening new Session for this thread.");

            session = getSessionFactory().getCurrentSession();
            if (session == null) {
                session = getSessionFactory().openSession();
            }

            threadSession.set(session);
        }
        return session;
    }

    /**
     * Closes the Session local to the thread.
     */
    public void closeSession() throws HibernateException {
        Session s = (Session) threadSession.get();
        threadSession.set(null);
        if (s != null && s.isOpen()) {
            logger.debug("Closing Session of this thread.");
            s.close();
        }

    }

    /**
     * Start a new database transaction.
     */
    public void beginTransaction() throws HibernateException {
        Transaction transaction = (Transaction) threadTransaction.get();
        if (transaction == null) {
            logger.debug("Starting new database transaction in this thread.");
            final Session session = getSession();
            transaction = session.beginTransaction();
            threadTransaction.set(transaction);
        }

    }

    /**
     * Commit the database transaction.
     */
    public void commitTransaction() throws HibernateException {
        Transaction transaction = (Transaction) threadTransaction.get();
        if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
            logger.debug("Committing database transaction of this thread.");
            transaction.commit();
        }
        threadTransaction.set(null);

    }

    /**
     * Rollback the database transaction.
     */
    public void rollbackTransaction() throws HibernateException {
        Transaction transaction = (Transaction) threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
                logger.debug("HibernateUtil.rollbackTransaction() - Tyring to rollback database transaction of this thread.");
                transaction.rollback();
            }
        } finally {
            closeSession();
        }
    }

}