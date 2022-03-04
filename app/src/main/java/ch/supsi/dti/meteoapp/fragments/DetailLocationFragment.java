package ch.supsi.dti.meteoapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.model.LocationsHolder;
import ch.supsi.dti.meteoapp.model.Location;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mIdTextView;

    public static DetailLocationFragment newInstance(UUID locationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID, locationId);

        DetailLocationFragment fragment = new DetailLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID locationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationsHolder.get(getActivity()).getLocation(locationId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_location, container, false);

        mIdTextView = v.findViewById(R.id.id_textView);
        mIdTextView.setText(mLocation.getName());

        return v;
    }

    // Menu

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_detail_location, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                Toast toast = Toast.makeText(getActivity(),
                        "Add a location",
                        Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}

