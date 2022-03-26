package ch.supsi.dti.meteoapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
@TypeConverters({DBConverter.class})
public abstract class AppDatabase  extends RoomDatabase {

    private static final String DATABASE_NAME = "location_db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME).build();
        }
        return sInstance;
    }

    public abstract LocationDAO personDao();
}
