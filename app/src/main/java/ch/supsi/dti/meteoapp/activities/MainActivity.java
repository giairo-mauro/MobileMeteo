package ch.supsi.dti.meteoapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements OnDialogResultListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.list_fragment_container);
        if (fragment == null) {
            fragment = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.list_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onDialogResult(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
