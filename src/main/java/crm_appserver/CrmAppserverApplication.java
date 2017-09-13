package crm_appserver;

import firebase_connections.DeviceHandler;
import firebase_connections.GCMNotification;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sforceData.SForceClient;

@SpringBootApplication
@RestController
public class CrmAppserverApplication {

	private GCMNotification gcmNotification;
	private SForceClient sforceClient;

	public static void main(String[] args) {
		SpringApplication.run(CrmAppserverApplication.class, args);
	}

	{
		gcmNotification = new GCMNotification();
		sforceClient = new SForceClient();
		DeviceHandler.updateDevices(new String[] {
				sforceClient.getDeviceToken(DeviceHandler.getDevice(0).getPhoneNumber()),
				sforceClient.getDeviceToken(DeviceHandler.getDevice(1).getPhoneNumber()),
				sforceClient.getDeviceToken(DeviceHandler.getDevice(2).getPhoneNumber()),
				sforceClient.getDeviceToken(DeviceHandler.getDevice(3).getPhoneNumber())
		});
	}

	@PostMapping(value = "/appserver")
	public void onIncomingCall(@RequestBody String body) {
		JSONObject json;
		try {
			json = new JSONObject(body);
			String callerPhoneNumber = json.getJSONObject("callEventNotification").getString("callingParticipant");
			String calledPhoneNumber = json.getJSONObject("callEventNotification").getString("calledParticipant");
			//System.out.println("Caller: " + callerPhoneNumber + ", " + "Called: "+calledPhoneNumber);
			callerPhoneNumber = "00" + callerPhoneNumber.substring(5);
			calledPhoneNumber = "00" + calledPhoneNumber.substring(5, calledPhoneNumber.indexOf("@"));
			System.out.println("Caller: "+callerPhoneNumber + ", " + "Called: "+calledPhoneNumber);
			String[] contactInfo = sforceClient.getSFInfo(callerPhoneNumber);
			gcmNotification.messageFirebase(contactInfo, calledPhoneNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
