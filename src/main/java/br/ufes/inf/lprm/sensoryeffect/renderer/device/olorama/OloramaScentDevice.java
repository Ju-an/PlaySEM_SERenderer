package br.ufes.inf.lprm.sensoryeffect.renderer.device.olorama;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import br.ufes.inf.lprm.sensoryeffect.renderer.device.SensoryEffectDeviceBase;
import br.ufes.inf.lprm.sensoryeffect.renderer.message.ScentMessage;
import br.ufes.inf.lprm.sensoryeffect.renderer.message.SensoryEffectMessage;
import br.ufes.inf.lprm.sensoryeffect.renderer.message.TimeLineDeviceCommand;
import br.ufes.inf.lprm.sensoryeffect.renderer.metadata.parser.mpegv.ClassificationScheme;

public class OloramaScentDevice extends SensoryEffectDeviceBase {

    @Override
    public Vector<TimeLineDeviceCommand> messageToDeviceCommand(SensoryEffectMessage sensoryEffectMessage) {
        Vector<TimeLineDeviceCommand> ret = new Vector<TimeLineDeviceCommand>();
		ScentMessage scentMessage = (ScentMessage)sensoryEffectMessage;
		byte[] message = formatMessage(scentMessage);
		Long time = getTimeCompensated(scentMessage.getOccurrenceTime());
		ret.add(new TimeLineDeviceCommand(time, message));
		return ret;
    }

    @Override
    public void resetDevice() {
        ScentMessage scentMessage = new ScentMessage();
		scentMessage.setIntensity(0);
		scentMessage.setLocation(ClassificationScheme.LOCATIONURIBASE + ClassificationScheme.LOC_EVERYWHERE);
		byte[] messageScent = formatMessage(scentMessage);
		this.getDeviceConnectivity().sendMessage(messageScent);
    }

    public boolean validValues(String id, int intensity, int duration){
        //Check if id is either of valid values: "00" to "10"
        if(!id.matches("^0[1-9]|10$")){
			return false;
		}
		if(intensity < 0 || intensity > 9999){
			return false;
		}else if(intensity < 100 || intensity > 500){
			System.out.println("Intensity outside its recommended range of 100 and 500ms");
		}
		if(duration < 0 || duration > 99999){
			return false;
		}else if(duration < 4000 || duration > 7000){
			System.out.println("Duration outside its recommended range of 4000 and 7000ms");
		}
        return true;
    }

    @Override
    public byte[] formatMessage(SensoryEffectMessage sensoryEffectMessage) {
        ScentMessage scentMessage = (ScentMessage)sensoryEffectMessage;
		String slotToActivate = "00";

        
        Map<String, String> scentSlots = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            String slotNumber = String.format("%02d", i);
            scentSlots.put(this.getProperties().get("ScentSlot" + slotNumber), slotNumber);
        }

        for (Map.Entry<String, String> entry : scentSlots.entrySet()) {
            if (scentMessage.getScent().equals(entry.getKey())) {
                slotToActivate = entry.getValue();
                break;
            }
        }

        boolean fanActivation = true;
        
        int intensity = scentMessage.getIntensity();
        int fan = fanActivation ? 1 : 0;
        int duration = 4000;

        if(validValues(slotToActivate, intensity, duration)){
            String msg = String.format("OUT,%s,%04d,%d,%05d,1000", slotToActivate, intensity, fan, duration);
            byte[] message = msg.getBytes();
            return message;
        }
        return null;
    }
    
}
