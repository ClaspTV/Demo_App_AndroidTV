package tv.vizbee.demo.atvreceiver.cast.applifecycle;

import android.app.Activity;

/**
 * NOTE: This is not a mandatory file for the Vizbee integration. If your app has another means to
 * pass the required models/references to AppAdapter that can be used (1) to handle deep linking for
 * start video request and (2) to process sign in request, this need not be included.
 *
 * An instance of this class that encapsulates the references needed for other components
 * (AppAdapter) to process something (start video/ sign in requests).
 */
public class SampleAppReadyModel {

    /**
     * In this Demo app, the activity reference is needed to process the start video request.
     *
     * You can replace this with any object such as an app view model or page navigation component
     * to handle the deep link coming from start video request via AppAdapter::onStart().
     * You can maintain references to multiple objects and pass it.
     */
    public Activity activity;
    public SampleAppReadyModel(Activity activity) {
        this.activity = activity;
    }
}