package tv.vizbee.demo.atvreceiver.cast;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import tv.vizbee.demo.atvreceiver.cast.applifecycle.SampleAppReadyModel;
import tv.vizbee.demo.atvreceiver.cast.applifecycle.VizbeeAppLifecycleAdapter;
import tv.vizbee.demo.atvreceiver.data.Video;
import tv.vizbee.demo.atvreceiver.ui.MainActivity;
import tv.vizbee.demo.atvreceiver.ui.VideoPlayerActivity;
import tv.vizbee.screen.api.adapter.VizbeeAppAdapter;
import tv.vizbee.screen.api.messages.CustomEvent;
import tv.vizbee.screen.api.messages.VideoInfo;

public class AppAdapter extends VizbeeAppAdapter {

    private static final String LOG_TAG = "VZBApp_AppAdapter";
    private final VizbeeAppLifecycleAdapter appLifecycleAdapter;
    private StartVideoRequest startVideoRequest;
    private CustomEvent customEvent;

    public AppAdapter(VizbeeAppLifecycleAdapter appLifecycleAdapter) {
        super();
        this.appLifecycleAdapter = appLifecycleAdapter;
        appLifecycleAdapter.addAppLifecycleListener(appReadyModelWeakRef -> {

            boolean haveSavedCustomEvent = (null != customEvent);
            boolean haveSavedStartVideoRequest = (null != startVideoRequest);
            Log.i(LOG_TAG, "onAppReady do we have a saved custom event? " + haveSavedCustomEvent +
                    " do we have a saved start video request? = " + haveSavedStartVideoRequest);

            if (haveSavedCustomEvent) {
                onEvent(customEvent);
            }

            if (haveSavedStartVideoRequest) {
                onStart(startVideoRequest.videoInfo, startVideoRequest.position);
            }
        });
    }

    @Override
    public void onStart(VideoInfo videoInfo, long positionMs) {
        super.onStart(videoInfo, positionMs);

        Log.i(LOG_TAG, "onStart " + videoInfo.toShortInlineString() +
                "position " + positionMs);

        if (!appLifecycleAdapter.isAppReady()) {
            Log.i(LOG_TAG, "App is not ready yet. Saving start video.");
            startVideoRequest = new StartVideoRequest(videoInfo, positionMs);
            return;
        }

        WeakReference<Object> appReadyModelWeakRef = appLifecycleAdapter.getAppReadyModel();
        if ((null != appReadyModelWeakRef) && (null != appReadyModelWeakRef.get())){
            SampleAppReadyModel appReadyModel = (SampleAppReadyModel) appReadyModelWeakRef.get();
            Activity activity = appReadyModel.activity;

            if (null != activity) {
                Video video = new Video(videoInfo.getGUID(), videoInfo.getTitle(), videoInfo.getSubtitle(),
                        videoInfo.getImageURL(), videoInfo.getVideoURL(), videoInfo.isLive());
                Intent i = new Intent(activity, VideoPlayerActivity.class)
                        .putExtra(MainActivity.VIDEO, video)
                        .putExtra(MainActivity.POSITION, positionMs);
                activity.startActivity(i);
            }
        }
    }

    @Override
    public void onEvent(@NonNull CustomEvent customEvent) {
        Log.i(LOG_TAG, "Invoked onEvent with event = " + customEvent);

        if (!appLifecycleAdapter.isAppReady()) {
            Log.i(LOG_TAG, "App is not ready yet. Saving custom event.");
            this.customEvent = customEvent;
            return;
        }

        WeakReference<Object> appReadyModelWeakRef = appLifecycleAdapter.getAppReadyModel();
        if ((null != appReadyModelWeakRef) && (null != appReadyModelWeakRef.get())){
            SampleAppReadyModel appReadyModel = (SampleAppReadyModel) appReadyModelWeakRef.get();
            Activity activity = appReadyModel.activity;

            if (null != activity) {

                String eventType = customEvent.getEventType();
                if (null != eventType && eventType.equalsIgnoreCase("tv.vizbee.homesign.signin")) {

                    JSONObject customEventData = customEvent.getEventData();
                    if (null == customEventData) {
                        Log.e(LOG_TAG, "Invoked onEvent with null eventData");
                        return;
                    }
                    JSONObject userAuthInfo = customEventData.optJSONObject("authInfo");
                    if (null == userAuthInfo) {
                        Log.e(LOG_TAG, "Invoked onEvent with null authInfo");
                        return;
                    }

                    try {
                        String userFullName = null;
                        if (userAuthInfo.has("userFullName")) {
                            userFullName = userAuthInfo.getString("userFullName");
                        }
                        String userLogin = null;
                        if (userAuthInfo.has("userLogin")) {
                            userLogin = userAuthInfo.getString("userLogin");
                        }
                        String userLabel = TextUtils.isEmpty(userFullName) ? userLogin : userFullName;
                        showToast(userLabel, activity);
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Exception occurred while processing OnEvent", e);
                    }
                }
            }
        }
    }

    private void showToast(String userLabel, Activity activity) {

        // toast
        Log.i(LOG_TAG, "Showing welcome popover for " + userLabel);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(
                activity,
                "Welcome " + userLabel,
                duration
        );
        toast.show();
    }
}
