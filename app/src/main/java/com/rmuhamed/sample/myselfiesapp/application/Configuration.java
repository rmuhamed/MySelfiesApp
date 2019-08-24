package com.rmuhamed.sample.myselfiesapp.application;

import android.content.Context;

import androidx.room.Room;

import com.rmuhamed.sample.myselfiesapp.api.ImgurAPI;
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController;
import com.rmuhamed.sample.myselfiesapp.db.MySelfiesDatabase;

import static com.rmuhamed.sample.myselfiesapp.BuildConfig.DB_NAME;

public final class Configuration {

    private ImgurAPI apiDataSource;
    private MySelfiesDatabase dbDataSource;

    public Configuration(Context applicationContext) {
        setupNetworkDataSource();
        setupStorageDataSource(applicationContext, DB_NAME);
    }

    private void setupNetworkDataSource() {
        this.apiDataSource = RetrofitController.INSTANCE.getImgurAPI();
    }

    private void setupStorageDataSource(Context context, String dbName) {
        this.dbDataSource = Room.databaseBuilder(context, MySelfiesDatabase.class, dbName).build();
    }

    public ImgurAPI getApiDataSource() {
        return apiDataSource;
    }

    public MySelfiesDatabase getDBDataSource() {
        return dbDataSource;
    }
}
