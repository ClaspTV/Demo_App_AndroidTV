package tv.vizbee.demo.atvreceiver.cast;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import tv.vizbee.screen.api.adapter.VizbeeAppAdapter;
import tv.vizbee.screen.api.messages.CustomEvent;
import tv.vizbee.screen.api.messages.VideoInfo;

import tv.vizbee.demo.atvreceiver.data.Video;
import tv.vizbee.demo.atvreceiver.ui.MainActivity;
import tv.vizbee.demo.atvreceiver.ui.VideoPlayerActivity;

public class AppAdapter extends VizbeeAppAdapter {

    private static final String LOG_TAG = "VZBApp::AppAdapter";
    private MainActivity activity;

    public AppAdapter(MainActivity mainActivity) {
        super();

        this.activity = mainActivity;
    }

    @Override
    public void onStart(VideoInfo videoInfo, long positionMs) {
        super.onStart(videoInfo, positionMs);

        Log.i(LOG_TAG, "onStart " + videoInfo.toShortInlineString() +
                "position " + positionMs);

        Video video = new Video(videoInfo.getGUID(),videoInfo.getTitle(), videoInfo.getSubtitle(),
                videoInfo.getImageURL(), videoInfo.getVideoURL(), videoInfo.isLive());
        Intent i = new Intent(this.activity, VideoPlayerActivity.class)
                .putExtra(MainActivity.VIDEO, video)
                .putExtra(MainActivity.POSITION, positionMs);
        this.activity.startActivity(i);
    }

    @Override
    public void onEvent(@NonNull CustomEvent customEvent) {
        Log.i(LOG_TAG, "Invoked onEvent with event = " + customEvent);

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
                showToast(userLabel);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Exception occurred while processing OnEvent", e);
            }
        }
    }

    private void showToast(String userLabel) {

        // toast
        Log.i(LOG_TAG, "Showing welcome popover for " + userLabel);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(
                this.activity,
                "Welcome " + userLabel,
                duration
        );
        toast.show();
    }
}
