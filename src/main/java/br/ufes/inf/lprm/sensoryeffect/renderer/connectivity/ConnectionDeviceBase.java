package br.ufes.inf.lprm.sensoryeffect.renderer.connectivity;

public abstract class ConnectionDeviceBase implements ConnectionDevice{
    protected boolean connected;
    protected int id;
    protected String devicePrefix;

    public ConnectionDeviceBase(int id){
        connected = false;
        this.id = id;
        devicePrefix = String.format("device%02d", id);
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setConnected(boolean isConnected) {
        connected = isConnected;
    }

    @Override
    public String toString(){
        return this.getClass() + devicePrefix;
    }

    public String getDevicePrefix() {
        return devicePrefix;
    }

    public int getId() {
        return id;
    }

}
