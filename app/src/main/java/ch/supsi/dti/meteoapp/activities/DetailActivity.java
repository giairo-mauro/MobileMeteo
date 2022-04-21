package ch.supsi.dti.meteoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.fragments.DetailLocationFragment;
import ch.supsi.dti.meteoapp.model.Location;
import ch.supsi.dti.meteoapp.model.LocationsHolder;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_LOCATION_ID = "ch.supsi.dti.meteoapp.location_id";

    private ViewPager mViewPager;
    private List<Location> mLocations;

    public static Intent newIntent(Context packageContext, UUID locationId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_LOCATION_ID, locationId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_location);

        UUID locationId = (UUID) getIntent().getSerializableExtra(EXTRA_LOCATION_ID);
        mViewPager = findViewById(R.id.my_view_pager);
        mLocations = LocationsHolder.get().getLocations();

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                UUID l = mLocations.get(position).getId();
                return DetailLocationFragment.newInstance(l);
            }
            @Override
            public int getCount() {
                return mLocations.size();
            }
        });

        for (int i = 0; i < mLocations.size(); i++) {
            if (mLocations.get(i).getId().equals(locationId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
