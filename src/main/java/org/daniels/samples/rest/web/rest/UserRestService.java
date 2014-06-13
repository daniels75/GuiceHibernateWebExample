package org.daniels.samples.rest.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UserRestService {

	// @Inject
	// public UserRestService(UserService userService) {
	// this.userService = userService;
	// }
	public UserRestService() {
	}

	@GET
	@Path("testme")
	@Produces(MediaType.APPLICATION_JSON)
	public int getNumberOfUsers() {
		return 10;
	}

}
