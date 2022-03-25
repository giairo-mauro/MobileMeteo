package ch.supsi.dti.meteoapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.activities.DetailActivity;
import ch.supsi.dti.meteoapp.model.LocationsHolder;
import ch.supsi.dti.meteoapp.model.Location;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mIdTextView;
    private ImageView mImageView;

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
        View v = inflater.inflate(R.layout.fragment_single_fragment, container, false);

        mIdTextView = v.findViewById(R.id.location);
        mIdTextView.setText(mLocation.getName());

        mImageView = v.findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.color_expand_weather_01);

        return v;
    }
}

