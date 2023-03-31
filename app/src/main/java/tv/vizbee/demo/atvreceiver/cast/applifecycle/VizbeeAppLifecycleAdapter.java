package tv.vizbee.demo.atvreceiver.cast.applifecycle;

import java.lang.ref.WeakReference;

public interface VizbeeAppLifecycleAdapter {

    interface AppLifecycleListener {
        void onAppReady(WeakReference<Object> appReadyModelWeakRef);
    }

    void addAppLifecycleListener (AppLifecycleListener appLifecycleListener);

    boolean isAppReady();

    void setAppReady(Object appReadyModel);

    void clearAppReady();

    WeakReference<Object> getAppReadyModel();
}