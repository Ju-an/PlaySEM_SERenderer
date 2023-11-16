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

    public VibrationMessage createVibration(int intensity, String location){
        VibrationMessage vibrationMessage = new VibrationMessage();
        vibrationMessage.setLocation(location);
        vibrationMessage.setIntensity(intensity);
        return vibrationMessage;
    }

    @Override
    public void resetDevice() {
        this.appId = this.getProperties().get("app-id");
        this.appKey = this.getProperties().get("app-key");
        
        VibrationMessage vibrationMessage = createVibration(0, ClassificationScheme.LOCATIONURIBASE + ClassificationScheme.LOC_X_LEFT);
		byte[] messageVib1 = formatMessage(vibrationMessage);
		
		VibrationMessage vibrationMessage2 = createVibration(0, ClassificationScheme.LOCATIONURIBASE + ClassificationScheme.LOC_X_RIGHT);
		byte[] messageVib2 = formatMessage(vibrationMessage2);
		
		this.getDeviceConnectivity().sendMessage(messageVib1);
		this.getDeviceConnectivity().sendMessage(messageVib2);
    }

    @Override
    public byte[] formatMessage(SensoryEffectMessage sensoryEffectMessage) {
        JSONObject msg = new JSONObject();
        
        VibrationMessage vibrationMessage = (VibrationMessage)sensoryEffectMessage;
        String[] locations = vibrationMessage.getLocation().split(":");
        locations = Arrays.copyOfRange(locations, 4, locations.length);
        String x = "*", y = "*", z = "*";
        x = locations[0];
        y = locations[1];
        z = locations[2];
        int[] points = {};
        if("*".equals(y)){
            if("*".equals(x)){
                points = VestAngles.ALL;
            }else{
                switch(x){
                    case ClassificationScheme.LOC_X_LEFT:
                        points = VestAngles.LEFT;
                    break;
                    case ClassificationScheme.LOC_X_CENTERLEFT:
                        points = VestAngles.CENTERLEFT;
                    break;
                    case ClassificationScheme.LOC_X_CENTER:
                        points = VestAngles.CENTER;
                    break;
                    case ClassificationScheme.LOC_X_CENTERRIGHT:
                        points = VestAngles.CENTERRIGHT;
                    break;
                    case ClassificationScheme.LOC_X_RIGHT:
                        points = VestAngles.RIGHT;
                    break;
                }
            }
        }else{

        }
        /*
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
        }*/

        xyzToAngles(x, y, z);

        return jsonToByteArray(msg);
    }

    public int[] xyzToAngles(String x, String y, String z){
        int[] angles;
        //if neither is wildcard, then return a single point
        if(!x.equals("*") && !y.equals("*") && !z.equals("*")){
            angles = new int[1];
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
            //TODO: remove duplicated angles from list
            return angles;
        }
        angles = new int[6];
        return angles;
    }

    public int xyzToAngle(String x, String y, String z){
        int angle = 0;
        return angle;
    }

    public int xyzToHeight(String x, String y, String z){
        return 0;
    }

    public static int closestIndex(int[] array, int number){
        int closest = 0;
        int current = 360;
        for(int i=0; i<array.length; i++){
            int error = Math.abs(array[i] - number);
            if(error < current){
                current = error;
                closest = i;
            }
        }
        return closest;
    }

    /**
     * Returns the haptic vest point (from the 40 points) based on angle and height
     * @param angle 0 to 360º (0,360 = frontal center)
     * @param height 0 to 100% (0 = bottom, 100% = top)
     * @return
     */
    public static int directionToDot(int angle, int height){
        int[] angles = {0, 45, 90, 135, 180, 225, 270, 315, 360};
        int[] horizontalDots = {2, 3, 23, 22, 21, 20, 0, 1, 2};
        int[] heights = {100, 75, 50, 25, 0};
        int[] verticalOffsets = {0, 4, 8, 12, 16};
        int firstDot = horizontalDots[closestIndex(angles, angle)];
        int dotOffset = verticalOffsets[closestIndex(heights, height)];
        //TODO: Extrapolate to multiple close points instead of the nearest. For this, the intensity should be divided
        return firstDot + dotOffset;
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