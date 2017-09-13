package firebase_connections;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class MessageSender {
    private String urlParameters;
    private String firebaseUrl;
    private String host;
    private String requestMethod;
    private String contentType;
    private String serverKey;

    MessageSender(){
        this.firebaseUrl = "https://fcm.googleapis.com/fcm/send";
        this.host = "fcm.googleapis.com";
        this.requestMethod = "POST";
        this.contentType = "application/json;charset=utf-8";
        this.serverKey = resourceBundle.getString("firebase_key");
    }

    void postMessage() throws Exception {
        try {
            URL url = new URL(firebaseUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setRequestProperty("Host", host);
            httpURLConnection.setRequestProperty("Authorization", serverKey);
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(urlParameters);
            out.flush();
            out.close();
            System.out.println("Firebase response code: " + httpURLConnection.getResponseCode() + ": " + httpURLConnection.getResponseMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setUrlParameters(String token, String [] contactInfo){
        this.urlParameters = "{ \n" +
                "\"data\": \n" +
                "  {\n" +
                "\"Name\" : \"" +  contactInfo[0] +"\"\n"+
                "\"Phone\" : \"" +  contactInfo[1] +"\"\n"+
                "\"Email\" : \"" +  contactInfo[2] +"\"\n"+
                "\"MobilePhone\" : \"" +  contactInfo[3] +"\"\n"+
                "\"AccountWebsite\" : \"" +  contactInfo[4] +"\"\n"+
                "\"AccountName\" : \"" +  contactInfo[5] +"\"\n"+
                "\"AccountId\" : \"" +  contactInfo[6] +"\"\n"+
                "\"CaseReason\" : \"" +  contactInfo[7] +"\"\n"+
                "\"CaseSubject\" : \"" +  contactInfo[8] +"\"\n"+
                "\"CaseDescription\" : \"" +  contactInfo[9] +"\"\n"+
                "\n" +
                "  },\n" +
                "\"to\" : \""+ token +"\"\n" +
                "}";
    }

}
