package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by NOX on 16/04/2015.
 */
public class FragmentOverview extends ListFragment{

    public static FragmentOverview newInstance(){
        FragmentOverview fragment = new FragmentOverview();
        return fragment;
    }

    public FragmentOverview(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar2);
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((HomeScreen) activity).onSectionAttached(1);
    }
}
