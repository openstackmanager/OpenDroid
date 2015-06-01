package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    static String responseCode;
    List<MyTask> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();

        final Button login = (Button) findViewById(R.id.button1);
        final TextView endpoint = (TextView) findViewById(R.id.editText);
        final TextView tenant = (TextView) findViewById(R.id.editText2);
        final TextView username = (TextView) findViewById(R.id.editText3);
        final TextView password = (TextView) findViewById(R.id.editText4);



        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String endpointString = endpoint.getText().toString();
                final String tenantString = tenant.getText().toString();
                final String usernameString = username.getText().toString();
                final String passwordString = password.getText().toString();
                login("http://"+endpointString+":5000/v2.0/tokens", endpointString,tenantString,usernameString,passwordString);
            }
        });
        tasks = new ArrayList<>();
    }

    private void loadMenu() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    private void login(String uri, String... params) {
        if(isOnline()) {
            MyTask task = new MyTask();
            task.execute(uri, params[0],params[1],params[2],params[3]);
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
        ProgressDialog progress = null;
        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");

            if (tasks.size() == 0) {
                progress = ProgressDialog.show(MainActivity.this, "Loading", "", true, true);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.login(params[0],params[1],params[2],params[3],params[4]);
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
                progress.dismiss();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }

    }

}
