package ru.mirea.panin.mireaproject.ui.contacts;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    public static App instance;
    private AppDb database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDb.class, "contacts_db").build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDb getDatabase() {
        return database;
    }
}
