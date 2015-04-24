package opendroid.nox.opendroid.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.Images;
import opendroid.nox.opendroid.model.Instances;

/**
 * Created by Brian on 21/04/2015.
 */
public class ImageJSONParser {
    public static List<Images> parseFeed(String content) {
        try {
            //Pass the content string into a JSONObject
            JSONObject ar = new JSONObject(content);
            //Pass the server array from the JSONObject into the JSONArray
            JSONArray result = ar.getJSONArray("images");

            List<Images> imageList = new ArrayList<>();

            for (int i=0; i < result.length(); i++) {

                Images image = new Images();
                //pass JSONArray back into JSONObject to allow for data extraction.
                JSONObject server = new JSONObject(result.get(i).toString());
                image.setName(server.getString("name"));
                image.setId(server.getString("id"));

                imageList.add(image);
            }

            return imageList;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
