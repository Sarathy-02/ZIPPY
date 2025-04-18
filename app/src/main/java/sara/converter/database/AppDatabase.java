package sara.converter.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import sara.converter.util.Constants;

@Database(entities = {History.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        //noinspection DoubleCheckedLocking
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, Constants.DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract HistoryDao historyDao();
}