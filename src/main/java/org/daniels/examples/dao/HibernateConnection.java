package org.daniels.examples.dao;

/**
 * Interface for creating Database-Connection-Wrappers.
 * 
 */
public interface HibernateConnection<T> {

    public void connect();

    public void closeSessionFactory();
    
    public T getSession();
    
    public void rollbackTransaction();
    
    public void commitTransaction();
    
    public void closeSession();
    
}
