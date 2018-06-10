package com.appify.ecbt;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("dVVty0n8MrhMhTusZHskFKJADY2HmG17KWW2TpQ9")
                .clientKey("5qPsWfWYkxDAEto91h7CxDoR3ktG06iqWXQGHaz3")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
