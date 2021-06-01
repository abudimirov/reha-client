package client;

import controller.ModelController;
import model.Procedure;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ManagedBean(name = "receiver")
@SessionScoped
@MessageDriven(name = "Receiver", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "testQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class Receiver implements MessageListener, Serializable {

    List<Procedure> procedureList = new CopyOnWriteArrayList<>();

    @Inject
    private ModelController modelController;


    public void onMessage(Message rcvMessage) {
        procedureList = modelController.getProceduresFromAPI();

    }
}
