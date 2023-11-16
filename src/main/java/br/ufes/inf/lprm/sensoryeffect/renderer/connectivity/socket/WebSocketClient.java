package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.socket;

import java.net.URI;

import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.ConnectionDeviceBase;

public class WebSocketClient extends ConnectionDeviceBase{
    
    private WebSocketContainer container;
    private Session session;
    protected String address;
    protected URI uri;

    public WebSocketClient(int id) {
        super(id);
    }

    public WebSocketContainer getContainer() {
        return container;
    }

    public void setContainer(WebSocketContainer container) {
        this.container = container;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
