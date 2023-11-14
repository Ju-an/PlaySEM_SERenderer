package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.socket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import br.ufes.inf.lprm.sensoryeffect.renderer.SERendererBroker;
import br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.ConnectivityBase;
import br.ufes.inf.lprm.utils.Utils;

public class SocketOutput extends ConnectivityBase{

    List<WebSocketClient> listSocketDevices = new ArrayList<>();	
	int numberOfDevices = 0;

    @Override
	public boolean isConnected() {
		for (int i = 0; i<listSocketDevices.size(); i++) {
			if (listSocketDevices.get(i).isConnected()){
				return true;
            }
		}
		return false;
	}

    @Override
    public void openConnection() {
        if (!isConnected()){
            try{
                listSocketDevices.clear();
				if (getProperties().get("number-of-devices") != null) {
					numberOfDevices = Integer.parseInt(getProperties().get("number-of-devices"));
					for (int i = 1; i <= numberOfDevices; i++) {
						if (getProperties().get("device0"+i+"-address") != null) {	
							try {
                                WebSocketClient client = new WebSocketClient();
								WebSocketContainer container = ContainerProvider.getWebSocketContainer();
								String address = getProperties().get("device0"+i+"-address");
                                URI uri = URI.create(address);
                                Session sess = container.connectToServer(client, uri);
								
                                client.setSession(sess);
                                client.setContainer(container);
                                client.setConnected(true);

								if (SERendererBroker.debugMode)
									System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": Connecting to "+ address);
							
								listSocketDevices.add(client);
								setConnected(true);
								
							} catch (NumberFormatException | IOException | DeploymentException e) {
								e.printStackTrace();
							}
						}
					}
				}

                if (isConnected()){
                    System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": UDP connection has been opened.");
                }
            }catch(NumberFormatException e) {
				e.printStackTrace();
			}
        }
    }

    @Override
    public void closeConnection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeConnection'");
    }

    @Override
    public void sendMessage(byte[] message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }
    
}
