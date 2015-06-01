package opendroid.nox.opendroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class InstanceDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_detail);

        Intent intent = getIntent();
        String _instanceId = intent.getStringExtra("Id");
        Toast.makeText(this, _instanceId, Toast.LENGTH_LONG).show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Instance Detail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
