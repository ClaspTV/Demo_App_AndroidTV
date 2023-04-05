package tv.vizbee.demo.atvreceiver.cast.bitmovinplayer;

import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bitmovin.player.api.Player;

import tv.vizbee.screen.api.adapter.MediaSessionCompatPlayerAdapter;
import tv.vizbee.screen.api.messages.PlaybackStatus;
import tv.vizbee.screen.api.messages.VideoStatus;
import tv.vizbee.screen.api.messages.VideoTrackInfo;

/**
 * Use this VizbeePlayerAdapter implementation if your app uses Bitmovin player and also manages
 * media session.
 */
public class BMMediaSessionCompatPlayerAdapter extends MediaSessionCompatPlayerAdapter {

    private static final String LOG_TAG = BMMediaSessionCompatPlayerAdapter.class.getSimpleName();

    private Player bmPlayer;
    private VideoStatus videoStatus;
    private boolean mVideoStarted;

    //---
    // Constructor
    //---

    public BMMediaSessionCompatPlayerAdapter(Player player, @NonNull MediaSessionCompat mediaSessionCompat) {
        super(mediaSessionCompat);
        this.bmPlayer = player;
        Log.i(LOG_TAG, "Constructor with MediaSessionCompat = " + mediaSessionCompat);
    }

    //---
    // Video Status
    //---

    /**
     * Vizbee SDK periodically requests the video status to the current state of the video playback.
     * Implement this to return the current state and also the position and duration of the video.
     *
     * This is an example implementation.
     * Please refer to <a href="https://gist.github.com/vizbee/a0a6a2e806fda77cd1e64df03a4d47f6">The Snippet</a>
     * for more possibilities in the detailed implementation.
     * @return an instance of VideoStatus.
     */
    @Override
    public VideoStatus getVideoStatus() {
        videoStatus = new VideoStatus();

        // set state
        if (bmPlayer.isStalled()) {
            Log.v(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.BUFFERING);
            videoStatus.mPlaybackStatus = PlaybackStatus.BUFFERING;

        } else if ((bmPlayer.isPlaying())) {
            Log.v(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.PLAYING);
            videoStatus.mPlaybackStatus = PlaybackStatus.PLAYING;
            mVideoStarted = true;

        } else if (bmPlayer.isPaused()){

            // IMPORTANT:
            // downstream logic will change this to better state
            if (mVideoStarted) {
                Log.v(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.PAUSED_BY_USER);
                videoStatus.mPlaybackStatus = PlaybackStatus.PAUSED_BY_USER;

            } else {
                Log.v(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.LOADING);
                videoStatus.mPlaybackStatus = PlaybackStatus.LOADING;
            }

        }

        // set position and duration
        videoStatus.setDuration((int) (bmPlayer.getDuration() * 1000));
        videoStatus.setPosition((int) (bmPlayer.getCurrentTime() * 1000));

        return videoStatus;
    }

    //---
    // Closed Captions [Optional]
    //---

    /**
     * Callback received when Mobile selects/unselects closed captions.
     * @param activeTrackInfo the track information of the closed captions.
     */
    @Override
    public void onSelectActiveTrackInfo(VideoTrackInfo activeTrackInfo) {
        Log.i(LOG_TAG, "onSelectActiveTrackInfo " + activeTrackInfo);
        // turn ON/OFF closed captions in your app
    }

    /**
     * Vizbee SDK calls this method to know the status of the current closed captions track.
     * Return the app's track info in VizbeeTrackInfo format.
     * @return an instance of VizbeeTrackInfo.
     */
    @Override
    public VideoTrackInfo getActiveTrackInfo() {

        // uncomment and implement as per your app logic
        /*
        Log.v(LOG_TAG, "getActiveTrackInfo isClosedCaptioning = " + isClosedCaptioning);
        if (!isClosedCaptionsOFF) {
            return null
        } else {
            // Convert app's track info to VideoTrackInfo and return
             return new VideoTrackInfo.Builder(1, VideoTrackInfo.TYPE_TEXT)
                    .setContentId("https://assets.epix.com/webvtt/rings.vtt")
                    .setContentType("text/vtt")
                    .setLanguage("en-US")
                    .setName("English")
                    .setSubtype(VideoTrackInfo.SUBTYPE_SUBTITLES)
                    .build();
        }*/

        return null; // remove this
    }
}
