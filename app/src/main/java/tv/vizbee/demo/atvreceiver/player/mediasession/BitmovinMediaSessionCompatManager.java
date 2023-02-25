package tv.vizbee.demo.atvreceiver.player.mediasession;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.bitmovin.player.api.Player;

import tv.vizbee.demo.atvreceiver.R;
import tv.vizbee.demo.atvreceiver.data.Video;

public class BitmovinMediaSessionCompatManager implements IMediaSessionManager {

    private static final String LOG_TAG = BitmovinMediaSessionCompatManager.class.getSimpleName();
    private PlaybackStateCompat.Builder playbackStateCompatBuilder;

    private final Player bmPlayer;
    private final IMediaSessionCallback mediaSessionCallback;

    private MediaSessionCompat mediaSessionCompat;
    private final Context context;


    public BitmovinMediaSessionCompatManager(Player bmPlayer, IMediaSessionCallback mediaSessionCallback, Context context) {
        this.bmPlayer = bmPlayer;
        this.mediaSessionCallback = mediaSessionCallback;
        this.context = context;
    }

    @Override
    public void create() {

        Log.d(LOG_TAG, "creating the media session");
        mediaSessionCompat = new MediaSessionCompat(context, context.getResources().getResourceName(R.string.app_name));
        mediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
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

            public void onSkipToNext() {
                super.onSkipToNext();
                Log.d(LOG_TAG, "onSkipToNext");
            }

            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                Log.d(LOG_TAG, "onSkipToPrevious");
            }

            public void onFastForward() {
                super.onFastForward();
                Log.d(LOG_TAG, "onFastForward");
            }

            public void onRewind() {
                super.onRewind();
                Log.d(LOG_TAG, "onRewind");
            }

        });
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mediaSessionCompat.setActive(true);
    }

    @Override
    public void updateMetadata(Video video) {
        MediaMetadataCompat metadataCompat = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, video.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, video.getSubtitle())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, video.getImageURL())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, video.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, video.getTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Math.round(bmPlayer.getDuration() * 1000L))
                .build();
        mediaSessionCompat.setMetadata(metadataCompat);
        Log.d(LOG_TAG, "update metadataCompat with " + metadataCompat + " duration = " + bmPlayer.getDuration());
    }

    @Override
    public void updatePlaybackState() {

        int playerState =  (bmPlayer.isPlaying()) ? STATE_PLAYING : STATE_PAUSED;
        long position = (bmPlayer.getCurrentTime() != 0) ? Math.round(bmPlayer.getCurrentTime()) * 1000L : 0L;
        if (null == playbackStateCompatBuilder) {
            playbackStateCompatBuilder = new PlaybackStateCompat.Builder();
        }
        PlaybackStateCompat playbackState = playbackStateCompatBuilder
                .setActions(getAvailableActions())
                .setState(playerState, position, 1)
                .build();
        mediaSessionCompat.setPlaybackState(playbackState);
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
        mediaSessionCompat.setActive(false);
        mediaSessionCompat.release();
    }

    @Override
    public Object getMediaSessionObject() {
        return mediaSessionCompat;
    }
}
