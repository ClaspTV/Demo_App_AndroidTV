package tv.vizbee.demo.atvreceiver.cast.applifecycle;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * NOTE: This is not a mandatory file for the Vizbee integration. If your app has another means to
 * pass the required models/references to AppAdapter that can be used (1) to handle deep linking for
 * start video request and (2) to process sign in request, this need not be included.
 *
 * This class implements VizbeeAppLifecycleAdapter and is a self sufficient implementation. No more
 * app specific changes are needed in this class except the name of the class. Use your app name as
 * a prefix to the class name.
 *
 * You need to create an instance of this class in Application class and make it available to other
 * components like MainActivity.
 */
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