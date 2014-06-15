package org.daniels.examples.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.dao.RoleDao;
import org.daniels.examples.dao.hibernate.RoleDaoHibernate;
import org.daniels.examples.dao.impl.HibernateConnection;
import org.daniels.examples.model.Role;
import org.daniels.samples.modules.HibernateModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;



public class DaoHibernateMain {

    private static Log logger = LogFactory.getLog(DaoHibernateMain.class);

    public static void main(String[] args) {       

        HibernateModule module = new HibernateModule();

        Injector injector = Guice.createInjector(new Module[]{module}); 
        
        HibernateConnection connection = injector.getInstance(HibernateConnection.class); 
        
        
        connection.connect();
        
        final Role role = new Role("Daniels", "Daniels Role Tester");
        
        final RoleDaoHibernate roleDao = new RoleDaoHibernate();
        //roleDao.setSessionFactory(connection.getSessionFactory());
        //roleDao.setHibernateConnection(connection);
        roleDao.setSession(connection.getSession());
        
        roleDao.save(role);
        
        connection.commitTransaction();
        connection.disConnect();
        
        
    }
    
}