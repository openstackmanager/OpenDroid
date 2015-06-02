package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.Instances;
import opendroid.nox.opendroid.parsers.InstanceJSONParser;

/**
 * Created by NOX on 16/04/2015.
 */
public class FragmentInstances extends ListFragment implements AdapterView.OnItemClickListener {

    List<MyTask> tasks;
    List<String> instances = new ArrayList<String>();;
    List<Instances> instanceList;
    View rootView;
    ListView lv;
    String[] instanceId;


    public static FragmentInstances newInstance(){
        return new FragmentInstances();
    }

    public FragmentInstances(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_instances, container, false);

        lv = (ListView) rootView.findViewById(R.id.InstaceListView);

        tasks = new ArrayList<>();

        //Should pass in uri data from login activity, not hardcoded like below
        //String tenantID = HttpManager.tenantId;

        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers");

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), InstanceDetailActivity.class);
        intent.putExtra("Id",instanceId[position]);
        startActivity(intent);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((HomeScreen) activity).onSectionAttached(2);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {
        instanceId = new String[100];
        int count  = 0;
        if (instanceList != null) {
            for (Instances instance : instanceList) {
                /**
                 * Populate the list view with instances
                 */
                instances.add(instance.getName() );
                //Populating the Array of id's
                instanceId[count] = instance.getId();
                count++;
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, instances);

        this.setListAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);
    }


    /**
     *
     protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
         } else {
            return false;
        }
     }
     *
     */


    private class MyTask extends AsyncTask<String, String, String> {
        ProgressDialog progress = null;
        @Override
        protected void onPreExecute() {

            //Make progress bar visible before task is executed and then adds task to the task list
            if (tasks.size() == 0) {
                progress = ProgressDialog.show(getActivity(),"Loading","",true,true);
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
            //Passing result from doInBackgroung to InstanceJSONParser and getting an Instance list back
            instanceList = InstanceJSONParser.parseFeed(result);

            //call updateDisplay to populate listView
            updateDisplay();

            //Remove tasks from list and when list size is back to zero make progressbar invisible
            tasks.remove(this);
            if (tasks.size() == 0) {
                progress.dismiss();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

    }
}
