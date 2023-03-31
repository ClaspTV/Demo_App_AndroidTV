package tv.vizbee.demo.atvreceiver;

import android.app.Application;
import android.util.Log;

import tv.vizbee.demo.atvreceiver.cast.VizbeeWrapper;
import tv.vizbee.demo.atvreceiver.cast.applifecycle.SampleAppLifecycleAdapter;
import tv.vizbee.demo.atvreceiver.cast.applifecycle.VizbeeAppLifecycleAdapter;

public class ATVVZBDemoApplication extends Application {

    private static final String LOG_TAG = ATVVZBDemoApplication.class.getSimpleName();

    private VizbeeAppLifecycleAdapter appLifecycleAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(LOG_TAG, "onCreate");
		appLifecycleAdapter = new SampleAppLifecycleAdapter();
		Log.i(LOG_TAG, "Init Vizbee");
        VizbeeWrapper.getInstance().init(ATVVZBDemoApplication.this);
    }

	public VizbeeAppLifecycleAdapter getAppLifecycleAdapter() {
        return appLifecycleAdapter;
    }
}