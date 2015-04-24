package opendroid.nox.opendroid;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by NOX on 16/04/2015.
 */
public class FragmentVolumes extends ListFragment{
    public static FragmentVolumes newInstance(){
        FragmentVolumes fragment = new FragmentVolumes();
        return fragment;
    }

    public FragmentVolumes(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_volumes, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((HomeScreen) activity).onSectionAttached(3);
    }
}
