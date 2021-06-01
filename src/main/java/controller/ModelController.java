package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Procedure;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Model
public class ModelController {

    @Produces
    @Named
    public List<Procedure> getProceduresFromAPI(){
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = null;
        String inputString = null;
        try {
            ResteasyWebTarget target = client.target("http://localhost:8082/api/procedures");
            response = target.request().get();
            inputString = response.readEntity(String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }


        Type listOfProcedureObject = new TypeToken<ArrayList<Procedure>>() {}.getType();
        Gson gson = new Gson();
        List<Procedure> procedureList = gson.fromJson(inputString, listOfProcedureObject);

        if (inputString != null) {
            for (Procedure procedure : procedureList) {
                System.out.println(procedure.toString());
            }
        }
        return procedureList;
    }
}
