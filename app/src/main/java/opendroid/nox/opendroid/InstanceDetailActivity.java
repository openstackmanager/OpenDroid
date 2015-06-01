package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.InstanceDetail;
import opendroid.nox.opendroid.parsers.InstanceDetailJSONParser;

import static java.lang.Thread.sleep;


public class InstanceDetailActivity extends Activity implements AdapterView.OnItemSelectedListener {
    String _instanceId;
    List<MyTask> tasks = new ArrayList<>();
    InstanceDetail instanceInfo;
    TextView name;
    TextView status;
    static TextView flavor;
    TextView addressIPv4;
    TextView dateCreated;
    TextView image;
    Button start;
    Button stop;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_detail);

        Intent intent = getIntent();
        _instanceId = intent.getStringExtra("Id");
        //Toast.makeText(this, _instanceId, Toast.LENGTH_LONG).show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Detail");
        //Needs to be dynamic
        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId);

        name = (TextView)findViewById(R.id.InstanceDetailNameT_V);
        status = (TextView)findViewById(R.id.InstanceStatus);
        addressIPv4 = (TextView)findViewById(R.id.textViewIPv4);
        dateCreated = (TextView)findViewById(R.id.textViewDateCreated);
        image = (TextView)findViewById(R.id.textViewImage);
        flavor = (TextView)findViewById(R.id.textViewFlavor);

        sp = (Spinner)findViewById(R.id.spinner);
        ArrayList list = new ArrayList();
        list.add("Action");
        list.add("Pause");
        list.add("Resume");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);
        sp.setOnItemSelectedListener(this);

        start = (Button)findViewById(R.id.buttonStart);
        stop = (Button)findViewById(R.id.buttonStop);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InstanceControl.pauseInstance("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId + "/action", "pause");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InstanceControl.resumeInstance("http://95.44.212.163:8774/v2.1/1f06575369474710959b62a0cb97b132/servers/" + _instanceId + "/action","unpause");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId);
            }
        });
    }

    public void pause() {
        InstanceControl.pauseInstance("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId + "/action", "pause");
        sp.setSelection(0);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId);

    }

    public void resume(){
        InstanceControl.resumeInstance("http://95.44.212.163:8774/v2.1/1f06575369474710959b62a0cb97b132/servers/" + _instanceId + "/action", "unpause");
        sp.setSelection(0);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instance_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    private void getFlavor(String uri){
        FlavorName task = new FlavorName();
        task.execute(uri);
    }

    public static void setFlavorName(String _flavor){
        flavor.setText(_flavor);
    }
    public void start(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    public void stop(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }
    protected void updateDisplay() {
        //Populatate charts
        if (instanceInfo != null) {
            name.setText(instanceInfo.getName());
            status.setText(instanceInfo.getStatus());
            addressIPv4.setText(instanceInfo.getAddressIP4());
            image.setText(instanceInfo.getImage());
            dateCreated.setText(instanceInfo.getDateCreated());
            String id = instanceInfo.getFlavor().toString();
            getFlavor("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/flavors/" + id);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(sp.getSelectedItemPosition() == 1){
            pause();
        }else if(sp.getSelectedItemPosition() == 2){
            resume();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    private class MyTask extends AsyncTask<String, String, String> {
        ProgressDialog progress = null;
        @Override
        protected void onPreExecute() {

            if (tasks.size() == 0) {
                progress = ProgressDialog.show(InstanceDetailActivity.this,"Loading", "", true, true);
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
            //Passing result from doInBackgroung to InstanceDetailJSONParser and getting data back
            instanceInfo = InstanceDetailJSONParser.parseFeed(result);
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
