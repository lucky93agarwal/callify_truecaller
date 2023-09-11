package com.gpslab.kaun.block;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {BlockedNumber.class}, version = 1)
@TypeConverters({BlockedNumber.class})
public abstract class BlockedNumberDatabase extends RoomDatabase {
    private static final String DB_NAME = "blockednumbers.db";

    private static volatile BlockedNumberDatabase INSTANCE;

    public abstract BlockedNumberDao blockedNumberDao();

    public static BlockedNumberDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BlockedNumberDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            BlockedNumberDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
