package org.daniels.examples.dao.impl;

import org.daniels.examples.dao.HibernateConnection;
import org.daniels.examples.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Wrapper-class for OrdersDAO (and all other DAO's). 
 */
public class HibernateConnectionImpl implements HibernateConnection<Session>{

    private HibernateUtil hibernateUtil;
    
    public HibernateConnectionImpl(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }
    
    public void connect() {
       hibernateUtil.configure();
    }
    
    public void beginTransaction(){
        hibernateUtil.beginTransaction();
    }

    public void closeSessionFactory() {
        hibernateUtil.closeSessionFactory();
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        hibernateUtil.setSessionFactory(sessionFactory);
    }
    
    public Session getSession() {
        return hibernateUtil.getSession();
    }
    
    public SessionFactory getSessionFactory() {
        return hibernateUtil.getSessionFactory();
    }

    public void rollbackTransaction() {
        hibernateUtil.rollbackTransaction();
    }

    public void commitTransaction() {
        hibernateUtil.commitTransaction();
    }

    public void closeSession() {
        hibernateUtil.closeSession();
    }
    
}
