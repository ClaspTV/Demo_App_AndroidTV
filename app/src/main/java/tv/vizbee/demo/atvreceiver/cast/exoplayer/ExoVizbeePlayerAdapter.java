package tv.vizbee.demo.atvreceiver.cast.exoplayer;


import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

import tv.vizbee.demo.atvreceiver.player.VideoPlayerGlue;
import tv.vizbee.screen.api.adapter.VizbeePlayerAdapter;
import tv.vizbee.screen.api.messages.PlaybackStatus;
import tv.vizbee.screen.api.messages.VideoStatus;
import tv.vizbee.screen.api.messages.VideoTrackInfo;

/**
 * Use this VizbeePlayerAdapter implementation if your app uses ExoPlayer and doesn't manage
 * media session.
 *
 * In case your app manages media session,
 * [1] Extend MediaSessionCompatPlayerAdapter
 * [2] Skip implementing play, pause, seek and stop calls as they are handled by Vizbee's
 * MediaSessionCompatPlayerAdapter
 * Refer BMMediaSessionCompatPlayerAdapter for an example.
 */
public class ExoVizbeePlayerAdapter extends VizbeePlayerAdapter {

    private static final String LOG_TAG = "ExoVizbeePlayerAdapter";

    private ExoPlayer exoPlayer;
    private VideoPlayerGlue playerGlue;
    private VideoStatus videoStatus;
    private boolean mVideoStarted;

    public ExoVizbeePlayerAdapter(VideoPlayerGlue playerGlue) {
        this(playerGlue, null);
    }

    //---
    // Constructor
    //---

    public ExoVizbeePlayerAdapter(VideoPlayerGlue videoPlayerGlue, ExoPlayer player) {
        super();

        exoPlayer = player;
        playerGlue = videoPlayerGlue;
        videoStatus = new VideoStatus();
    }

    //---
    // Playback Commands
    //---

    @Override
    public void play() {
        playerGlue.play();
    }

    @Override
    public void pause() {
        playerGlue.pause();
    }

    @Override
    public void seek(long positionMs) {
        playerGlue.seekTo(positionMs);
    }

    @Override
    public void skipNext() {
        super.skipNext();
    }

    @Override
    public void skipPrev() {
        super.skipPrev();
    }

    @Override
    public void stop(int statusCode) {
        Log.i(LOG_TAG, "Stop called: statusCode = " + statusCode);

        exoPlayer.stop();
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
        if (exoPlayer.getPlaybackState() == Player.STATE_BUFFERING) {
            videoStatus.mPlaybackStatus = PlaybackStatus.BUFFERING;

        } else if ((exoPlayer.getPlaybackState() == Player.STATE_READY) && exoPlayer.getPlayWhenReady()) {
            videoStatus.mPlaybackStatus = PlaybackStatus.PLAYING;
            mVideoStarted = true;

        } else if ((exoPlayer.getPlaybackState() == Player.STATE_READY) && !exoPlayer.getPlayWhenReady()){
            // IMPORTANT:
            // downstream logic will change this to better state
            if (mVideoStarted) {
                videoStatus.mPlaybackStatus = PlaybackStatus.PAUSED_BY_USER;

            } else {
                videoStatus.mPlaybackStatus = PlaybackStatus.LOADING;
            }

        } else if (exoPlayer.getPlaybackState() == Player.STATE_ENDED) {
            videoStatus.mPlaybackStatus = PlaybackStatus.FINISHED;
        }

        // set position and duration
        videoStatus.setDuration((int) exoPlayer.getDuration());
        videoStatus.setPosition((int) exoPlayer.getCurrentPosition());

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
