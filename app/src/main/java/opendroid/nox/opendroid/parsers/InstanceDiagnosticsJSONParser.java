package opendroid.nox.opendroid.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import opendroid.nox.opendroid.model.InstanceDetail;
import opendroid.nox.opendroid.model.InstanceDiagnosticsModel;

/**
 * Created by Brian on 02/06/2015.
 */
public class InstanceDiagnosticsJSONParser {
    public static InstanceDiagnosticsModel parseFeed(String contentString) {

        InstanceDiagnosticsModel instance = new InstanceDiagnosticsModel();
        //Pass the content string into a JSONObject
        JSONObject content = null;
        try {
            content = new JSONObject(contentString);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return instance;
    }
}
