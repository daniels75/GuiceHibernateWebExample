package org.daniels.examples.main;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.controller.OrdersController;
import org.daniels.examples.dao.impl.HibernateConnection;
import org.daniels.examples.dao.impl.OrdersDAO;
import org.daniels.examples.model.Article;
import org.daniels.examples.model.Customer;
import org.daniels.examples.model.Orders;
import org.daniels.samples.modules.HibernateModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


/**
 * Hibernate with Guice - Part II
 * 
 * This is a simple Hibernate-Application which was extended with
 * Guice functionality. To see this Application without Guice, 
 * just visit Part I.
 * 
 * @author Siegfried Bolz (www.jdevelop.eu)
 */
public class Main {

    private static Log logger = LogFactory.getLog(Main.class);

    public static void main(String[] args) {       
        /**
         * There are our bindings defined.
         */
        HibernateModule module = new HibernateModule();
        /**
         * The creation of this injector is something that you would do once, probably upon 
         * application startup. The injector is initiated with our bindings.
         */
        Injector injector = Guice.createInjector(new Module[]{module}); 
        
        firstCreate(injector);
        //secondCreate(injector);
    }
    
    public static void firstCreate(Injector injector){
        /**
         * The injector injects our controller instance with its dependencies.
         * Our OrdersDAO-instance will be now created.
         */
        OrdersController ordersController = new OrdersController();
        injector.injectMembers(ordersController);
        /**
         * Here we use the Factory to create a HibernateConnection (with its 
         * HibernateUtil-reference) instance.
         */
        HibernateConnection connection = injector.getInstance(HibernateConnection.class); 
        connection.connect(); // initialize HibernateUtil
        /**
         * Retrieve the previously created instance.
         */
        OrdersDAO dao = ordersController.getOrdersDAO();
        
        
        /**
         * Link the HibernateConnection-instance to the OrderDAO-instance.
         * OrderDAO needs this reference to the HibernateUtil.
         */
        dao.setConnection(connection);
        
        
        /**
         * Create entities
         */
        Article article1 = new Article();
        article1.setName("iPhone");
        article1.setPrice(399.99);
        
        Customer customer1 = new Customer();
        customer1.setCity("Munich");
        customer1.setName("Siegfried Bolz");
        customer1.setPhone("555123123");
        customer1.setPostcode("80222");
        customer1.setStreet("Mainstreet 12");
        
        Orders order1 = new Orders();
        order1.setAmount(1);
        order1.setOrderDate(new Date());
        order1.setSum(article1.getPrice() * order1.getAmount());
        order1.setArticle(article1);
        order1.setCustomer(customer1);
        
        /**
         * Let OrderDAO handle the persistence.
         */
        dao.makePersistent(order1);
        
        
        /**
         * Commit transaction.
         */
        connection.commitTransaction();
        /**
         * Close Hibernate SessionFactory and all sessions.
         */
        connection.disConnect(); 
        
    }
    public static void secondCreate(Injector injector){
        /**
         * ********************* now look if guice handles singleton correctly *************************
         */
        
        
        /**
         * Create a new OrdersController but retrieve the same old OrdersDAO-Instance
         * as in line 44 (singleton).
         */
        OrdersController ordersController2 = new OrdersController();
        injector.injectMembers(ordersController2);
        OrdersDAO dao2 = ordersController2.getOrdersDAO();
        
        
        /**
         * New database-input.
         */
        Article article2 = new Article();
        article2.setName("MacBook Air");
        article2.setPrice(1799.99);
        
        Customer customer2 = new Customer();
        customer2.setCity("Kempten");
        customer2.setName("Max Mustermann");
        customer2.setPhone("01714465435");
        customer2.setPostcode("87435");
        customer2.setStreet("Sunset Blv. 6");
        
        Orders order2 = new Orders();
        order2.setAmount(2);
        order2.setOrderDate(new Date());
        order2.setSum(article2.getPrice() * order2.getAmount());
        order2.setArticle(article2);
        order2.setCustomer(customer2);
        
        
        /**
         * Retrieves the same old HibernateConnection-Instance as in line 52 (singleton!).
         */
        HibernateConnection connection2 = injector.getInstance(HibernateConnection.class); 
        
        connection2.connect(); // reconnect to database 
        dao2.setConnection(connection2);
        
        dao2.makePersistent(order2);
        
        connection2.commitTransaction(); // write in database
        
        
        /**
         * get number of orders in database
         */
        //logger.info("****** Number of orders in database: " + dao.findAll().size());
        
        connection2.disConnect(); // finish connection    	
    }

} // .EOF