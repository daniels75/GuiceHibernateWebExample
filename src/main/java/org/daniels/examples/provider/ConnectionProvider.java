package org.daniels.examples.provider;

import org.daniels.examples.dao.impl.HibernateConnection;
import org.daniels.examples.util.HibernateUtil;

import com.google.inject.Provider;

import static java.lang.annotation.ElementType.*;

import com.google.inject.Inject;

/**
 * A simple Provider class that conforms to Guice Provider that creates and 
 * return HibernateConnection objects. Guice automatically injects a brand new
 * HibernateUtil-instance (as singleton, defined in class GuiceModule). 
 * 
 * @author Siegfried Bolz
 */
public class ConnectionProvider implements Provider<HibernateConnection>{

    private final HibernateUtil hibernateUtil;
    
    @Inject
    ConnectionProvider(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }    
    
    
    public HibernateConnection get() {
        HibernateConnection connection = new HibernateConnection(hibernateUtil);
        return connection;
    }

} // .EOF
