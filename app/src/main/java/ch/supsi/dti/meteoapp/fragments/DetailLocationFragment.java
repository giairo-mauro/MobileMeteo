package ch.supsi.dti.meteoapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.UUID;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.model.Location;
import ch.supsi.dti.meteoapp.model.LocationsHolder;
import ch.supsi.dti.meteoapp.model.OnTaskCompleted;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchCountry;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchImg;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchTemp;

public class DetailLocationFragment extends Fragment implements OnTaskCompleted {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mIdTextView;
    private TextView mTempTextView;
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
        assert getArguments() != null;
        UUID locationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationsHolder.get().getLocation(locationId);
        //Get location and country
        Log.i("weatherTEST", "LOG:"+ mLocation.getCity());
        if(mLocation.getCity() != null) {
            FetchCountry c = new FetchCountry(mLocation.getCity(), DetailLocationFragment.this);
            c.execute();
            FetchTemp t = new FetchTemp(mLocation.getCity(), DetailLocationFragment.this);
            t.execute();
            //Get weather image
            FetchImg i = new FetchImg(mLocation.getCity(), DetailLocationFragment.this);
            i.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_fragment, container, false);

        mIdTextView = v.findViewById(R.id.location);
        mTempTextView = v.findViewById(R.id.textTemp);
        String text = mLocation.getCity();
        mIdTextView.setText(text);

        mImageView = v.findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.color_expand_weather_01);

        return v;
    }

    @Override
    public void OnTextGet(String weather) {
        mIdTextView.append(", "+ weather);
        Log.i("weatherTEST", "Weather: "+ weather);
    }

    @Override
    public void OnTempGet(String temperature) {
        mTempTextView.setText(temperature);
    }

    @Override
    public void onImageGet(byte[] img) {
        if(img != null) {
            Bitmap b = BitmapFactory.decodeByteArray(img, 0, img.length);
            mImageView.setImageBitmap(b);
        }
    }
}

