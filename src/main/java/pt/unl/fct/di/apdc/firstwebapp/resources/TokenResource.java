package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.UserData;

@Path("/tokens")
public class TokenResource {

	private final Gson g = new Gson();
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@POST
	@Path("/info")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getTokenInfo(UserData data) {
		Key userToken = datastore.newKeyFactory().setKind("Token").newKey(data.username);
		Entity userT = datastore.get(userToken);
		long userTExpirationdata=userT.getLong("expirationData");
		long userTCreationdata=userT.getLong("creationData");
		String tokenID=userT.getString("tokenID");
		//long userTMagicNumber=userT.getLong("magicNumber");
		
		if (userTExpirationdata-System.currentTimeMillis()<0) {
			return Response.status(Status.BAD_REQUEST).entity("Token expired").build();
		}
		// Build the response string with the token information
		StringBuilder sb = new StringBuilder();
		sb.append("Username: ").append(data.username).append("\n");
		//sb.append("Magic Number: ").append(userTMagicNumber).append("\n");
		sb.append("Token ID: ").append(tokenID).append("\n");
		sb.append("Creation Date: ").append(userTCreationdata).append("\n");
		sb.append("Expiration Date: ").append(userTExpirationdata).append("\n");

		return Response.status(Status.OK).entity(g.toJson(sb)).build();
	}
}