package opendroid.nox.opendroid.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.Instances;

/**
 * Created by Brian on 21/04/2015.
 */
public class InstanceJSONParser {
    //Parsing query result and returning list
    public static List<Instances> parseFeed(String contentString) {
        try {
            //Pass the content string into a JSONObject
            JSONObject content = new JSONObject(contentString);
            //Pass the server array from the JSONObject into the JSONArray
            JSONArray result = content.getJSONArray("servers");

            List<Instances> instanceList = new ArrayList<>();

            for (int i=0; i < result.length(); i++) {

                Instances instance = new Instances();
                //pass JSONArray back into JSONObject to allow for data extraction.
                JSONObject server = new JSONObject(result.get(i).toString());
                instance.setName(server.getString("name"));
                instance.setId(server.getString("id"));

                instanceList.add(instance);
            }

            return instanceList;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
