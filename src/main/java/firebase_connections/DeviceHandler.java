package firebase_connections;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeviceHandler {

    static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    private static ArrayList<Device> devices = new ArrayList<>();

    static void setupDevices(){
        Device testPhone17 = new Device("TestPhone17", resourceBundle.getString("testphone17"), "");
        Device testPhone18 = new Device("TestPhone18", resourceBundle.getString("testphone18"), "");
        Device testPhone38 = new Device("TestPhone38", resourceBundle.getString("testphone38"), "");
        Device testPhone39 = new Device("TestPhone39", resourceBundle.getString("testphone39"), "");
        addDevice(testPhone17);
        addDevice(testPhone18);
        addDevice(testPhone38);
        addDevice(testPhone39);
    }

    public static void updateDevices(String [] phoneNumbers){
        devices.clear();
        Device testPhone17 = new Device("TestPhone17", resourceBundle.getString("testphone17"), phoneNumbers[0]);
        Device testPhone18 = new Device("TestPhone18", resourceBundle.getString("testphone18"), phoneNumbers[1]);
        Device testPhone38 = new Device("TestPhone38", resourceBundle.getString("testphone38"), phoneNumbers[2]);
        Device testPhone39 = new Device("TestPhone39", resourceBundle.getString("testphone39"), phoneNumbers[3]);
        addDevice(testPhone17);
        addDevice(testPhone18);
        addDevice(testPhone38);
        addDevice(testPhone39);
    }

    public static ArrayList<Device> getDevices() {
        return devices;
    }

    public static Device getDevice(int index) {
        return devices.get(index);
    }

    public static Device getDevice(String phoneNumber) {
        for (int i = 0; i<devices.size(); i++){
            if(devices.get(i).getPhoneNumber().equals(phoneNumber)){
                return devices.get(i);
            }
        }
        return null;
    }

    public static int getDeviceIndex(String phoneNumber){
        for (int i = 0; i<devices.size(); i++){
            if(devices.get(i).getPhoneNumber().equals(phoneNumber)){
                return i;
            }
        }
        return -1;
    }

    public static void addDevice(Device device) {
        devices.add(device);
    }

}
