package br.ufes.inf.lprm.sensoryeffect.renderer.device.bhaptics;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.json.simple.JSONObject;


import br.ufes.inf.lprm.sensoryeffect.renderer.device.SensoryEffectDeviceBase;
import br.ufes.inf.lprm.sensoryeffect.renderer.message.SensoryEffectMessage;
import br.ufes.inf.lprm.sensoryeffect.renderer.message.TimeLineDeviceCommand;
import br.ufes.inf.lprm.sensoryeffect.renderer.message.VibrationMessage;
import br.ufes.inf.lprm.sensoryeffect.renderer.metadata.parser.mpegv.ClassificationScheme;

public class BHapticsVibrationDevice extends SensoryEffectDeviceBase {

    private String appId;
    private String appKey;

    private final static List<String> VIBRATION_X = Arrays.asList(
                                                        ClassificationScheme.LOC_X_LEFT,
                                                        ClassificationScheme.LOC_X_CENTERLEFT,
                                                        ClassificationScheme.LOC_X_CENTER,
                                                        ClassificationScheme.LOC_X_CENTERRIGHT,
                                                        ClassificationScheme.LOC_X_RIGHT);
    private final static List<String> VIBRATION_Y = Arrays.asList(
                                                        ClassificationScheme.LOC_Y_BOTTOM,
                                                        ClassificationScheme.LOC_Y_MIDDLE,
                                                        ClassificationScheme.LOC_Y_TOP);
    private final static List<String> VIBRATION_Z = Arrays.asList(
                                                        ClassificationScheme.LOC_Z_BACK,
                                                        ClassificationScheme.LOC_Z_MIDWAY,
                                                        ClassificationScheme.LOC_Z_FRONT);
    @Override
    public Vector<TimeLineDeviceCommand> messageToDeviceCommand(SensoryEffectMessage sensoryEffectMessage) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'messageToDeviceCommand'");
    }

    @Override
    public void resetDevice() {
        this.appId = this.getProperties().get("app-id");
        this.appKey = this.getProperties().get("app-key");
        
        VibrationMessage vibrationMessage = new VibrationMessage();
		vibrationMessage.setIntensity(0);
		vibrationMessage.setLocation(ClassificationScheme.LOCATIONURIBASE + ClassificationScheme.LOC_X_LEFT);
		byte[] messageVib1 = formatMessage(vibrationMessage);
		
		VibrationMessage vibrationMessage2 = new VibrationMessage();
		vibrationMessage2.setIntensity(0);
		vibrationMessage2.setLocation(ClassificationScheme.LOCATIONURIBASE + ClassificationScheme.LOC_X_RIGHT);
		byte[] messageVib2 = formatMessage(vibrationMessage2);
		
		this.getDeviceConnectivity().sendMessage(messageVib1);
		this.getDeviceConnectivity().sendMessage(messageVib2);

    }

    @Override
    public byte[] formatMessage(SensoryEffectMessage sensoryEffectMessage) {
        JSONObject msg = new JSONObject();
        
        VibrationMessage vibrationMessage = (VibrationMessage)sensoryEffectMessage;
        String locations[] = vibrationMessage.getLocation().split(":");
        locations = Arrays.copyOfRange(locations, 4, locations.length);
        String x = "*", y = "*", z = "*";
        if(locations.length == 1){
            if(VIBRATION_X.contains(locations[0])){
                x = locations[0];
            }else if(VIBRATION_Y.contains(locations[0])){
                y = locations[0];
            }else if(VIBRATION_Z.contains(locations[0])){
                z = locations[0];
            }else {
                return null;
            }
        }else{
            x = locations[0];
            y = locations[1];
            z = locations[2];
        }

        xyzToAngles(x, y, z);

        return jsonToByteArray(msg);
    }

    /*º x
    z   -45	-23	0	23	45
        -90	-90	0	90	90
        -135	-157	180	157	135
     */
    public int[] xyzToAngles(String x, String y, String z){
        int[] angles;
        //if neither is wildcard, then return a single point
        if(!x.equals("*") && !y.equals("*") && !z.equals("*")){
            angles = new int[0];
            angles[0] = xyzToAngle(x, y, z);
            return angles;
        }
        //if only one is wildcard, then return a line
        if(x.equals("*") && !y.equals("*") && !z.equals("*")){
            //This is not correct solution, as there may be points in between the angles
            //TODO: Convert to all the points in vest
            angles = new int[5];
            angles[0] = xyzToAngle("left", y, z);
            angles[1] = xyzToAngle("centerleft", y, z);
            angles[2] = xyzToAngle("center", y, z);
            angles[3] = xyzToAngle("centerright", y, z);
            angles[4] = xyzToAngle("right", y, z);
            return angles;
        }
        angles = new int[6];
        return angles;
    }

    public int xyzToAngle(String x, String y, String z){
        int angle = 0;
        return angle;
    }

    public byte[] jsonToByteArray(JSONObject obj){
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //Json.createWriter(stream).write(obj); //import javax.json.JsonWriter;

        //byte[] sendData = stream.toByteArray();
        return obj.toJSONString().getBytes();
    }

    /*
def submit_dot(key, position, dot_points, duration_millis):
front_frame = {
    "position": position,
    "dotPoints": dot_points,
    "durationMillis": duration_millis
}
submit(key, front_frame)


def submit(key, frame):
request = {
    "Submit": [{
        "Type": "frame",
        "Key": key,
        "Frame": frame
    }]
}
json_str = json.dumps(request);
__submit(json_str)


def __submit(json_str):
if ws is not None:
    ws.send(json_str)
*/
}