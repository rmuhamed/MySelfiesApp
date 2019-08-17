package com.rmuhamed.sample.myselfiesapp.application;

import com.rmuhamed.sample.myselfiesapp.api.ImgurAPI;
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController;

public final class Configuration {

    private ImgurAPI apiDataSource;

    Configuration() {
        setup();
    }

    private void setup() {
        apiDataSource = RetrofitController.INSTANCE.getImgurAPI();
    }

    public ImgurAPI getApiDataSource() {
        return apiDataSource;
    }
}
