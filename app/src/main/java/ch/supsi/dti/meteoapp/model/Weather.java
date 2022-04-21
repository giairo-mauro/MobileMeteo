package ch.supsi.dti.meteoapp.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.supsi.dti.meteoapp.activities.MainActivity;
import ch.supsi.dti.meteoapp.model.weatherFetch.FetchTemp;

public class Weather extends Worker implements OnTaskCompleted{
    private Context mContext;
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat manager;

    public Weather(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext = context;
    }

    @SuppressLint("WrongThread")
    @NonNull
    @Override
    public Result doWork() {
        //Create a default message to send if there is no lcation
        mBuilder = new NotificationCompat.Builder(mContext, "default")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Weather warning")
                .setContentText("Temperature not available")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        FetchTemp t;
        if (MainActivity.getCurrentLoc() != null){
            List<Address> addresses = null;
            try {
                addresses = MainActivity.getGeoLocation().getFromLocation(MainActivity.getCurrentLoc().getLatitude(),
                        MainActivity.getCurrentLoc().getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            t = new FetchTemp(addresses.get(0).getLocality(), Weather.this);
            t.execute();
        }else{
            manager = NotificationManagerCompat.from(mContext);
            manager.notify(0, mBuilder.build());
        }
        return Result.success();
    }

    @Override
    public void OnTextGet(String weather) {

    }

    @Override
    public void OnTempGet(String temperature) {
        //Based on the temperature sends a notification
        if(Integer.parseInt(temperature) >= 0 && Integer.parseInt(temperature) <= 30) {
            mBuilder = new NotificationCompat.Builder(mContext, "default")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("Weather warning")
                    .setContentText("normal temperature: " + temperature)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }else{
            mBuilder = new NotificationCompat.Builder(mContext, "default")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("Weather warning")
                    .setContentText("critical temperature: " + temperature)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        manager = NotificationManagerCompat.from(mContext);
        manager.notify(0, mBuilder.build());

    }

    @Override
    public void onImageGet(byte[] img) {

    }
}
