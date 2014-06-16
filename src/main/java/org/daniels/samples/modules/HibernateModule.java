package org.daniels.samples.modules;

import org.daniels.examples.dao.impl.HibernateConnectionImpl;
import org.daniels.examples.provider.HibernateConnectionProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;


/**
 * Configuration-class for guice. Here are the bindings defined.
 * 
 */
public class HibernateModule extends AbstractModule{
    
    @Override
    protected void configure() {
        
        /**
         * Without the Scopes.SINGLETON, each time when you call 
         * HibernateConnection connection = injector.getInstance(HibernateConnection.class); 
         * a new Instance of HibernateConnection (with the included HibernateUtil) will be created.
         */
        bind(HibernateConnectionImpl.class).toProvider(HibernateConnectionProvider.class).in(Scopes.SINGLETON);
       
    } 
    
}