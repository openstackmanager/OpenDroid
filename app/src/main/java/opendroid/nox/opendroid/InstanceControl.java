package opendroid.nox.opendroid;

import android.os.AsyncTask;
import android.util.Log;

import opendroid.nox.opendroid.parsers.InstanceDetailJSONParser;

/**
 * Created by Brian on 31/05/2015.
 */
public class InstanceControl {

    public static void pauseInstance(String uri,String action){
        InstanceControl ic = new InstanceControl();
        ic.start(uri,action);
    }

    public static void resumeInstance(String uri, String action){
        InstanceControl ic = new InstanceControl();
        ic.stop(uri,action);
    }

    private void start(String uri, String action){
        MyTask task = new MyTask();
        task.execute(uri,action);
    }

    private void stop(String uri, String action){
        MyTask task = new MyTask();
        task.execute(uri,action);
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HttpManager.instanceAction(params[0],params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Passing result from doInBackgroung to InstanceDetailJSONParser and getting data back
            //instanceInfo = InstanceDetailJSONParser.parseFeed(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }
}
