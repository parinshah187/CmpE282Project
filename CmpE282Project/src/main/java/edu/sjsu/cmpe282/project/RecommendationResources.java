package edu.sjsu.cmpe282.project;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.sjsu.cmpe282.domain.User;
import edu.sjsu.cmpe282.dto.AWSDao;
import edu.sjsu.cmpe282.dto.AWSRecoDao;
import edu.sjsu.cmpe282.dto.UserDao;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationResources {

	private UserDao userdao = new UserDao();
	
	@GET
	@Path("/recommendations")
	@Produces("text/html")
	public Response FetchRecommendation(){
		AWSRecoDao awsRecoDao = new AWSRecoDao();
		return Response.status(200).entity(awsRecoDao.QueryRecommendations("201405062318")).build();
	}
	
	@GET
	@Path("/products")
	@Produces("text/html")
	public Response FetchHome(){
		AWSDao awsdao = new AWSDao();
		return Response.status(200).entity(awsdao.QueryHTMLProducts()).build();
		
	}

	@POST
	@Path("/signin")
	public boolean signIn(User user)
	{
		return userdao.checkUser(user);
	}
	
}
