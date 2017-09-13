package firebase_connections;

public class Device {

    private String phoneNumber;
    private String deviceName;
    private String fireBaseToken;

    public Device(String name, String number, String token ){
        phoneNumber = number;
        deviceName = name;
        fireBaseToken = token;
    }

    public String getFireBaseToken() {
        return fireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }
}
