package opendroid.nox.opendroid;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Brian on 31/03/2015.
 */
public class HttpManager {
    static String token = null;
    static  String tokenId = null;
    static String tenantId = null;
    static String responseCode = null;
    static String endPoint = "http://95.44.212.163/";
    //static AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");

    public static String getData(String uri){

        AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
        HttpGet request = new HttpGet(uri);
        request.setHeader("X-Auth-Token",tokenId);
        HttpResponse response;


        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String re = EntityUtils.toString(entity);
            Log.i("TAG", "Instance request :" + re);
            return re;

        } catch (IOException e) {
            e.printStackTrace();
            return  "error"+ e;
        } finally {
            client.close();
        }
    }

    public static String login(String uri, String... params){

        AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
        HttpPost httppost = new HttpPost(uri);
        int r = 0;
        String code;
        String a = "{\n" +
                "    \"auth\": {\n" +
                "        \"tenantName\": \"admin\",\n" +
                "        \"passwordCredentials\": {\n" +
                "            \"username\": \"admin\",\n" +
                "            \"password\": \"openstack\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JSONObject auth1 = null;
        try {
            auth1 = new JSONObject(a);

            Log.i("TAG", "passing your data" + auth1.toString());

            StringEntity auth = new StringEntity(auth1.toString());
            auth.setContentEncoding("UTF-8");
            auth.setContentType("application/json");

            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(auth);

            // Execute HTTP Post Request
            HttpResponse response = client.execute(httppost);
            Log.i("TAG", "Server response is " + response.getEntity());
            Log.i("TAG", "Server response is " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());// Server response cannont be of type JSONObject

        //Check if response from http request is accepted and start menu activity

            response = client.execute(httppost);
            r =  response.getStatusLine().getStatusCode();
            setResponseCode(""+r);
            HttpEntity entity = response.getEntity();
            //passing response to token String
            //token = EntityUtils.toString(entity);
            Log.i("TAG", "Token String value " + token);

            //Passing response to getToken method to extract the token ID
            String tokenResponse= getToken(EntityUtils.toString(entity));//can only "consume" entity content once, will get error "Caused by: java.lang.IllegalStateException: Content has been consumed"
            tokenId = tokenResponse;
            tenantId = tokenResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Log.i("TAG", "Token ID String value(Login Method) " + tokenId);
            client.close();
            }

        return code = ""+r;
    }
    //Method to extract the token ID
    public static String getToken(String t){
        JSONObject tokenId = null;
        String id = null;
        try {
            tokenId = new JSONObject(t);
            //Getting the id string from the JSONObject and assigning it to the String id
            id = tokenId.getJSONObject("access").getJSONObject("token").getString("id").toString();
            Log.i("TAG", "Token ID String value(getToken Method) " + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static String getTenantId(String token){
        JSONObject tokenId = null;
        String id = null;
        try {
            tokenId = new JSONObject(token);
            //Getting the id string from the JSONObject and assigning it to the String id
            id = tokenId.getJSONObject("access").getJSONObject("token").getJSONObject("tenant").getString("id").toString();
            Log.i("TAG", "Tenat ID  " + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Methods to handle response code from the server
     * Respone code 200 means everything went OK
     * If response code does not equals 200 the login activity cannot call the menu activity.
     */
    public static void setResponseCode(String response){
        responseCode = response;
    }

    public static String getResponseCode(){
        return responseCode;
    }

}
