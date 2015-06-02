package opendroid.nox.opendroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import opendroid.nox.opendroid.model.Flavors;
import opendroid.nox.opendroid.parsers.FlavorJSONParser;

/**
 * Created by Brian on 31/05/2015.
 */

     class FlavorName extends AsyncTask<String, String, String> {
        Flavors flavor;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            //Passing result from doInBackgroung to InstanceDetailJSONParser and getting data back
            flavor = FlavorJSONParser.parseFeed(result);
            InstanceDetailActivity.setFlavorName(flavor.getName());
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }
