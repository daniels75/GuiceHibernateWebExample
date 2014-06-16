package org.daniels.samples.rest.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daniels.examples.dao.HibernateConnection;
import org.daniels.examples.dao.hibernate.RoleDaoHibernate;
import org.daniels.examples.dao.impl.HibernateConnectionImpl;
import org.daniels.examples.main.DaoHibernateMain;
import org.daniels.examples.model.Role;

import com.google.inject.Inject;

@Path("/users")
public class UserRestService {

    private static Log logger = LogFactory.getLog(UserRestService.class);
	// @Inject
	// public UserRestService(UserService userService) {
	// this.userService = userService;
	// }
    
    private final HibernateConnectionImpl hibernateConnection;
    
    @Inject
	public UserRestService(final HibernateConnectionImpl hibernateConnection) {
        this.hibernateConnection = hibernateConnection;
	}

	@GET
	@Path("testme")
	@Produces(MediaType.APPLICATION_JSON)
	public int getNumberOfUsers() {
	    logger.info("connection initialized: " + hibernateConnection != null);
	    
        createRole();	    
	    
		return 10;
	}

    private void createRole() {
        final Role role = new Role("Daniels", "Daniels Role Tester");
        
        final RoleDaoHibernate roleDao = new RoleDaoHibernate();
        //roleDao.setSessionFactory(connection.getSessionFactory());
        //roleDao.setHibernateConnection(connection);
        roleDao.setSessionFactory(hibernateConnection.getSessionFactory());
        logger.info(">>> statistics: " + hibernateConnection.getSessionFactory().getStatistics());
        
        
        hibernateConnection.beginTransaction();
        roleDao.save(role);
        logger.info(">>> statistics: " + hibernateConnection.getSessionFactory().getStatistics());
        hibernateConnection.commitTransaction();
    }

}
