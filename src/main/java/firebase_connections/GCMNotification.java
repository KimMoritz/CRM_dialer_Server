package firebase_connections;
import org.springframework.stereotype.Component;

@Component
public class GCMNotification {
    private MessageSender messageSender;

    public GCMNotification(){
        messageSender = new MessageSender();
        DeviceHandler.setupDevices();
    }

    public void messageFirebase(String [] contactInfo, String phoneNumber){
        try {
            messageSender.setUrlParameters(DeviceHandler.getDevice(phoneNumber).getFireBaseToken(), contactInfo);
            messageSender.postMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}