package org.daniels.examples.dao;

import org.daniels.examples.exceptions.InfrastructureException;

/**
 * Interface of the Generic DAO. Use this to create DAO-Implementations
 * for the models.
 * 
 * @author Siegfried Bolz
 */
public interface IGenericDAO<T> {
    
    public void setConnection(IConnection connection)
        throws InfrastructureException;
    
    public IConnection getConnection()
        throws InfrastructureException;    
    
    public void makePersistent(T var)
        throws InfrastructureException;

    public void makePersistentUpdate(T var)
        throws InfrastructureException;

    public void makeTransient(T var)
        throws InfrastructureException;    
       
} // .EOF
