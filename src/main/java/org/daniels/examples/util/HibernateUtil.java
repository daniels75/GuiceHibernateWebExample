package org.daniels.examples.util;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.exceptions.InfrastructureException;

import javax.naming.*;

/**
 * Large Hibernate helper class, handles SessionFactory, Session and Transaction.
 * 
 * Uses a  initializer for the initial SessionFactory creation
 * and holds Session and Transactions in thread local variables. All
 * exceptions are wrapped in an unchecked InfrastructureException.
 *
 * @version 1.2
 * @author Siegfried Bolz (www.jdevelop.eu)
 */
public class HibernateUtil {
  
        /** Logger. */
	private  Log log = LogFactory.getLog(HibernateUtil.class);

	/** configuration. */
	private  Configuration configuration;

	/** Session Factory */
	private  SessionFactory sessionFactory;



	/** If running unit tests set to true. */
	//private  boolean offlineMode = true;

	/** threadlocal. */
	private  final ThreadLocal threadSession = new ThreadLocal();

	/** threadlocal. */
	private  final ThreadLocal threadTransaction = new ThreadLocal();


	/**
	 * Create the initial SessionFactory from hibernate.xml.cfg
	 */
	public  void configure() {
		log.debug("HibernateUtil.Configure() - Trying to initialize Hibernate.");
		try {
			// Use hibernate.cfg.xml (true) or JNDI (false)
			sessionFactory = getSessionFactory();

		} catch (Throwable x) {
			// We have to catch Throwable, otherwise we will miss
			// NoClassDefFoundError and other subclasses of Error
			log.error("HibernateUtil.Configure() - Building SessionFactory failed.", x);
			throw new ExceptionInInitializerError(x);
		}
	}
	
	

    
        /**
	 * Returns the SessionFactory used for this  class. If offlineMode has
	 * been set then we use hibernate.cfg.xml to create sessionfactory, if not
	 * then we use sessionfactory bound to JNDI.
	 * 
	 * @return SessionFactory
	 */
	public  SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
				log.debug("HibernateUtil.getSessionFactory() - Using hibernate.cfg.xml to create a SessionFactory");
				try {
					configuration = new Configuration();
					sessionFactory = configuration.configure().buildSessionFactory();
				} catch (HibernateException x) {
					throw new InfrastructureException("HibernateUtil.getSessionFactory() - Error creating SessionFactory with hibernate.cfg.xml .",x);
				}
				
		}

		if (sessionFactory == null) {
			throw new IllegalStateException("HibernateUtil.getSessionFactory() - SessionFactory not available.");
		}
		return sessionFactory;
	}


	/**
	 * Sets the given SessionFactory.
	 * 
	 * @param sessionFactory
	 */
	public  void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

  
        /**
	 * Destroy this SessionFactory and release all resources (caches, connection
	 * pools, etc).
	 * 
	 * @author Siegfried Bolz
	 * @param cfg
	 */
	public  void closeSessionFactory() throws InfrastructureException {
		synchronized (sessionFactory) {
			try {
				log.debug("HibernateUtil.closeSessionFactory() - Destroy the current SessionFactory.");
				closeSession();
				sessionFactory.close();
				// Clear  variables
				configuration = null;
				sessionFactory = null;
			} catch (Exception x) {
				throw new InfrastructureException("HibernateUtil.closeSessionFactory() - Error destroying the current SessionFactory",x);
			}
		}
	}

  
        /**
	 * Retrieves the current Session local to the thread. <p/>
	 * 
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
	public  Session getSession() throws InfrastructureException {
		Session session = (Session) threadSession.get();
		try {
			if (session == null) {
				log.debug("HibernateUtil.getSession() - Opening new Session for this thread.");

				//session = getSessionFactory().openSession();
				session = getSessionFactory().getCurrentSession();
				
				
				threadSession.set(session);
			}
		} catch (HibernateException x) {
			throw new InfrastructureException("HibernateUtil.getSession() - Error retrieving/creating a session.",x);
		}
		return session;
	}

  
        /**
	 * Closes the Session local to the thread.
	 */
	public  void closeSession() throws InfrastructureException {
		try {
			Session s = (Session) threadSession.get();
			threadSession.set(null);
			if (s != null && s.isOpen()) {
				log.debug("HibernateUtil.closeSession() - Closing Session of this thread.");
				s.close();
			}
		} catch (HibernateException x) {
			throw new InfrastructureException("HibernateUtil.closeSession() - Error closing the session.",x);
		}
	}


        /**
	 * Start a new database transaction.
	 */
	public  void beginTransaction() throws InfrastructureException {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if (tx == null) {
				log.debug("HibernateUtil.beginTransaction() - Starting new database transaction in this thread.");
				final Session session = getSession();
				tx = session.beginTransaction();
				threadTransaction.set(tx);
			}
		} catch (HibernateException x) {
			throw new InfrastructureException("HibernateUtil.beginTransaction() - Error starting a new database transaction.",x);
		}
	}

	
        /**
	 * Commit the database transaction.
	 */
	public  void commitTransaction() throws InfrastructureException {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				log.debug("HibernateUtil.commitTransaction() - Committing database transaction of this thread.");
				tx.commit();
			}
			threadTransaction.set(null);
		} catch (HibernateException x) {
			rollbackTransaction();
			throw new InfrastructureException("HibernateUtil.commitTransaction() - Error commiting the database transaction.",x);
		}
	}

  
        /**
	 * Rollback the database transaction.
	 */
	public  void rollbackTransaction() throws InfrastructureException {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			threadTransaction.set(null);
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				log.debug("HibernateUtil.rollbackTransaction() - Tyring to rollback database transaction of this thread.");
				tx.rollback();
			}
		} catch (HibernateException x) {
			throw new InfrastructureException("HibernateUtil.rollbackTransaction() - Error rolling back the database transaction.",x);
		} finally {
			closeSession();
		}
	}

  

  



  

	
} 