package org.daniels.examples.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.dao.RoleDao;
import org.daniels.examples.dao.hibernate.RoleDaoHibernate;
import org.daniels.examples.dao.impl.HibernateConnectionImpl;
import org.daniels.examples.hibernate.util.HibernateUtil;
import org.daniels.examples.model.Role;
import org.daniels.samples.modules.HibernateModule;
import org.hibernate.SessionFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;



public class DaoHibernateMain {

    private static Log logger = LogFactory.getLog(DaoHibernateMain.class);

    public static void main(String[] args) {       

        HibernateModule module = new HibernateModule();

        Injector injector = Guice.createInjector(new Module[]{module}); 
        
        HibernateConnectionImpl connection = injector.getInstance(HibernateConnectionImpl.class); 
        //can be: HibernateUtil connection = injector.getInstance(HibernateUtil.class); 
        
        SessionFactory sessionFactory = connection.getSessionFactory();
        
        final Role role = new Role("Daniels", "Daniels Role Tester");
        
        final RoleDao roleDao = injector.getInstance(RoleDaoHibernate.class);

        logger.info(">>> statistics: " + sessionFactory.getStatistics());
        
        
        connection.beginTransaction();
        roleDao.save(role);
        logger.info(">>> statistics: " + sessionFactory.getStatistics());
        connection.commitTransaction();
        connection.closeSession();
        
        
        connection.beginTransaction();
        roleDao.save(role);
        logger.info(">>> statistics: " + connection.getSessionFactory().getStatistics());
        connection.commitTransaction();
        connection.closeSession();
        
        
        connection.closeSessionFactory();
        
        
        
        
    }
    
}