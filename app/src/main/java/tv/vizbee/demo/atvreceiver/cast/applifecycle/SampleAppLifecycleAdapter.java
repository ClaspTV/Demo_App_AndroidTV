package tv.vizbee.demo.atvreceiver.cast.applifecycle;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SampleAppLifecycleAdapter implements VizbeeAppLifecycleAdapter {

    private static final String LOG_TAG = "SampleAppLifecycleAdp";

    private WeakReference<Object> appReadyModelWeakRef  = null;
    private ArrayList<VizbeeAppLifecycleAdapter.AppLifecycleListener> appLifecycleListeners
            = new ArrayList<>();

    @Override
    public void addAppLifecycleListener(VizbeeAppLifecycleAdapter.AppLifecycleListener appLifecycleListener) {
        this.appLifecycleListeners.add(appLifecycleListener);
    }

    @Override
    public boolean isAppReady() {
        return (null != appReadyModelWeakRef) && (null != appReadyModelWeakRef.get());
    }

    @Override
    public void setAppReady(Object appReadyModel) {

        // check if it's duplicate app ready before assigning the model
        boolean isDuplicate = isAppReady();
        this.appReadyModelWeakRef = new WeakReference(appReadyModel);

        if (!isDuplicate) {
            Log.i(LOG_TAG, "Broadcasting app ready to the listeners");
            for (AppLifecycleListener listener: appLifecycleListeners) {
                listener.onAppReady(this.appReadyModelWeakRef);
            }
        } else {
            Log.i(LOG_TAG, "Ignore duplicate app ready");
        }
    }

    @Override
    public void clearAppReady() {

        // reset app ready model
        Log.i(LOG_TAG, "Resetting app ready model");
        this.appReadyModelWeakRef = null;
    }

    @Override
    public WeakReference<Object> getAppReadyModel() {
        return appReadyModelWeakRef;
    }
}