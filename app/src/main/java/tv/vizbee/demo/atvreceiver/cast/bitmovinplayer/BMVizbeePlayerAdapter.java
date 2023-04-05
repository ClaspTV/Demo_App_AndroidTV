package tv.vizbee.demo.atvreceiver.cast.bitmovinplayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bitmovin.player.api.Player;

import java.lang.ref.WeakReference;

import tv.vizbee.screen.api.Vizbee;
import tv.vizbee.screen.api.adapter.VizbeePlayerAdapter;
import tv.vizbee.screen.api.messages.VideoTrackInfo;
import tv.vizbee.screen.api.VizbeeStatusCodes;
import tv.vizbee.screen.api.messages.PlaybackStatus;
import tv.vizbee.screen.api.messages.VideoStatus;

/**
 * Use this VizbeePlayerAdapter implementation if your app uses Bitmovin player and doesn't manage
 * media session.
 */
public class BMVizbeePlayerAdapter extends VizbeePlayerAdapter {

    private static final String LOG_TAG = "BMVizbeePlayerAdapter";

    private Player bmPlayer;
    private final WeakReference<Context> mContext;
    private VideoStatus videoStatus;
    private boolean mVideoStarted;

    //---
    // Constructor
    //---

    public BMVizbeePlayerAdapter(Player player) {
        this(player, null);
    }

    public BMVizbeePlayerAdapter(Player player, Context context) {
        super();

        bmPlayer = player;
        mContext = new WeakReference<>(context);
        videoStatus = new VideoStatus();
    }

    //---
    // Playback Commands
    //---

    @Override
    public void play() {
        bmPlayer.play();
    }

    @Override
    public void pause() {
        bmPlayer.pause();
    }

    @Override
    public void seek(long positionMs) {
        bmPlayer.seek(positionMs/1000);
    }

    @Override
    public void skipNext() {
        super.skipNext();
    }

    @Override
    public void skipPrev() {
        super.skipPrev();
    }

    /**
     * Callback received when Mobile sends a stop command.
     * It's important to reset the app state such as exiting the player fragment and release player
     * here.
     * @param statusCode a status code just to know if the stop command is an explicit one
     *                   initiated by the user or it is triggered from a switch video handling.
     */
    @Override
    public void stop(int statusCode) {

        Log.i(LOG_TAG, "Stop called: statusCode = " + statusCode);

        bmPlayer.unload();
        Vizbee.getInstance().resetPlayerAdapter();
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
            Log.i(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.BUFFERING);
            videoStatus.mPlaybackStatus = PlaybackStatus.BUFFERING;

        } else if ((bmPlayer.isPlaying())) {
            Log.i(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.PLAYING);
            videoStatus.mPlaybackStatus = PlaybackStatus.PLAYING;
            mVideoStarted = true;

        } else if (bmPlayer.isPaused()){

            // IMPORTANT:
            // downstream logic will change this to better state
            if (mVideoStarted) {
                Log.i(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.PAUSED_BY_USER);
                videoStatus.mPlaybackStatus = PlaybackStatus.PAUSED_BY_USER;

            } else {
                Log.i(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.LOADING);
                videoStatus.mPlaybackStatus = PlaybackStatus.LOADING;
            }

        }
//        else {
//            // videoStatus.mPlaybackStatus = PlaybackStatus.FINISHED;
//            Log.i(LOG_TAG, "PlaybackStatus = " + PlaybackStatus.UNKNOWN);
//            videoStatus.mPlaybackStatus = PlaybackStatus.UNKNOWN;
//        }

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
