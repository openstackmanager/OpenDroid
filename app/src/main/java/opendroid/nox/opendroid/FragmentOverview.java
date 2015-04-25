package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.Instances;
import opendroid.nox.opendroid.model.Limits;
import opendroid.nox.opendroid.parsers.InstanceJSONParser;
import opendroid.nox.opendroid.parsers.LimitsJSONParser;

/**
 * Created by NOX on 16/04/2015.
 */
public class FragmentOverview extends Fragment {
    List<MyTask> tasks = new ArrayList<>();;
    TextView output;
    View rootView;
    Limits limitList;

    public static FragmentOverview newInstance(){
        FragmentOverview fragment = new FragmentOverview();
        return fragment;
    }

    public FragmentOverview(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        output = (TextView) rootView.findViewById(R.id.textView);
        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/limits");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((HomeScreen) activity).onSectionAttached(1);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {

        if (limitList != null) {
            Log.i("TAG", "update display" + limitList.getTotalCoresUsed());
            output.append("Cores Used: "+limitList.getTotalCoresUsed()+" Total Cores: "+limitList.getMaxTotalCores());
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
            //Passing result from doInBackgroung to LimitsJSONParser and getting an limit data back
            limitList = LimitsJSONParser.parseFeed(result);

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
