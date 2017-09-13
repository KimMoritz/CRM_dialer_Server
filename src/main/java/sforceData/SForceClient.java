package sforceData;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class SForceClient {

    private static final String userName = resourceBundle.getString("sf_username");
    private static final String password = resourceBundle.getString("sf_password");
    private static final String loginInstanceDomain = resourceBundle.getString("sf_logininstancedomain");
    private static final String apiVersion = esourceBundle.getString("sf_apiversion");
    private static final String consumerKey = resourceBundle.getString("sf_consumerkey");
    private static final String consumerSecret = resourceBundle.getString("sf_consumersecret");
    private static final String grantType = "password";
    private Header oauthHeader;
    private Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
    private Timer timer;

    public SForceClient(){
        timer = new Timer();
        TimerTask hourlyLogin = new TimerTask() {
            @Override
            public void run() {
                oauth2Login();
            }
        };
        timer.schedule(hourlyLogin, 1800000,3600000);
        oauth2Login();
    }

    private void oauth2Login() {
        String loginHostUri = "https://" + loginInstanceDomain + "/services/oauth2/token";
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(loginHostUri);
            String requestBodyText = "grant_type=" + grantType + "&username=" + userName +
                    "&password=" + password + "&client_id=" + consumerKey + "&client_secret=" + consumerSecret;
            StringEntity requestBody = new StringEntity(requestBodyText);
            requestBody.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(requestBody);
            httpPost.addHeader(prettyPrintHeader);
            HttpResponse response = httpClient.execute(httpPost);

            if (  response.getStatusLine().getStatusCode() == 200 ) {
                String response_string = EntityUtils.toString(response.getEntity());
                JSONObject json = new JSONObject(response_string);
                OAuth2Response oauth2Response = new OAuth2Response(json);
                String REST_ENDPOINT = "/services/data";
                String baseUri = oauth2Response.instance_url + REST_ENDPOINT + "/v" + apiVersion + ".0";
                oauthHeader = new BasicHeader("Authorization", "OAuth " + oauth2Response.access_token);
                System.out.println("\nSuccessfully logged in to instance: " + baseUri);
            } else {
                System.out.println("An error has occured. Http status: " + response.getStatusLine().getStatusCode());
                System.out.println(getBody(response.getEntity().getContent()));
                System.out.println(response.getStatusLine().getReasonPhrase());
                System.out.println(response.getStatusLine().getStatusCode());
                System.exit(-1);
            }
        } catch (JSONException |IOException | NullPointerException ioe) {
            ioe.printStackTrace();
        }
    }

    public String[] getSFInfo(String phoneNumber){
        String[] contactInfo = new String[10];
        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            String baseUri = "https://" + loginInstanceDomain +"/" + "services/data/v" + apiVersion + "/";
            String uri = baseUri + "query?q=SELECT+name+,+phone+,+email+,+id+,+mobilephone,+" +
                    "accountid+,+Account.Name+,+Account.Website+" +
                    "FROM+Contact+WHERE+phone='" + phoneNumber + "'";
            String uri2 = baseUri + "query?q=" +
                    "SELECT+OwnerId,+Subject,+Reason,+Description+" +
                    "FROM+Case+WHERE+Contact.phone='" + phoneNumber + "'";
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);
            HttpResponse response = httpClient.execute(httpGet);
            httpGet = new HttpGet(uri2);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);
            HttpResponse response2 = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                JSONObject json2 = new JSONObject(EntityUtils.toString(response2.getEntity()));
                System.out.println(json.getJSONArray("records").getJSONObject(0).getString("Id"));
                contactInfo[0] = json.getJSONArray("records").getJSONObject(0).getString("Name");
                contactInfo[1] = json.getJSONArray("records").getJSONObject(0).getString("Phone");
                contactInfo[2] = json.getJSONArray("records").getJSONObject(0).getString("Email");
                contactInfo[3] = json.getJSONArray("records").getJSONObject(0).getString("MobilePhone");
                contactInfo[4] = json.getJSONArray("records").getJSONObject(0).getJSONObject("Account").getString("Website");
                contactInfo[5] = json.getJSONArray("records").getJSONObject(0).getJSONObject("Account").getString("Name");
                contactInfo[6] = json.getJSONArray("records").getJSONObject(0).getString("AccountId");
                contactInfo[7] = json2.getJSONArray("records").getJSONObject(0).getString("Reason");
                contactInfo[8] = json2.getJSONArray("records").getJSONObject(0).getString("Subject");
                contactInfo[9] = json2.getJSONArray("records").getJSONObject(0).getString("Description");
            } else {
                System.out.println("Query was unsuccessful. Status :" + statusCode + " " + response.getStatusLine().getReasonPhrase());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return contactInfo;
    }

    public String getDeviceToken(String phoneNumber){
        String contactInfo = "";
        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            String baseUri = "https://" + loginInstanceDomain +"/" + "services/data/v" + apiVersion + "/";
            String uri = baseUri + "query?q=SELECT+description+" +
                    "FROM+Contact+WHERE+phone='" + phoneNumber + "'";
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);
            HttpResponse response = httpClient.execute(httpGet);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                contactInfo = json.getJSONArray("records").getJSONObject(0).getString("Description");
            } else {
                System.out.println("Query was unsuccessful. Status :" + statusCode + " " + response.getStatusLine().getReasonPhrase());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return contactInfo;
    }

    private String getBody(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream)
            );
            String inputLine;
            while ( (inputLine = in.readLine() ) != null )
                result.append(inputLine).append("\n");
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toString();
    }

    static class OAuth2Response {
        String id;
        String issued_at;
        String instance_url;
        String signature;
        String access_token;

        OAuth2Response(JSONObject json) {
            try {
                id =json.getString("id");
                issued_at = json.getString("issued_at");
                instance_url = json.getString("instance_url");
                signature = json.getString("signature");
                access_token = json.getString("access_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}