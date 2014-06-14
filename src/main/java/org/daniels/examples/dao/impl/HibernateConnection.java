package org.daniels.examples.dao.impl;

import org.daniels.examples.dao.IConnection;
import org.daniels.examples.util.HibernateUtil;
import org.hibernate.Session;

/**
 * Wrapper-class for OrdersDAO (and all other DAO's). 
 * If you want to use an other HibernateUtil, just change the
 * ConnectionProvider.class -Binding in GuiceModule or create
 * other Provider- and Connection classes.
 * 
 * @author Siegfried Bolz
 */
public class HibernateConnection implements IConnection<Session>{

    private HibernateUtil hibernateUtil;
    
    public HibernateConnection(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }
    
    public void connect() {
       hibernateUtil.configure();
       hibernateUtil.beginTransaction();
    }

    public void disConnect() {
        hibernateUtil.closeSessionFactory();
    }

    public Session getSession() {
        return hibernateUtil.getSession();
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
    
} // .EOF
