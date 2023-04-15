package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.DeleteUserData;
import pt.unl.fct.di.apdc.firstwebapp.util.UserData;

import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

@Path("/delete")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class DeleteUserResource {

	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(DeleteUserResource.class.getName());

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public DeleteUserResource() {
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUserResource(DeleteUserData data) {
		LOG.fine("Attempt to delete user: " + data.usernameRemoved);
		Transaction txn = datastore.newTransaction();
		try {
			Key userResponsibleKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
			Key userToRemoveTokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.usernameRemoved);

			Entity userResponsible = txn.get(userResponsibleKey);
			String roleFromResponsible = userResponsible.getString("user_role");
			if (roleFromResponsible.equals("USER")) {
				if (!data.username.equals(data.usernameRemoved)) {
					txn.rollback();
					return Response.status(Status.FORBIDDEN).entity("You can only delete your own account.").build();
				} else {
					txn.delete(userResponsibleKey);
					txn.delete(userToRemoveTokenKey);
					LOG.info("User deleted: " + data.usernameRemoved);
					txn.commit();
					return Response.ok("{}").build();
				}
			} else if (roleFromResponsible.equals("GBO")) {
				Key userToRemoveKey = datastore.newKeyFactory().setKind("User").newKey(data.usernameRemoved);
				Entity userToRemove = txn.get(userToRemoveKey);
				if (userToRemove == null) {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();
				}
				String roleFromRemoved = userToRemove.getString("user_role");
				if (roleFromRemoved.equals("USER")) {
					txn.delete(userToRemoveKey);
					txn.delete(userToRemoveTokenKey);
					LOG.info("User deleted: " + data.usernameRemoved);
					txn.commit();
					return Response.ok("{}").build();
				} else {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();
				}
			} else if (roleFromResponsible.equals("GA")) {
				Key userToRemoveKey = datastore.newKeyFactory().setKind("User").newKey(data.usernameRemoved);
				Entity userToRemove = txn.get(userToRemoveKey);
				if (userToRemove == null) {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();
				}
				String roleFromRemoved = userToRemove.getString("user_role");
				if (roleFromRemoved.equals("USER") || roleFromRemoved.equals("GBO")) {
					txn.delete(userToRemoveKey);
					txn.delete(userToRemoveTokenKey);
					LOG.info("User deleted: " + data.usernameRemoved);
					txn.commit();
					return Response.ok("{}").build();
				} else {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();

				}
			} else if (roleFromResponsible.equals("GS")) {
				Key userToRemoveKey = datastore.newKeyFactory().setKind("User").newKey(data.usernameRemoved);
				Entity userToRemove = txn.get(userToRemoveKey);
				if (userToRemove == null) {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();
				}
				String roleFromRemoved = userToRemove.getString("user_role");
				if (roleFromRemoved.equals("USER") || roleFromRemoved.equals("GBO") || roleFromRemoved.equals("GA")) {
					txn.delete(userToRemoveKey);
					txn.delete(userToRemoveTokenKey);
					LOG.info("User deleted: " + data.usernameRemoved);
					txn.commit();
					return Response.ok("{}").build();
				} else {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();

				}
			} else {
				Key userToRemoveKey = datastore.newKeyFactory().setKind("User").newKey(data.usernameRemoved);
				Entity userToRemove = txn.get(userToRemoveKey);
				if (userToRemove == null) {
					txn.rollback();
					return Response.status(Status.NOT_FOUND).entity("User not found.").build();
				}
				txn.delete(userToRemoveKey);
				txn.delete(userToRemoveTokenKey);
				LOG.info("User deleted: " + data.usernameRemoved);
				txn.commit();
				return Response.ok("{}").build();

			}
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}
}
