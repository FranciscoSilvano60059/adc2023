package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.cloud.datastore.Key;
import javax.ws.rs.core.Response.Status;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.ProfileData;
import pt.unl.fct.di.apdc.firstwebapp.util.UserData;

@Path("/profile/")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class viewProfile {

    private final Gson g = new Gson();
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    public viewProfile() {
    }

    @POST
    @Path("/")
    public Response getProfile(UserData data) {
        //Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
        //Entity userEntity = datastore.get(userKey);
    	Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Entity userEntity = txn.get(userKey);
        if (userEntity == null) {
            return Response.status(Status.NOT_FOUND).entity("User not found").build();
        }

        ProfileData profile = new ProfileData(
                userEntity.getString("user_name"),
                userEntity.getString("user_email"),
                userEntity.getString("user_nif"),
                userEntity.getString("user_ocupacao"),
                userEntity.getString("user_codigoPostal"),
                userEntity.getString("user_morada"),
                userEntity.getString("user_telefone"),
                userEntity.getString("user_telemovel"),
                userEntity.getString("user_isPublic")
        );

        String profileJson = g.toJson(profile);
        return Response.ok(profileJson).build();
    }
}