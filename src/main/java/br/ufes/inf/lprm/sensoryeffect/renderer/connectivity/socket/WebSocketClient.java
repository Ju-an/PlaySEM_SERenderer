package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.socket;

import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class WebSocketClient {
    
    private WebSocketContainer container;
    private Session session;
    private boolean connected;

    public boolean isConnected(){
        return connected;
    };

    public void setConnected(boolean connected){
        this.connected = connected;
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
}
