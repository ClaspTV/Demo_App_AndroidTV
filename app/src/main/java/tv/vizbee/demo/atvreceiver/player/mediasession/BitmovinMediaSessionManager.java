package tv.vizbee.demo.atvreceiver.player.mediasession;

import static android.media.session.PlaybackState.ACTION_FAST_FORWARD;
import static android.media.session.PlaybackState.ACTION_PAUSE;
import static android.media.session.PlaybackState.ACTION_PLAY;
import static android.media.session.PlaybackState.ACTION_PLAY_FROM_MEDIA_ID;
import static android.media.session.PlaybackState.ACTION_PLAY_FROM_SEARCH;
import static android.media.session.PlaybackState.ACTION_PLAY_PAUSE;
import static android.media.session.PlaybackState.ACTION_REWIND;
import static android.media.session.PlaybackState.ACTION_SEEK_TO;
import static android.media.session.PlaybackState.ACTION_SKIP_TO_NEXT;
import static android.media.session.PlaybackState.ACTION_STOP;
import static android.media.session.PlaybackState.STATE_PAUSED;
import static android.media.session.PlaybackState.STATE_PLAYING;

import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.util.Log;

import com.bitmovin.player.api.Player;

import tv.vizbee.demo.atvreceiver.R;
import tv.vizbee.demo.atvreceiver.data.Video;

public class BitmovinMediaSessionManager implements IMediaSessionManager {
    
    private static final String LOG_TAG = BitmovinMediaSessionManager.class.getSimpleName();
    private PlaybackState.Builder playbackStateBuilder;

    private final Player bmPlayer;
    private final IMediaSessionCallback mediaSessionCallback;

    private MediaSession mediaSession;
    private final Context context;
    

    public BitmovinMediaSessionManager(Player bmPlayer, IMediaSessionCallback mediaSessionCallback, Context context) {
        this.bmPlayer = bmPlayer;
        this.mediaSessionCallback = mediaSessionCallback;
        this.context = context;
    }

    @Override
    public void create() {

        Log.d(LOG_TAG, "creating the media session");
        mediaSession = new MediaSession(context, context.getResources().getResourceName(R.string.app_name));
        mediaSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                mediaSessionCallback.onPlay();
                Log.d(LOG_TAG, "onPlay");
            }

            @Override
            public void onPause() {
                super.onPause();
                mediaSessionCallback.onPause();
                Log.d(LOG_TAG, "onPause");
            }
            
            @Override
            public void onStop() {
                super.onStop();
                mediaSessionCallback.onStop();
                Log.d(LOG_TAG, "onStop");
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
                mediaSessionCallback.onSeekTo(pos);
                Log.d(LOG_TAG, "onSeekTo " + pos);
            }
        });
        mediaSession.setFlags(
                MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mediaSession.setActive(true);
    }

    @Override
    public void updateMetadata(Video video) {
        MediaMetadata metadata = new MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, video.getTitle())
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, video.getSubtitle())
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, video.getImageURL())
                .putString(MediaMetadata.METADATA_KEY_TITLE, video.getTitle())
                .putString(MediaMetadata.METADATA_KEY_ARTIST, video.getTitle())
                .putLong(MediaMetadata.METADATA_KEY_DURATION, Math.round(bmPlayer.getDuration() * 1000L))
                .build();
        mediaSession.setMetadata(metadata);
        Log.d(LOG_TAG, "update metadata with " + metadata + " duration = " + bmPlayer.getDuration());
    }

    @Override
    public void updatePlaybackState() {

        int playerState =  (bmPlayer.isPlaying()) ? STATE_PLAYING : STATE_PAUSED;
        long position = (bmPlayer.getCurrentTime() != 0) ? Math.round(bmPlayer.getCurrentTime()) * 1000L : 0L;
        if (null == playbackStateBuilder) {
            playbackStateBuilder = new PlaybackState.Builder();
        }
        PlaybackState playbackState = playbackStateBuilder
                .setActions(getAvailableActions())
                .setState(playerState, position, 1)
                .build();
        mediaSession.setPlaybackState(playbackState);
        Log.d(LOG_TAG, "update playback state with " + playbackState + " duration = " + bmPlayer.getDuration());
    }

    private long getAvailableActions() {

        long actions = (
                ACTION_PLAY_PAUSE
                        | ACTION_PLAY_FROM_MEDIA_ID
                        | ACTION_PLAY_FROM_SEARCH
                        | ACTION_SEEK_TO
                        | ACTION_REWIND
                        | ACTION_FAST_FORWARD
                        | ACTION_STOP
                        | ACTION_SKIP_TO_NEXT);

        if (bmPlayer.isPlaying()) {
            actions = actions | ACTION_PAUSE;
        } else {
            actions = actions | ACTION_PLAY;
        }
        return actions;
    }

    @Override
    public void destroy() {
        Log.d(LOG_TAG, "release media session");
        mediaSession.setActive(false);
        mediaSession.release();
    }

    @Override
    public Object getMediaSessionObject() {
        return mediaSession;
    }
}
