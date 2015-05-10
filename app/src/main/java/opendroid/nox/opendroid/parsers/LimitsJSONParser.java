package opendroid.nox.opendroid.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.Instances;
import opendroid.nox.opendroid.model.Limits;

/**
 * Created by Brian on 24/04/2015.
 */
public class LimitsJSONParser {

    public static Limits parseFeed(String contentString) {

        Limits limit = new Limits();
        //Pass the content string into a JSONObject
        JSONObject content = null;
        try {
            content = new JSONObject(contentString);

            limit.setMaxTotalCores(content.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalCores"));
            limit.setTotalCoresUsed(content.getJSONObject("limits").getJSONObject("absolute").getString("totalCoresUsed"));
            Log.i("TAG", "parser" + limit.getTotalCoresUsed());
            limit.setMaxTotalRAMSize(content.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalRAMSize"));
            limit.setTotalRAMUsed(content.getJSONObject("limits").getJSONObject("absolute").getString("totalRAMUsed"));

            limit.setMaxTotalInstances(content.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalInstances"));
            limit.setTotalInstancesUsed(content.getJSONObject("limits").getJSONObject("absolute").getString("totalInstancesUsed"));

            limit.setMaxTotalFloatingIps(content.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalFloatingIps"));
            limit.setTotalFloatingIpsUsed(content.getJSONObject("limits").getJSONObject("absolute").getString("totalFloatingIpsUsed"));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        //List<Limits> limitsList = new ArrayList<>();

        return limit;
    }
}
