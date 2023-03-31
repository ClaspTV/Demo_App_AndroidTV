package tv.vizbee.demo.atvreceiver.cast.exoplayer;


import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

import tv.vizbee.demo.atvreceiver.player.VideoPlayerGlue;
import tv.vizbee.screen.api.adapter.VizbeePlayerAdapter;
import tv.vizbee.screen.api.messages.PlaybackStatus;
import tv.vizbee.screen.api.messages.VideoStatus;
import tv.vizbee.screen.api.messages.VideoTrackInfo;

public class ExoVizbeePlayerAdapter extends VizbeePlayerAdapter {

    private static final String LOG_TAG = "ExoVizbeePlayerAdapter";

    private ExoPlayer exoPlayer;
    private VideoPlayerGlue playerGlue;
    private VideoStatus videoStatus;
    private boolean mVideoStarted;

    public ExoVizbeePlayerAdapter(VideoPlayerGlue playerGlue) {
        this(playerGlue, null);
    }

    public ExoVizbeePlayerAdapter(VideoPlayerGlue videoPlayerGlue, ExoPlayer player) {
        super();

        exoPlayer = player;
        playerGlue = videoPlayerGlue;
        videoStatus = new VideoStatus();
    }

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

    @Override
    public VideoTrackInfo getActiveTrackInfo() {

        if (null != super.getActiveTrackInfo()) {
            return new VideoTrackInfo.Builder(1, VideoTrackInfo.TYPE_TEXT)
                    .setContentId("https://assets.epix.com/webvtt/rings.vtt")
                    .setContentType("text/vtt")
                    .setLanguage("en-US")
                    .setName("English")
                    .setSubtype(VideoTrackInfo.SUBTYPE_SUBTITLES)
                    .build();
        }
        return null;
    }

    @Override
    public void onSelectActiveTrackInfo(VideoTrackInfo activeTrackInfo) {
    }
}
