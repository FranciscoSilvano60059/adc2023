package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.ChangeInfoData;
import pt.unl.fct.di.apdc.firstwebapp.util.ChangePasswordData;

import org.apache.commons.codec.digest.DigestUtils;

@Path("/changeInfo")
public class ChangeInfoResource {

	private static final Logger LOG = Logger.getLogger(ChangePasswordResource.class.getName());

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public ChangeInfoResource() {
	}

	@POST
	@Path("/")
	public Response changeInfoResource(ChangeInfoData data) {
		LOG.fine("Info changed by " + data.usernameResponsible);
		Transaction txn = datastore.newTransaction();
		try {
			// user
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
			Entity user = txn.get(userKey);
			String roleFromUser = user.getString("user_role");
			// responsavel
			Key userResponsibleKey = datastore.newKeyFactory().setKind("User").newKey(data.usernameResponsible);
			Entity userResponsible = txn.get(userResponsibleKey);
			String roleFromResponsible = userResponsible.getString("user_role");

			// só propria conta e nao mudar username, email ou nome, assim como role e
			// estado
			if (roleFromResponsible.equals("USER")) {
				user = Entity.newBuilder(userResponsibleKey).set("user_email", userResponsible.getString("user_email"))
						.set("user_name", userResponsible.getString("user_name")).set("user_nif", data.nif)
						.set("user_codigoPostal", data.codigoPostal).set("user_morada", data.morada)
						.set("user_telefone", data.telefoneFixo).set("user_telemovel", data.telefoneMovel)
						.set("user_isPublic", data.visibilidade)
						.set("user_role", userResponsible.getString("user_role"))
						.set("user_state", userResponsible.getString("user_state")).build();
				txn.put(user);
				LOG.info("User updated" + data.username);
				txn.commit();
				return Response.ok("{}").build();

			}
			// só propria conta ou USER e nao mudar username
			if (roleFromResponsible.equals("GBO")) {
				if (roleFromUser.equals("USER") || user == userResponsible)
					user = Entity.newBuilder(userKey).set("user_email", data.email).set("user_name", data.name)
							.set("user_nif", data.nif).set("user_codigoPostal", data.codigoPostal)
							.set("user_morada", data.morada).set("user_telefone", data.telefoneFixo)
							.set("user_telemovel", data.telefoneMovel).set("user_isPublic", data.visibilidade)
							.set("user_role", user.getString("user_role"))
							.set("user_state", data.state).build();
				txn.put(user);
				LOG.info("User updated" + data.username);
				txn.commit();
				return Response.ok("{}").build();

			} // só propria conta GBO ou USER e nao mudar username
			if (roleFromResponsible.equals("GS")) {
				if (roleFromUser.equals("USER") || roleFromUser.equals("GBO") || user == userResponsible)

					user = Entity.newBuilder(userKey).set("user_email", data.email).set("user_name", data.name)
							.set("user_nif", data.nif).set("user_codigoPostal", data.codigoPostal)
							.set("user_morada", data.morada).set("user_telefone", data.telefoneFixo)
							.set("user_telemovel", data.telefoneMovel).set("user_isPublic", data.visibilidade)
							.set("user_role", data.role).set("user_state", data.state).build();
				txn.put(user);
				LOG.info("User updated" + data.username);
				txn.commit();
				return Response.ok("{}").build();
			} // qualquer conta e nao mudar username
			if (roleFromResponsible.equals("SU")) {
				user = Entity.newBuilder(userKey).set("user_email", data.email).set("user_name", data.name)
						.set("user_nif", data.nif).set("user_codigoPostal", data.codigoPostal)
						.set("user_morada", data.morada).set("user_telefone", data.telefoneFixo).set("user_ocupacao", data.ocupacao)
						.set("user_telemovel", data.telefoneMovel).set("user_isPublic", data.visibilidade)
						.set("user_role", data.role).set("user_state", data.state).build();
				txn.put(user);
				LOG.info("User updated" + data.username);
				txn.commit();
				return Response.ok("{}").build();
			} else {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("Impossible change.").build();
			}
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

	}
}