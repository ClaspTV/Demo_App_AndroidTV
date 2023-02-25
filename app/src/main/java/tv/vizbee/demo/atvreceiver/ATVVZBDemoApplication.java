package tv.vizbee.demo.atvreceiver;

import android.app.Application;
import android.util.Log;

public class ATVVZBDemoApplication extends Application {

    private static final String LOG_TAG = ATVVZBDemoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(LOG_TAG, "onCreate");
    }
}