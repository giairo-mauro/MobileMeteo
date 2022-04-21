package ch.supsi.dti.meteoapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.activities.DetailActivity;
import ch.supsi.dti.meteoapp.activities.MainActivity;
import ch.supsi.dti.meteoapp.activities.OnDialogResultListener;
import ch.supsi.dti.meteoapp.model.Location;
import ch.supsi.dti.meteoapp.model.LocationsHolder;
import ch.supsi.dti.meteoapp.model.OnTaskCompleted;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchCountry;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchImg;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchTemp;
import ch.supsi.dti.meteoapp.model.weatherFetch.Weather;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListFragment extends Fragment {
    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mLocationRecyclerView = view.findViewById(R.id.recycler_view);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Location> locations = LocationsHolder.get(getActivity()).getLocations();

        Location currentLoc = new Location();
        List<Address> addresses = null;

        if(MainActivity.getCurrentLoc() != null) {
            //Get location info and set as first element title
            try {
                addresses = MainActivity.getGeoLocation().getFromLocation(MainActivity.getCurrentLoc().getLatitude(),
                        MainActivity.getCurrentLoc().getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            assert addresses != null;
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            currentLoc.setCity(city);
            locations.set(0, currentLoc);
        }else {
            locations.set(0, currentLoc);
        }

        mAdapter = new LocationAdapter(locations);
        mLocationRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null) {}
        else
            mAdapter.notifyDataSetChanged();
    }

    // Menu

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                showDialogAndGetResult("Add new location", "Location", "City", ((MainActivity)getActivity()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialogAndGetResult(final String title, final String message, final String initialText, final OnDialogResultListener listener) {

        final EditText editText = new EditText(getActivity());
        editText.setText(initialText);

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onDialogResult(editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setView(editText)
                .show();
    }

    // Holder
    private class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnTaskCompleted {
        private final TextView mNameTextView;
        private Location mLocation;
        private CircleImageView mWeatherImage;

        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            Log.i("mccTEST", String.valueOf(R.id.name));
            mNameTextView = itemView.findViewById(R.id.name);
            mWeatherImage = itemView.findViewById(R.id.country_flag);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);
        }

        public void bind(Location location) {
            mLocation = location;
            String text = null;
            //Get location and country
            if(mLocation.getCity() != null) {
                FetchCountry c = new FetchCountry(mLocation.getCity(), LocationHolder.this);
                c.execute();
                FetchTemp t = new FetchTemp(mLocation.getCity(), LocationHolder.this);
                t.execute();
                //Get weather image
                FetchImg i = new FetchImg(mLocation.getCity(), LocationHolder.this);
                i.execute();
                text = mLocation.getCity();
            }else{
                text = "Location nonexistent";
            }
            mNameTextView.setText(text);
        }

        @Override
        public void OnTextGet(String data) {
            mNameTextView.append(", "+ data);
        }

        @Override
        public void OnTempGet(String temperature) {
            mNameTextView.append(", "+ temperature);
        }

        @Override
        public void onImageGet(byte[] img) {
            if(img != null) {
                Bitmap b = BitmapFactory.decodeByteArray(img, 0, img.length);
                mWeatherImage.setImageBitmap(b);
            }
        }
    }

    // Adapter

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private final List<Location> mLocations;

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @NonNull
        @Override
        public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LocationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, int position) {
            Location location = mLocations.get(position);
            holder.bind(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }
    }
}
