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
    public static InstanceDetail parseFeed(String content) {

        InstanceDetail instance = new InstanceDetail();
        //Pass the content string into a JSONObject
        JSONObject ar = null;
        try {
            ar = new JSONObject(content);

            instance.setGetData(ar.getString(""));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return instance;
    }
}
