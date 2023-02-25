package tv.vizbee.demo.atvreceiver.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.leanback.app.VideoSupportFragment;

import tv.vizbee.demo.atvreceiver.data.Video;
import tv.vizbee.demo.atvreceiver.player.VideoPlayerGlue;

/**
 * Handles video playback with media controls.
 */
@SuppressLint("LongLogTag")
public abstract class VideoPlayerFragment extends VideoSupportFragment {

    private static final String LOG_TAG = "VZBApp::VideoPlayerFragment";

    protected static final int UPDATE_DELAY = 16;

    protected VideoPlayerGlue mPlayerGlue;

    //---
    // LifeCycle
    //---

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        initializePlayer();
    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d(LOG_TAG, "onStart");

        processIntent(getActivity().getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");

        if (mPlayerGlue != null && !mPlayerGlue.isPlaying()) {
            mPlayerGlue.play();
        }
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.d(LOG_TAG, "onPause");

        if (mPlayerGlue != null && mPlayerGlue.isPlaying()) {
            mPlayerGlue.pause();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy");

        releasePlayer();
    }
    @Override
    public void onError(int errorCode, CharSequence errorMessage) {

        Log.e(LOG_TAG, "onError" + errorMessage);
        logAndDisplay(errorMessage.toString());
        requireActivity().finish();
    }

    void processIntent(Intent intent) {

        Log.v(LOG_TAG, "processIntent()");

        if (intent.hasExtra(MainActivity.VIDEO)) {

            // Intent came from MainActivity (User chose an item inside ATV app).
            Video video = (Video) intent.getSerializableExtra(MainActivity.VIDEO);
            long startPosition = intent.hasExtra(MainActivity.POSITION) ? intent.getLongExtra(MainActivity.POSITION, 0L) : 0L;
            startPlayback(video, startPosition);
        }
    }

    public void initializePlayer() {
        // Child class implements
    }

    public void releasePlayer() {
        // Child class implements
    }

    public void startPlayback(Video video, long startPosition) {
        // Child class implements
    }

    private void logAndDisplay(String error) {
        Log.d(LOG_TAG, error);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}