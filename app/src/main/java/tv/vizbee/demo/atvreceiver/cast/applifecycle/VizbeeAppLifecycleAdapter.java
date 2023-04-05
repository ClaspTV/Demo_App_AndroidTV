package tv.vizbee.demo.atvreceiver.cast.applifecycle;

import java.lang.ref.WeakReference;

/**
 * NOTE: This is not a mandatory file for the Vizbee integration. If your app has another means to
 * pass the required models/references to AppAdapter that can be used (1) to handle deep linking for
 * start video request and (2) to process sign in request, this need not be included.
 *
 * Implementations of this interface should know when the app is ready, meaning that the required
 * initialisation of the app is done and all the models are ready.
 */
public interface VizbeeAppLifecycleAdapter {

    /**
     * Implement this interface to get a callback as soon as the app is ready.
     * For Vizbee integration, AppAdapter implements and processes saved start video/sign in requests
     * if any.
     */
    interface AppLifecycleListener {
        void onAppReady(WeakReference<Object> appReadyModelWeakRef);
    }

    /**
     * Add a listener to listen to app ready callbacks.
     * For Vizbee integration, AppAdapter adds a listener.
     * @param appLifecycleListener a listener that implements AppLifecycleListener
     */
    void addAppLifecycleListener (AppLifecycleListener appLifecycleListener);

    /**
     * Queries if the app is ready at that moment
     * @return true if the app is ready, false otherwise
     */
    boolean isAppReady();

    /**
     * Set model when the app has become ready.
     * Typically, when the activity is created and basic initialisation of models are done, call
     * this method with an object that encapsulates the references needed for other components
     * (AppAdapter) to process something (start video/ sign in requests).
     * @param appReadyModel an object that encapsulates the references needed for other components
     */
    void setAppReady(Object appReadyModel);

    /**
     * Clear the app ready reference
     */
    void clearAppReady();

    /**
     * Returns the current app ready model.
     * @return the current app ready model
     */
    WeakReference<Object> getAppReadyModel();
}