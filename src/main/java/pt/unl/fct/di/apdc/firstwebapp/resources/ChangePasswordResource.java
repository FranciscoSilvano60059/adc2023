package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

import pt.unl.fct.di.apdc.firstwebapp.util.ChangePasswordData;

import org.apache.commons.codec.digest.DigestUtils;

@Path("/changePassword")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ChangePasswordResource {

    private static final Logger LOG = Logger.getLogger(ChangePasswordResource.class.getName());

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    public ChangePasswordResource() {
    }

    @POST
    @Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordData data) {
        LOG.fine("Attempt to change password for user: " + data.username);
        if (!data.validChangePassword()) {
            return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
        }

        Transaction txn = datastore.newTransaction();
        try {
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
            Entity user = txn.get(userKey);
            
           

            user = Entity.newBuilder(userKey)
                    .set("user_name", user.getString("user_name"))
                    .set("user_pwd", DigestUtils.sha512Hex(data.newPassword))
                    .set("user_email", user.getString("user_email"))
                    .set("user_creation_time", user.getTimestamp("user_creation_time"))
                    .set("user_role", user.getString("user_role"))
                    .set("user_nif", user.getString("user_nif"))
                    .set("user_codigoPostal", user.getString("user_codigoPostal"))
                    .set("user_morada", user.getString("user_morada"))
                    .set("user_telefone", user.getString("user_telefone"))
                    .set("user_telemovel", user.getString("user_telemovel"))
                    .set("user_ocupacao", user.getString("user_ocupacao"))
                    .set("user_isPublic", user.getString("user_isPublic"))
                    .set("user_state", user.getString("user_state"))
                    .build();

            txn.update(user);
            LOG.info("Password changed for user: " + data.username);
            txn.commit();
            return Response.ok("{}").build();

        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }
    }
