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

public class BMVizbeePlayerAdapter extends VizbeePlayerAdapter {

    private static final String LOG_TAG = "BMVizbeePlayerAdapter";

    private Player bmPlayer;
    private final WeakReference<Context> mContext;
    private VideoStatus videoStatus;
    private boolean mVideoStarted;

    public BMVizbeePlayerAdapter(Player player) {
        this(player, null);
    }

    public BMVizbeePlayerAdapter(Player player, Context context) {
        super();

        bmPlayer = player;
        mContext = new WeakReference<>(context);
        videoStatus = new VideoStatus();
    }

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

    @Override
    public void stop(int statusCode) {

        Log.i(LOG_TAG, "Stop called: statusCode = " + statusCode);
//        String message = "Stop invoked with status code = " + statusCode + " (" + VizbeeStatusCodes.debugString(statusCode) + ")";
//        displayToast(message);

        bmPlayer.unload();
        Vizbee.getInstance().resetPlayerAdapter();
    }

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

    @Override
    public void onSelectActiveTrackInfo(VideoTrackInfo activeTrackInfo){
    }

    private void displayToast(String message) {
        Context context = mContext.get();
        if (null != context) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
