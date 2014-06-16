package org.daniels.examples.dao.impl;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.dao.HibernateConnection;
import org.daniels.examples.dao.IGenericDAO;
import org.daniels.examples.exceptions.InfrastructureException;
import org.daniels.examples.model.Orders;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.google.inject.Singleton;

/**
 * DAO for Table "orders". Running as a Singleton.
 * 
 * @author Siegfried Bolz
 */
@Singleton 
public class OrdersDAO implements IGenericDAO<Orders> {

    
    private int counter=0; // remove if not needed.
    
    private Log logger = LogFactory.getLog(OrdersDAO.class);
    private HibernateConnectionImpl connection;


    public void makePersistent(Orders orders) throws InfrastructureException {
        
        /**
         * You can see here if "ordersController2" has successfully used
         * the OrdersDAO-Singleton or created a new OrdersDAO-instance.
         * Remove if not needed.
         */
        logger.info("****** Access to OrderDAO: " + ++counter);
        
        try {
            Session session = (Session) this.getConnection().getSession();
            session.save(orders);
            
        } catch (HibernateException ex) {
            this.getConnection().rollbackTransaction();
            logger.error("Exception in OrdersDAO.makePersistent().", ex);
            throw new InfrastructureException(ex);
        }

    }

    public void makePersistentUpdate(Orders orders) throws InfrastructureException {
        try {
            Session session = (Session) this.getConnection().getSession();
            session.update(orders);
            
        } catch (HibernateException ex) {
            this.getConnection().rollbackTransaction();
            logger.error("Exception in OrdersDAO.makePersistentUpdate().", ex);
            throw new InfrastructureException(ex);
        }

    }

    public void makeTransient(Orders orders) throws InfrastructureException {
        try {
            Session session = (Session) this.getConnection().getSession();
            session.delete(orders);
            
        } catch (HibernateException ex) {
            this.getConnection().rollbackTransaction();
            logger.error("Exception in OrdersDAO.makeTransient(). Rollback activated.", ex);
            throw new InfrastructureException(ex);
        }
    }

    /**
     * Search for all Orders-Entities in Database.
     * 
     * @return Collection
     * @throws org.daniels.examples.exceptions.InfrastructureException
     */
    public Collection findAll() throws InfrastructureException {
        Collection collections;
        try {

            Session session = this.getConnection().getSession();
            collections = session.createCriteria(Orders.class).list();
            
            this.getConnection().commitTransaction();
        } catch (HibernateException ex) {
            logger.error("Exception in OrdersDAO.findAll().", ex);
            throw new InfrastructureException(ex);
        } finally {
            this.getConnection().closeSession();
        }

        return collections;
    }

    /**
     * Set HibernateConnection (Wrapper for HibernateUtil)
     * 
     * @param connection
     * @throws org.daniels.examples.exceptions.InfrastructureException
     */
    public void setConnection(HibernateConnection connection) throws InfrastructureException {
        this.connection = (HibernateConnectionImpl) connection;
    }

    /**
     * Use this method to gain access to the Hibernate-Wrapper.
     * 
     * @return
     * @throws org.daniels.examples.exceptions.InfrastructureException
     */
    public HibernateConnection<Session> getConnection() throws InfrastructureException {
        return this.connection;
    }
    
} // .EOF
