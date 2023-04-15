
package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import pt.unl.fct.di.apdc.firstwebapp.util.UserData;

@Path("/listusers")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ListUsersResource {

	private static final Logger LOG = Logger.getLogger(ListUsersResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final Gson g = new Gson();

	public ListUsersResource() {
	}

	@POST
	@Path("/")
	public Response doListUsers(UserData data) {
		Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Entity userEntity = txn.get(userKey);
		String roleFromUser = userEntity.getString("user_role");

		// todos os users USER com perfil publico e estado ativo e mostrar nome email e
		// username
		if (roleFromUser.equals("USER")) {
			          
			PropertyFilter roleFilter = PropertyFilter.eq("user_role", "USER");
			PropertyFilter publicFilter =  PropertyFilter.eq("user_isPublic", "publico");
			PropertyFilter activeFilter=   PropertyFilter.eq("user_state", "ativo");
			Query<Entity> query = Query.newEntityQueryBuilder().setKind("User")
					.setFilter(roleFilter)
					.setFilter(publicFilter)
					.setFilter(activeFilter).build();
			QueryResults<Entity> resultList = datastore.run(query);
			List<String> usersList = new ArrayList<>();

			resultList.forEachRemaining(user -> {
				usersList.add(user.getString("user_name") + " " + user.getString("user_email"));
			});
			return Response.ok(g.toJson(usersList)).build();

		}
		// todos os users USERS e mostrar tudo
		if (roleFromUser.equals("GBO")) {
			PropertyFilter roleFilter = PropertyFilter.eq("user_role", "USER");
			Query<Entity> query = Query.newEntityQueryBuilder().setKind("User").setFilter(roleFilter).build();
			QueryResults<Entity> resultList = datastore.run(query);
			List<String> usersList = new ArrayList<>();

			resultList.forEachRemaining(user -> {
				usersList.add(user.getString("user_name") + " " + user.getString("user_email") + " "
						+ user.getString("user_codigoPostal") + " " + user.getString("user_morada") + " "
						+ user.getString("user_state") + " " + user.getString("user_nif") + " "
						+ user.getString("user_telefone") + " " + user.getString("user_telemovel") + " "
						+ user.getString("user_role") + " " + user.getString("user_isPublic"));
			});
			return Response.ok(g.toJson(usersList)).build();

		}
		// todos os users USER e GBO e mostrar tudo
		if (roleFromUser.equals("GS")) {
			PropertyFilter roleFilter = PropertyFilter.eq("user_role", "USER");

			Query<Entity> query = Query.newEntityQueryBuilder().setKind("User").setFilter(roleFilter).build();
			QueryResults<Entity> resultList = datastore.run(query);
			List<String> usersList = new ArrayList<>();
			resultList.forEachRemaining(user -> {
				usersList.add(user.getString("user_name") + " " + user.getString("user_email") + " "
						+ user.getString("user_codigoPostal") + " " + user.getString("user_morada") + " "
						+ user.getString("user_state") + " " + user.getString("user_nif") + " "
						+ user.getString("user_telefone") + " " + user.getString("user_telemovel") + " "
						+ user.getString("user_role") + " " + user.getString("user_isPublic"));
			});
			//return Response.ok(g.toJson(usersList)).build();
			
			
			PropertyFilter roleFilter2 = PropertyFilter.eq("user_role", "GBO");

			Query<Entity> query2 = Query.newEntityQueryBuilder().setKind("User").setFilter(roleFilter2).build();
			QueryResults<Entity> resultList2 = datastore.run(query2);
			List<String> usersList2 = new ArrayList<>();
			resultList2.forEachRemaining(user -> {
				usersList2.add(user.getString("user_name") + " " + user.getString("user_email") + " "
						+ user.getString("user_codigoPostal") + " " + user.getString("user_morada") + " "
						+ user.getString("user_state") + " " + user.getString("user_nif") + " "
						+ user.getString("user_telefone") + " " + user.getString("user_telemovel") + " "
						+ user.getString("user_role") + " " + user.getString("user_isPublic"));
			});
			List<String> usersListFinal = new ArrayList<>();
			usersListFinal.addAll(usersList);
			usersListFinal.addAll(usersList2);
			return Response.ok(g.toJson(usersListFinal)).build();

		}
		// tudo
		if (roleFromUser.equals("SU")) {

			Query<Entity> query = Query.newEntityQueryBuilder().setKind("User").build();
			QueryResults<Entity> resultList = datastore.run(query);
			List<String> usersList = new ArrayList<>();
			resultList.forEachRemaining(user -> {
				usersList.add(user.getString("user_name") + " " + user.getString("user_email") + " "
						+ user.getString("user_codigoPostal") + " " + user.getString("user_morada") + " "
						+ user.getString("user_state") + " " + user.getString("user_nif") + " "
						+ user.getString("user_telefone") + " " + user.getString("user_telemovel") + " "
						+ user.getString("user_role") + " " + user.getString("user_isPublic"));
			});
			return Response.ok(g.toJson(usersList)).build();

		}
		return Response.status(Status.FORBIDDEN).entity("Not possible.").build();

	}
}
