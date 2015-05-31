package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import opendroid.nox.opendroid.model.Limits;
import opendroid.nox.opendroid.parsers.LimitsJSONParser;

/**
 * Created by NOX on 16/04/2015.
 */
public class FragmentOverview extends Fragment {
    List<MyTask> tasks = new ArrayList<>();
    TextView output;
    View rootView;
    Limits limitList;
    String used = "0";
    String totalRam = "0";
    PieChart ramChart,chart3,chart4;

    public static FragmentOverview newInstance(){
        FragmentOverview fragment = new FragmentOverview();
        return fragment;
    }

    public FragmentOverview(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        //output = (TextView) rootView.findViewById(R.id.textView);
        requestData("http://95.44.212.163:8774/v2/1f06575369474710959b62a0cb97b132/limits");
        //BarChart chart = (BarChart) rootView.findViewById(R.id.chart);
        ramChart = (PieChart) rootView.findViewById(R.id.chart2);
        chart3 = (PieChart) rootView.findViewById(R.id.chart3);
        chart4 = (PieChart) rootView.findViewById(R.id.chart4);

//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(4f, 0));
//        entries.add(new BarEntry(8f, 1));
//        entries.add(new BarEntry(6f, 2));
//        entries.add(new BarEntry(12f, 3));
//        entries.add(new BarEntry(18f, 4));
//        entries.add(new BarEntry(9f, 5));
//
//        BarDataSet dataset = new BarDataSet(entries, "# of Calls");
//
//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
//
//        BarData data = new BarData(labels, dataset);
//        chart.setData(data);
//        chart.setDescription("# of times Alice called Bob");
//        chart.animateY(5000);
//        chart.invalidate();



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
        //Populatate charts
        if (limitList != null) {
            chartOne(limitList.getTotalRAMUsed(),limitList.getMaxTotalRAMSize());
            chartTwo(limitList.getTotalCoresUsed(), limitList.getMaxTotalCores());
        }
    }

    public void chartOne(String ramUsed, String totalRam){
        ArrayList<Entry> num = new ArrayList<>();
        String[] items = {"Used","Remaining"};
        int usedram = Integer.parseInt(ramUsed);
        int totalram = Integer.parseInt(totalRam);
        num.add(new Entry(usedram, 1));
        num.add(new Entry(totalram, 2));

        ArrayList<String> itemArray = new ArrayList<>();
        itemArray.add(items[0] + ": " + usedram + " MB");

        int remainingRam= totalram - usedram;

        itemArray.add(items[1] + ": " + remainingRam + " MB");

        PieDataSet pdata = new PieDataSet(num,"RAM");
        pdata.setColors(new int[] { R.color.primaryColor, R.color.primaryColorDark, R.color.primaryColor,R.color.primaryColorDark}, this.getActivity());
        pdata.setSliceSpace(2);
        ramChart.setUsePercentValues(true);
        ramChart.setDescription("ram usage");
        ramChart.setDrawHoleEnabled(true);
        ramChart.setHoleColorTransparent(true);
        ramChart.setHoleRadius(7);
        ramChart.setTransparentCircleRadius(10);
        ramChart.animateXY(3000, 2000);
        ramChart.setRotationEnabled(false);

        ArrayList<Integer> colours = new ArrayList<>();
        for(int c : ColorTemplate.COLORFUL_COLORS) {
            //colours.add(c);
        }

        PieData dataSet = new PieData(itemArray,pdata);

        colours.add(ColorTemplate.getHoloBlue());
        //pdata.setColors(colours);
        ramChart.setData(dataSet);
        Legend legend = ramChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12);

        ramChart.invalidate();
    }

    public void chartTwo(String coresUsed, String totalCores){
        ArrayList<Entry> num = new ArrayList<>();
        String[] items = {"Used","Remaining"};
        int usedcores = Integer.parseInt(coresUsed);
        int totalcores = Integer.parseInt(totalCores);
        num.add(new Entry(usedcores, 1));
        num.add(new Entry(totalcores, 2));

        ArrayList<String> itemArray = new ArrayList<>();

        itemArray.add(items[0]+": "+coresUsed+" Cores");

        int remainingCores = totalcores - usedcores;

        itemArray.add(items[1] +": "+remainingCores+" Cores");

        PieDataSet pdata = new PieDataSet(num,"CORES");
        pdata.setSliceSpace(2);
        chart3.setUsePercentValues(true);
        chart3.setDescription("Core usage");
        chart3.setDrawHoleEnabled(true);
        chart3.setHoleColorTransparent(true);
        chart3.setHoleRadius(7);
        chart3.setTransparentCircleRadius(10);
        chart3.animateXY(3000, 2000);

        ArrayList<Integer> colours = new ArrayList<>();
        for(int c : ColorTemplate.LIBERTY_COLORS)
            colours.add(c);

        PieData dataSet = new PieData(itemArray,pdata);
        colours.add(ColorTemplate.getHoloBlue());
        pdata.setColors(colours);
        Legend legend = chart3.getLegend();
        legend.setEnabled(true);
        chart3.setData(dataSet);
        chart3.invalidate();
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
