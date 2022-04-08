package ch.supsi.dti.meteoapp.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class Weather extends Worker{
    private Context mContext;

    public Weather(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "default")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Weather warning")
                .setContentText("Temperature is not normal!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        manager.notify(0, mBuilder.build());

        return Result.success();
    }
}
