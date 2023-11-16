package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.ufes.inf.lprm.sensoryeffect.renderer.SERendererBroker;
import br.ufes.inf.lprm.utils.Utils;

public abstract class ConnectivityAdvanced<D extends ConnectionDeviceBase, M> extends ConnectivityBase{

    protected List<D> deviceList = new ArrayList<>();	
	protected int numberOfDevices = 0;

    protected abstract D instantiateDevice(int i);
    protected abstract M generateMessage(byte[] message);
    protected abstract boolean canSend(M message, D device);
    protected abstract void send(M message, D device);
    protected abstract void connectDevice(D device);
    protected abstract void disconnectDevice(D device);
    
    @Override
    public void openConnection() {
        if (!isConnected()){
            deviceList.clear();
            if (getProperties().get("number-of-devices") != null) {
                numberOfDevices = Integer.parseInt(getProperties().get("number-of-devices"));
                    if(numberOfDevices > 99){
                        throw new UnsupportedOperationException("Cannot handle more than 99 devices for the same Connectivity Interface.");
                    }
					for (int i = 1; i <= numberOfDevices; i++) {
                        try{
                        D device = instantiateDevice(i);
                        if (SERendererBroker.debugMode){
							System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": Connecting to "+ device.devicePrefix);
                        }
                        connectDevice(device);
                        device.setConnected(true);
                        deviceList.add(device);
                        
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isConnected()){
                System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": connection has been opened.");
            }
        }
    }

    @Override
    public void closeConnection() {
        for(int i = 0; i < numberOfDevices; i++){
            disconnectDevice(deviceList.get(i));
        }
        setConnected(false);
    }

    @Override
    public void sendMessage(byte[] message) {
        if(message == null){
            return;
        }

        if (!isConnected()){
            openConnection();
        }

        M messageToSend = generateMessage(message);

        try {
            for (int i = 0; i<numberOfDevices; i++) {
                D device = deviceList.get(i);
                if (device.isConnected() && canSend(messageToSend, device)) {
                    send(messageToSend, device);
                    if (SERendererBroker.debugMode){
                        System.out.println(Utils.dateFormat.format(Calendar.getInstance().getTime()) + ": message sent to "+ device);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isConnected() {
		for (int i = 0; i<deviceList.size(); i++) {
			if (deviceList.get(i).isConnected()){
				return true;
            }
		}
		return false;
	}

    @Override
	public void setConnected(boolean isConnected) {
		for (int i = 0; i<deviceList.size(); i++) {
			deviceList.get(i).setConnected(isConnected);
		}
	}

}
