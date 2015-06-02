package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;
import opendroid.nox.opendroid.model.Limits;
import opendroid.nox.opendroid.parsers.LimitsJSONParser;


public class FragmentOverview extends Fragment {
    List<MyTask> tasks = new ArrayList<>();
    View rootView;
    Limits limitList;
    PieChart instanceChart, ramChart, cpuChart, floatingIpChart;

    public static FragmentOverview newInstance(){
        return new FragmentOverview();
    }

    public FragmentOverview(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/limits");
        ramChart = (PieChart) rootView.findViewById(R.id.ram_chart);
        cpuChart = (PieChart) rootView.findViewById(R.id.cpu_chart);
        instanceChart = (PieChart) rootView.findViewById(R.id.instance_chart);
        floatingIpChart = (PieChart) rootView.findViewById(R.id.floating_ip_chart);

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
        //Populate charts
        if (limitList != null) {
            ramChart(limitList.getTotalRAMUsed(), limitList.getMaxTotalRAMSize());
            cpuChart(limitList.getTotalCoresUsed(), limitList.getMaxTotalCores());
            instanceChart(limitList.getTotalInstancesUsed(), limitList.getMaxTotalInstances());
            floatingIpChart(limitList.getTotalFloatingIpsUsed(), limitList.getMaxTotalFloatingIps());
        }
    }
    public void cpuChart(String coresUsed, String totalCores){
        int usedcores = Integer.parseInt(coresUsed);
        int totalcores = Integer.parseInt(totalCores);
        int remainingcores = totalcores - usedcores;

        ArrayList<Entry> num = new ArrayList<>();
        num.add(new Entry(usedcores, 1));
        num.add(new Entry(remainingcores, 2));

        ArrayList<String> itemArray = new ArrayList<>();
        itemArray.add("Used: " + coresUsed + " Cores");
        itemArray.add("Remaining: " + remainingcores + " Cores");

        PieDataSet pdata = new PieDataSet(num,"");
        pdata.setSliceSpace(2);
        pdata.setDrawValues(false);
        pdata.setColors(ColorTemplate.LIBERTY_COLORS);
        cpuChart.setDescription("");
        cpuChart.setDrawHoleEnabled(true);
        cpuChart.setUsePercentValues(true);
        cpuChart.setHoleColorTransparent(true);
        cpuChart.setHoleRadius(30);
        cpuChart.setTransparentCircleRadius(40);
        cpuChart.animateXY(2000, 1000);
        cpuChart.setRotationEnabled(false);
        cpuChart.setCenterText("CPU");
        cpuChart.setDrawSliceText(true);

        PieData dataSet = new PieData(itemArray,pdata);

        cpuChart.setData(dataSet);
        cpuChart.invalidate();
    }

    public void ramChart(String ramUsed, String totalRam){
        int usedram = Integer.parseInt(ramUsed);
        int totalram = Integer.parseInt(totalRam);
        int remainingram = totalram - usedram;

        ArrayList<Entry> num = new ArrayList<>();
        num.add(new Entry(usedram, 1));
        num.add(new Entry(remainingram, 2));

        ArrayList<String> itemArray = new ArrayList<>();
        itemArray.add("Used: " + usedram + " MB");
        itemArray.add("Remaining: " + remainingram + " MB");

        PieDataSet pdata = new PieDataSet(num,"");
        pdata.setSliceSpace(2);
        pdata.setDrawValues(false);
        pdata.setColors(ColorTemplate.LIBERTY_COLORS);
        ramChart.setDescription("");
        ramChart.setUsePercentValues(true);
        ramChart.setDrawHoleEnabled(true);
        ramChart.setHoleColorTransparent(true);
        ramChart.setHoleRadius(30);
        ramChart.setTransparentCircleRadius(40);
        ramChart.animateXY(5000, 3000);
        ramChart.setRotationEnabled(false);
        ramChart.setCenterText("RAM");
        ramChart.setDrawSliceText(true);

        PieData dataSet = new PieData(itemArray,pdata);

        ramChart.setData(dataSet);
        ramChart.invalidate();
    }

    public void instanceChart(String instancesUsed, String totalInstances){
        int used_instances = Integer.parseInt(instancesUsed);
        int total_instances = Integer.parseInt(totalInstances);
        int remaining_instances = total_instances - used_instances;

        ArrayList<Entry> num = new ArrayList<>();
        num.add(new Entry(used_instances, 1));
        num.add(new Entry(remaining_instances, 2));

        ArrayList<String> itemArray = new ArrayList<>();
        itemArray.add("Used: " + used_instances + " Instances");
        itemArray.add("Remaining: " + remaining_instances + " Instances");

        PieDataSet pdata = new PieDataSet(num,"");
        pdata.setColors(ColorTemplate.LIBERTY_COLORS);
        pdata.setSliceSpace(2);
        pdata.setDrawValues(false);
        instanceChart.setUsePercentValues(true);
        instanceChart.setDescription("");
        instanceChart.setDrawHoleEnabled(true);
        instanceChart.setHoleColorTransparent(true);
        instanceChart.setHoleRadius(30);
        instanceChart.setTransparentCircleRadius(40);
        instanceChart.animateXY(1500, 1000);
        instanceChart.setRotationEnabled(false);
        instanceChart.setCenterText("Instances");
        instanceChart.setDrawSliceText(true);

        PieData dataSet = new PieData(itemArray,pdata);

        instanceChart.setData(dataSet);
        instanceChart.invalidate();
    }
    public void floatingIpChart(String floatingIpUsed, String totalFloatingIps){
        int used_floating_ips = Integer.parseInt(floatingIpUsed);
        int total_floating_ips = Integer.parseInt(totalFloatingIps);
        int remaining_floating_ips = total_floating_ips - used_floating_ips;

        ArrayList<Entry> num = new ArrayList<>();
        num.add(new Entry(used_floating_ips, 1));
        num.add(new Entry(remaining_floating_ips, 2));

        ArrayList<String> itemArray = new ArrayList<>();
        itemArray.add("Used: " + used_floating_ips);
        itemArray.add("Remaining: " + remaining_floating_ips);

        PieDataSet pdata = new PieDataSet(num,"");
        pdata.setColors(ColorTemplate.LIBERTY_COLORS);
        pdata.setSliceSpace(2);
        pdata.setDrawValues(false);
        floatingIpChart.setUsePercentValues(true);
        floatingIpChart.setDescription("");
        floatingIpChart.setDrawHoleEnabled(true);
        floatingIpChart.setHoleColorTransparent(true);
        floatingIpChart.setHoleRadius(30);
        floatingIpChart.setTransparentCircleRadius(40);
        floatingIpChart.animateXY(2500, 1500);
        floatingIpChart.setRotationEnabled(false);
        floatingIpChart.setCenterText("Floating IPs");
        floatingIpChart.setDrawSliceText(true);

        PieData dataSet = new PieData(itemArray,pdata);

        floatingIpChart.setData(dataSet);
        floatingIpChart.invalidate();
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
