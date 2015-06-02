package opendroid.nox.opendroid.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import opendroid.nox.opendroid.model.Flavors;
import opendroid.nox.opendroid.model.InstanceDetail;

/**
 * Created by Brian on 31/05/2015.
 */
public class FlavorJSONParser {
    //Parsing query result and returning list
    public static Flavors parseFeed(String contentString) {

        Flavors flavor = new Flavors();
        //Pass the content string into a JSONObject
        JSONObject content = null;
        try {
            content = new JSONObject(contentString);
            flavor.setName(content.getJSONObject("flavor").getString("name"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return flavor;
    }
}
