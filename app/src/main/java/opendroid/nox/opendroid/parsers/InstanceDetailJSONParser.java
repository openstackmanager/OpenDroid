package opendroid.nox.opendroid.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.InstanceDetail;
import opendroid.nox.opendroid.model.Instances;
import opendroid.nox.opendroid.model.Limits;

/**
 * Created by Brian on 26/04/2015.
 */
public class InstanceDetailJSONParser {
    //Parsing query result and returning list
    public static InstanceDetail parseFeed(String contentString) {

        InstanceDetail instance = new InstanceDetail();
        //Pass the content string into a JSONObject
        JSONObject content = null;
        try {
            content = new JSONObject(contentString);

            instance.setName(content.getJSONObject("server").getString("name"));
            instance.setStatus(content.getJSONObject("server").getString("status"));
            JSONObject addressIP4 = new JSONObject(content.getJSONObject("server").getJSONObject("addresses").getJSONArray("private").getString(0).toString());
            instance.setAddressIP4(addressIP4.getString("addr"));
            instance.setImage(content.getJSONObject("server").getJSONObject("image").getString("id"));
            instance.setFlavor(content.getJSONObject("server").getJSONObject("flavor").getString("id"));
            instance.setDateCreated(content.getJSONObject("server").getString("created"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return instance;
    }
}
