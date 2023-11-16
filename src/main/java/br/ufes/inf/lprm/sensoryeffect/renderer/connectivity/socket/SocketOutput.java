package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.ConnectivityAdvanced;

public class SocketOutput extends ConnectivityAdvanced<WebSocketClient, String>{

    public final static String ENCODING = "UTF-8";

    @Override
    protected WebSocketClient instantiateDevice(int i) {
        WebSocketClient client = new WebSocketClient(i);
        String address = getProperties().get(client.getDevicePrefix()+"-address");
        URI uri = URI.create(address);
        client.setUri(uri);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        client.setContainer(container);
        return client;
    }

    @Override
    protected String generateMessage(byte[] message) {
        try {
            return new String(message, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void send(String message, WebSocketClient client) {
        try {
            client.getSession().getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void connectDevice(WebSocketClient client) {
        Session sess;
        try {
            sess = client.getContainer().connectToServer(client, client.getUri());
            client.setSession(sess);
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void disconnectDevice(WebSocketClient client) {
        try {
            client.getSession().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean canSend(String message, WebSocketClient device) {
        return true;
    }


}
