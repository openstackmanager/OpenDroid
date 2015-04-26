package opendroid.nox.opendroid;

/**
 * Created by Brian on 26/04/2015.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.InstanceDetail;

import opendroid.nox.opendroid.parsers.InstanceDetailJSONParser;




/**
 * Created by NOX on 16/04/2015.
 */
public class FragmentInstanceDetail extends Fragment {
    List<MyTask> tasks = new ArrayList<>();
    TextView output;
    View rootView;
    InstanceDetail instance;
    static String _instanceId;

    public static FragmentInstanceDetail newInstance(String instanceId){
        _instanceId = instanceId;
        FragmentInstanceDetail fragment = new FragmentInstanceDetail();
        return fragment;
    }

    public FragmentInstanceDetail(){}


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_instance_detail, container, false);

        //output = (TextView) rootView.findViewById(R.id.textView);
        //requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/"+_instanceId);
        Toast.makeText(getActivity(),_instanceId,Toast.LENGTH_LONG).show();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((HomeScreen) activity).onSectionAttached(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.horizon_url) {
            String horizonWebpage = HttpManager.endPoint;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(horizonWebpage));
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {

        if (instance != null) {
            Log.i("TAG", "update display" + instance.getGetData());
            output.append(instance.getGetData());
        }
    }

    private class MyTask extends AsyncTask<String, String, String> {
        ProgressDialog progress = null;
        @Override
        protected void onPreExecute() {

            if (tasks.size() == 0) {
                progress = ProgressDialog.show(getActivity(), "Loading", "", true, true);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            //Passing result from doInBackground to InstanceDetailJSONParser and getting data back
            instance = InstanceDetailJSONParser.parseFeed(result);

            //call updateDisplay to populate listView


            //Remove tasks from list and when list size is back to zero make progressbar invisible
            tasks.remove(this);
            if (tasks.size() == 0) {
                progress.dismiss();
            }
            updateDisplay();
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

    }
}
