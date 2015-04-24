package opendroid.nox.opendroid;


import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 08/04/2015.
 */
public class TaskRunner extends AsyncTask<String,String,String> {
    //TextView output;
    //ProgressBar pb;
    String status;
    List<TaskRunner> tasks = new ArrayList<>();
    @Override
    protected void onPreExecute() {
        if (tasks.size() == 0) {
            MainActivity.pb.setVisibility(View.VISIBLE);
        }
        tasks.add(this);
    }

    @Override
    protected String doInBackground(String... params) {

        String content = HttpManager.login(params[0],params[1]);

        return content;
    }

    @Override
    protected void onPostExecute(String result) {
        updateDisplay(result);

        tasks.remove(this);
        if (tasks.size() == 0) {
           MainActivity.pb.setVisibility(View.INVISIBLE);
        }
        if(result.equals("200")){

        }
        MainActivity.responseCode = HttpManager.getResponseCode();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        updateDisplay(values[0]);
    }

    protected String updateDisplay(String message) {
        return message + "\n";
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    protected String getStatus(String status) {
        return status;
    }
}
