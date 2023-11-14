package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity.udp;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPDeviceConnection {

	private DatagramSocket clientSocketDevice;
    private InetSocketAddress inetAddress;
    private String ipAddress = "";
    private int port = 5010;
	private boolean connected = false;
	
	public DatagramSocket getClientSocketDevice() {
		return clientSocketDevice;
	}
	public void setClientSocketDevice(DatagramSocket clientSocketDevice) {
		this.clientSocketDevice = clientSocketDevice;
	}
	public InetSocketAddress getAddress() {
		return inetAddress;
	}
	public void setAddress(InetSocketAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
