package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Transaction;
import com.google.api.client.http.HttpHeaders;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.PathElement;
import com.google.gson.Gson;
import javax.ws.rs.core.Response.Status;

import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;
import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {
	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Gson g = new Gson();
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public LoginResource() {
	}

	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogin(LoginData data) {
		
		
		LOG.fine("Login attempt by user: " + data.username);
		Transaction txn = datastore.newTransaction();
		
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
			Entity user = datastore.get(userKey);
			if (user == null) {
				LOG.warning("Failed login attempt");
				return Response.status(Status.FORBIDDEN).build();	
			} else {
				String hashedPWD = user.getString("user_pwd");
				if (hashedPWD.equals(DigestUtils.sha512Hex(data.password))) {

					AuthToken token = new AuthToken(data.username);
					Key userToken = datastore.newKeyFactory().setKind("Token").newKey(data.username);
					Entity uToken = Entity.newBuilder(userToken).set("username", token.username)
							.set("tokenID", token.tokenID).set("creationData", token.creationData)
							.set("expirationData", token.expirationData).build();
							//.set("magicNumber",token.magicNumber) teria de enviar encriptado como se fosse uma palavra passe
					txn.add(uToken);
					LOG.info("User '" + data.username + "' logged in sucessfully.");
					txn.commit();
					return Response.ok("{}").build();
					
				} else {
					LOG.warning("Failed login attempt");
					return Response.status(Status.FORBIDDEN).entity("Wrong password").build();
				}
			}
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}

		}

	
		
	}

		}