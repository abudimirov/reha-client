package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Procedure;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.ws.rs.core.Response;

@ManagedBean(name = "receiver")
@SessionScoped
@MessageDriven(name = "Receiver", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "testQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class Receiver implements MessageListener, Serializable {

    String text = "No info";
    List<Procedure> procedureList = new CopyOnWriteArrayList<>();

    public String getText() {
        return text;
    }

    public List<Procedure> getProcedureList() {
        return procedureList;
    }

    public static void main(String[] args) {

    }

    public void onMessage(Message rcvMessage) {
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
        procedureList = gson.fromJson(inputString, listOfProcedureObject);

        if (inputString != null) {
            for (Procedure procedure : procedureList) {
                System.out.println(procedure.toString());
            }
        }
    }
}
