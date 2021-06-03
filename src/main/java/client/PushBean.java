package client;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
public class PushBean implements Serializable {

    @Inject
    @Push(channel = "websocket")
    private PushContext push;

    public void sendUpdate() {
        push.send("update");
    }

}