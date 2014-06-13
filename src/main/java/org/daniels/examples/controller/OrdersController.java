package org.daniels.examples.controller;

import org.daniels.examples.dao.impl.OrdersDAO;

import com.google.inject.Inject; 

/**
 * This class is used to retrieve the one and only instance of
 * OrdersDAO (it is a Singleton). We are using Guice to inject a 
 * OrdersDAO-instance into our controller. 
 * 
 * @author Siegfried Bolz
 */
public class OrdersController {
    
    private OrdersDAO dao;

     @Inject
    public void setOrdersDAO(OrdersDAO dao) {
        this.dao = dao;
    } 
    
    public OrdersDAO getOrdersDAO() {
        return dao;
    } 
    
} // .EOF
