package tv.vizbee.demo.atvreceiver.cast.bitmovinplayer;

import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bitmovin.player.api.Player;

import tv.vizbee.screen.api.adapter.MediaSessionCompatPlayerAdapter;
import tv.vizbee.screen.api.messages.PlaybackStatus;
import tv.vizbee.screen.api.messages.VideoStatus;

public class BMMediaSessionCompatPlayerAdapter extends MediaSessionCompatPlayerAdapter {

    private static final String LOG_TAG = BMMediaSessionCompatPlayerAdapter.class.getSimpleName();

    private Player bmPlayer;
    private VideoStatus videoStatus;
    private boolean mVideoStarted;

    public BMMediaSessionCompatPlayerAdapter(Player player, @NonNull MediaSessionCompat mediaSessionCompat) {
        super(mediaSessionCompat);
        this.bmPlayer = player;
        Log.i(LOG_TAG, "Constructor with MediaSessionCompat = " + mediaSessionCompat);
    }

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
}
