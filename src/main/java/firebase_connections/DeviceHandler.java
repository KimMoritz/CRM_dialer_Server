package firebase_connections;

import java.util.ArrayList;

public class DeviceHandler {

    private static ArrayList<Device> devices = new ArrayList<>();

    /*static void setupDevices(){
        Device testPhone17 = new Device("TestPhone17", "004917268050017", "cxXUm_ufkLs:APA91bF4aryeJEd-yhwtolyD73cZ8c1PCwavY1mKE-luTsZYJOOQi2uNnKU0onfnCjA5KPZrqlrMhbvfBZdKCXa0VT-V6CHtnfO98EWL91Gv0-baJ_G8z-MWaGIArybGp-JmAWSEt4W3");
        Device testPhone18 = new Device("TestPhone18", "004917268050018", "f5500LDRIRI:APA91bFybC0r454MfVbISIKmnmbb1Vym4SHLTt4C4IoTESf6P6KYrcAmfwcmQjRl-0NmYDeIboopCzTrNxy7jN471RFhgRfQ4pOpYI0xSXGLbnCen501_Srvc9L76_QflKGc_UXZiju7");
        addDevice(testPhone17);
        addDevice(testPhone18);
    }*/

    static void setupDevices(){
        Device testPhone17 = new Device("TestPhone17", "004917268050017", "");
        Device testPhone18 = new Device("TestPhone18", "004917268050018", "");
        addDevice(testPhone17);
        addDevice(testPhone18);
    }

    public static void updateDevices(String [] phoneNumbers){
        devices.clear();
        Device testPhone17 = new Device("TestPhone17", "004917268050017", phoneNumbers[0]);
        Device testPhone18 = new Device("TestPhone18", "004917268050018", phoneNumbers[1]);
        addDevice(testPhone17);
        addDevice(testPhone18);
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
