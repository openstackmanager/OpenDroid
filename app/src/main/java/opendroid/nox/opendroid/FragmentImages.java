package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;


import opendroid.nox.opendroid.model.Images;
import opendroid.nox.opendroid.model.Instances;
import opendroid.nox.opendroid.parsers.ImageJSONParser;

/**
 * Created by Brian on 23/04/2015.
 */
public class FragmentImages extends ListFragment implements AdapterView.OnItemClickListener {

    public FragmentImages(){}

    List<MyTask> tasks;
    List<Images> imageList;
    List<String> images = new ArrayList<String>();;
    View rootView;
    ListView ListView;



    public static FragmentImages newInstance(){
        FragmentImages fragment = new FragmentImages();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_images, container, false);

        ListView = (ListView) rootView.findViewById(R.id.ImageslistView);

        tasks = new ArrayList<>();

        //Should pass in uri data from login activity, not hardcoded like below
        String tenantID = HttpManager.tenantId;

        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/images");

        updateDisplay();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((HomeScreen) activity).onSectionAttached(4);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {

        if (imageList != null) {
            for (Images image : imageList) {
                /**
                 * Populate the list view with images
                 */
                images.add(image.getName()+"\n" );

            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, images);

        this.setListAdapter(arrayAdapter);
        ListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragmentManager = getFragmentManager();

        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentOverview.newInstance())
                    .commit();
        } else if (position == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentInstances.newInstance())
                    .commit();
        } else if (position == 2) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentVolumes.newInstance())
                    .commit();
        } else if (position == 3) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentImages.newInstance())
                    .commit();
        }

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
            //Passing result from doInBackgroung to ImageJSONParser and getting an Imagelist back
            imageList = ImageJSONParser.parseFeed(result);
            if (imageList != null) {
                for (Images image : imageList) {
                    /**
                     * Populate the list view with instances
                     */
                    Log.i("TAG", "result " + image.getName());
                }
            }
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
