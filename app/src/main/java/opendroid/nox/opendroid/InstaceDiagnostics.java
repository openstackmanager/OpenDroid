package opendroid.nox.opendroid;

import android.os.AsyncTask;
import android.util.Log;

import opendroid.nox.opendroid.model.InstanceDiagnosticsModel;
import opendroid.nox.opendroid.parsers.InstanceDetailJSONParser;
import opendroid.nox.opendroid.parsers.InstanceDiagnosticsJSONParser;

/**
 * Created by Brian on 02/06/2015.
 */
public class InstaceDiagnostics extends AsyncTask<String,String,String> {
    InstanceDiagnosticsModel info;

    @Override
    protected String doInBackground(String... params) {
        String content = HttpManager.getData(params[0]);
        Log.i("Tag: content", content);
        return content;
    }

    @Override
    protected void onPostExecute(String result) {
        //Passing result from doInBackgroung to InstanceDetailJSONParser and getting data back
        info = InstanceDiagnosticsJSONParser.parseFeed(result);
        //Remove tasks from list and when list size is back to zero make progressbar invisible

        InstanceDetailActivity.updateDiagnostics(info);

    }
}
