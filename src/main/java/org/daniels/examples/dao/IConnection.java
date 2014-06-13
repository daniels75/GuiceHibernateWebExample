package org.daniels.examples.dao;

/**
 * Interface for creating Database-Connection-Wrappers.
 * 
 * @author Siegfried Bolz
 */
public interface IConnection<T> {

    public void connect();

    public void disConnect();
    
    public T getSession();
    
    public void rollbackTransaction();
    
    public void commitTransaction();
    
    public void closeSession();
    
} // .EOF
