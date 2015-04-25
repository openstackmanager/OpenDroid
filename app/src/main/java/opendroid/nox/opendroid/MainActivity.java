package opendroid.nox.opendroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    TextView output;
    static ProgressBar pb;
    static String responseCode;
    List<MyTask> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = (Button) findViewById(R.id.button1);
        final TextView endpoint1 = (TextView) findViewById(R.id.editText);
        final TextView tenant1 = (TextView) findViewById(R.id.editText2);
        final TextView username1 = (TextView) findViewById(R.id.editText3);
        final TextView password1 = (TextView) findViewById(R.id.editText4);

        username1.setText("216fe186a8bc4ee2809fac384dea9fe1");

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login("http://95.44.212.163:5000/v2.0/tokens", "");
            }
        });

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void loadMenu() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    private void login(String uri, String... params) {
        if(isOnline()) {
            MyTask task = new MyTask();
            task.execute(uri, params[0]);
        }else {
            Toast.makeText(this, "No Network Connection...", Toast.LENGTH_LONG).show();
        }
    }

    private void getToken(String... params) {
        MyTask task = new MyTask();
        task.execute(params[0]);
    }

    protected void updateDisplay(String message) {
        //output.append(message + "\n");
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");

            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.login(params[0], params[1]);
            if (content.equals("200")) {
                loadMenu();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            updateDisplay(result);

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }

    }

}
