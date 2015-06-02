package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import opendroid.nox.opendroid.model.InstanceDetail;
import opendroid.nox.opendroid.model.InstanceDiagnosticsModel;
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
    static BarChart chart;
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
        //diagnostics("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId+"/diagnostics");
        name = (TextView)findViewById(R.id.InstanceDetailNameT_V);
        status = (TextView)findViewById(R.id.InstanceStatus);
        addressIPv4 = (TextView)findViewById(R.id.textViewIPv4);
        dateCreated = (TextView)findViewById(R.id.textViewDateCreated);
        image = (TextView)findViewById(R.id.textViewImage);
        flavor = (TextView)findViewById(R.id.textViewFlavor);
        chart = (BarChart)findViewById(R.id.instaceChart);
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

    }

    public static void populateChart(InstanceDiagnosticsModel info){
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Ram");
        labels.add("CPU");

        ArrayList<BarEntry> entries = new ArrayList<>();
        float ramMB, max_ram_MB = 0;
        if(info.getMemory_rss() != null) {
            float ram = Float.parseFloat(info.getMemory_rss());
            ramMB = ram / 1000;

            entries.add(new BarEntry(ramMB, 0));
            entries.add(new BarEntry(8f, 1));
        }
        if(info.getMemory()!=null){
            float max_ram = Float.parseFloat(info.getMemory());
            max_ram_MB = max_ram / 1000;
        }

        BarDataSet dataset = new BarDataSet(entries,"");
        BarData data = new BarData(labels, dataset);
        YAxis yAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        yAxis.setAxisMaxValue(max_ram_MB);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        chart.setPinchZoom(false);
        chart.animateY(2000);
        chart.setDescription("");
        chart.setData(data);
        chart.setDrawBarShadow(true);
        chart.invalidate();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
        }else  if (id == R.id.horizon_url) {
            String horizonWebpage = HttpManager.endPoint;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(horizonWebpage));
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String... params) {
        MyTask task = new MyTask();
        task.execute(params[0]);
    }

    private void getFlavor(String uri){
        FlavorName task = new FlavorName();
        task.execute(uri);
    }

    public static void setFlavorName(String _flavor){
        flavor.setText(_flavor);
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
            InstaceDiagnostics task = new InstaceDiagnostics();
            task.execute("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/servers/" + _instanceId+"/diagnostics");
        }
    }

    public static void updateDiagnostics(InstanceDiagnosticsModel info){
        populateChart(info);
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
