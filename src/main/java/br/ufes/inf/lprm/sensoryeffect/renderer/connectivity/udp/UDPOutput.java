package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.udp;

import br.ufes.inf.lprm.sensoryeffect.renderer.SERendererBroker;
import br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.ConnectivityBase;
import br.ufes.inf.lprm.utils.Utils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPOutput extends ConnectivityBase {

    List<UDPDeviceConnection> listUDPDevices = new ArrayList<>();	
	int numberOfDevices = 0;
	
	@Override
	public boolean isConnected() {
		for (int i = 0; i<listUDPDevices.size(); i++) {
			if (listUDPDevices.get(i).isConnected()){
				return true;
			}
		}
		return false;
	}


    @Override
    public void openConnection() {
        if (!isConnected()){
            try{

                listUDPDevices.clear();
				if (getProperties().get("number-of-devices") != null) {
					numberOfDevices = Integer.parseInt(getProperties().get("number-of-devices"));
					for (int i = 1; i <= numberOfDevices; i++) {
						if (getProperties().get("device0"+i+"-address") != null) {	
							try {
								UDPDeviceConnection udpDevice = new UDPDeviceConnection();
								String[] address = getProperties().get("device0"+i+"-address").split(":");
								udpDevice.setIpAddress(address[0]);
								udpDevice.setPort(Integer.valueOf(address[1]));
								
								if (SERendererBroker.debugMode)
									System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": Connecting to "+ udpDevice.getIpAddress() + ":" + udpDevice.getPort());
							
								DatagramSocket clientSocketDevice = new DatagramSocket();
                                InetSocketAddress inetAddress = new InetSocketAddress(udpDevice.getIpAddress(), udpDevice.getPort());
							
								udpDevice.setClientSocketDevice(clientSocketDevice);
								udpDevice.setAddress(inetAddress);

								udpDevice.setConnected(true);
								listUDPDevices.add(udpDevice);
								setConnected(true);
								
							} catch (NumberFormatException | IOException e) {
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
			if (isConnected()) {
				for (int i = 0; i<listUDPDevices.size(); i++) {
					if (listUDPDevices.get(i).isConnected()) {
						if (SERendererBroker.debugMode)
							System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": Closing connection with "+ listUDPDevices.get(i).getIpAddress() + ":" + listUDPDevices.get(i).getPort());
						
						listUDPDevices.get(i).getClientSocketDevice().close();
					}
				}
			}
            setConnected(false);
			System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": UDP connection has been closed.");
    }

    @Override
    public void sendMessage(byte[] message) {
        if(message == null){
            return;
        }

        if (!isConnected()){
            openConnection();
        }

        try {
            for (int i = 0; i<listUDPDevices.size(); i++) {
                if (listUDPDevices.get(i).isConnected()) {
                    DatagramPacket packet = new DatagramPacket(message, message.length, listUDPDevices.get(i).getAddress());
                    try {
                        listUDPDevices.get(i).getClientSocketDevice().send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    if (SERendererBroker.debugMode){
                        System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": UDP message sent to "+ listUDPDevices.get(i).getIpAddress() + ":" + listUDPDevices.get(i).getPort());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
