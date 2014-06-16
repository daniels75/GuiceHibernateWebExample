package org.daniels.examples.provider;

import org.daniels.examples.dao.impl.HibernateConnectionImpl;
import org.daniels.examples.hibernate.util.HibernateUtil;

import com.google.inject.Provider;

import static java.lang.annotation.ElementType.*;

import com.google.inject.Inject;

/**
 * A simple Provider class that conforms to Guice Provider that creates and 
 * return HibernateConnection objects. Guice automatically injects a brand new
 * HibernateUtil-instance (as singleton, defined in class GuiceModule). 
 * 
 */
public class HibernateConnectionProvider implements Provider<HibernateConnectionImpl>{

    private final HibernateUtil hibernateUtil;
    
    @Inject
    HibernateConnectionProvider(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }    
    
    
    public HibernateConnectionImpl get() {
        HibernateConnectionImpl connection = new HibernateConnectionImpl(hibernateUtil);
        return connection;
    }

}
