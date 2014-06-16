package org.daniels.examples.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.google.inject.Provider;

public class SessionFactoryProvider implements Provider<SessionFactory> {
    private static final Log logger = LogFactory.getLog(SessionFactoryProvider.class);
    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    public SessionFactory get() {
        SessionFactory sessionFactory = null;
        try {
            final Configuration configuration = new Configuration();
            configuration.configure(HIBERNATE_CFG_XML);
            logger.debug("Hibernate Annotation Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            logger.debug("Hibernate Annotation serviceRegistry created");

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

}
