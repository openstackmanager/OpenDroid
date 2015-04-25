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

    public static Limits parseFeed(String content) {

        Limits limit = new Limits();
        //Pass the content string into a JSONObject
        JSONObject ar = null;
        try {
            ar = new JSONObject(content);

            limit.setMaxTotalCores(ar.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalCores"));
            limit.setTotalCoresUsed(ar.getJSONObject("limits").getJSONObject("absolute").getString("totalCoresUsed"));
            Log.i("TAG", "parser" + limit.getTotalCoresUsed());
            limit.setMaxTotalRAMSize(ar.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalRAMSize"));
            limit.setTotalRAMUsed(ar.getJSONObject("limits").getJSONObject("absolute").getString("totalRAMUsed"));

            limit.setMaxTotalInstances(ar.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalInstances"));
            limit.setTotalInstancesUsed(ar.getJSONObject("limits").getJSONObject("absolute").getString("totalInstancesUsed"));

            limit.setMaxTotalFloatingIps(ar.getJSONObject("limits").getJSONObject("absolute").getString("maxTotalFloatingIps"));
            limit.setTotalFloatingIpsUsed(ar.getJSONObject("limits").getJSONObject("absolute").getString("totalFloatingIpsUsed"));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        //List<Limits> limitsList = new ArrayList<>();

        return limit;
    }
}
