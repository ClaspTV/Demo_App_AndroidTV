package tv.vizbee.demo.atvreceiver.player;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.bitmovin.player.PlayerView;
import com.bitmovin.player.api.PlaybackConfig;
import com.bitmovin.player.api.Player;
import com.bitmovin.player.api.PlayerConfig;
import com.bitmovin.player.api.event.EventListener;
import com.bitmovin.player.api.event.PlayerEvent;
import com.bitmovin.player.api.event.SourceEvent;
import com.bitmovin.player.api.source.SourceConfig;
import com.bitmovin.player.api.source.SourceType;
import com.bitmovin.player.api.ui.StyleConfig;

import tv.vizbee.screen.api.Vizbee;
import tv.vizbee.screen.api.adapter.VizbeePlayerAdapter;
import tv.vizbee.screen.api.messages.VideoInfo;
import tv.vizbee.demo.atvreceiver.R;
import tv.vizbee.demo.atvreceiver.cast.bitmovinplayer.BMMediaSessionCompatPlayerAdapter;
import tv.vizbee.demo.atvreceiver.cast.bitmovinplayer.BMVizbeePlayerAdapter;
import tv.vizbee.demo.atvreceiver.data.Video;
import tv.vizbee.demo.atvreceiver.player.mediasession.IMediaSessionCallback;
import tv.vizbee.demo.atvreceiver.player.mediasession.IMediaSessionManager;
import tv.vizbee.demo.atvreceiver.player.mediasession.MediaSessionManagerFactory;
import tv.vizbee.demo.atvreceiver.ui.VideoPlayerFragment;

public class BitMovinPlayerFragment extends VideoPlayerFragment {

    private static final String LOG_TAG = BitMovinPlayerFragment.class.getSimpleName();

    private static final int SEEKING_OFFSET = 10;

    // Player

    private PlayerView playerView;
    private Player bmPlayer;

    // Player Adapter

    enum PlayerAdapterChoice {
        SIMPLE_VIZBEE_PLAYER_ADAPTER,
        MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION_COMPAT,
        MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION
    }
    private PlayerAdapterChoice playerAdapterChoice;

    // Media Session
    private IMediaSessionManager mediaSessionManager;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        getActivity().setContentView(R.layout.bitmovin_playerview);
        super.onCreate(saveInstanceState);
    }

    public void initializePlayer() {

        // initialize BitmovinPlayerView from layout
        playerView = getActivity().findViewById(R.id.bitmovin_player_view);
        bmPlayer = Player.create(requireContext(), createPlayerConfig());
        playerView.setPlayer(bmPlayer);

        playerAdapterChoice = PlayerAdapterChoice.MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION;
        createMediaSessionBasedOnPlayerAdapterChoice();
    }

    private void createMediaSessionBasedOnPlayerAdapterChoice() {
        switch (playerAdapterChoice) {
            case MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION_COMPAT:
                mediaSessionManager = MediaSessionManagerFactory.create(bmPlayer, mediaSessionCallback, getContext(), true);
                mediaSessionManager.create();
                break;
            case MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION:
                mediaSessionManager = MediaSessionManagerFactory.create(bmPlayer, mediaSessionCallback, getContext(), false);
                mediaSessionManager.create();
                break;
            case SIMPLE_VIZBEE_PLAYER_ADAPTER:
            default:
                break;
        }
    }

    private IMediaSessionCallback mediaSessionCallback = new IMediaSessionCallback() {
        @Override
        public void onPlay() {
            Log.i(LOG_TAG, "onPlay");
            bmPlayer.play();
            mediaSessionManager.updatePlaybackState();
        }

        @Override
        public void onPause() {
            Log.i(LOG_TAG, "onPause");
            bmPlayer.pause();
            mediaSessionManager.updatePlaybackState();
        }

        @Override
        public void onSeekTo(long pos) {
            Log.i(LOG_TAG, "onSeekTo " + (pos/1000));
            bmPlayer.seek(pos*1F/1000);
            mediaSessionManager.updatePlaybackState();
        }

        @Override
        public void onStop() {
            Log.i(LOG_TAG, "onStop");
            stopPlayback();
        }
    };

    private PlayerConfig createPlayerConfig() {

        // creating a new PlayerConfig
        PlayerConfig playerConfig = new PlayerConfig();

        // Here a custom bitmovinplayer-ui.js is loaded which utilizes the cast-UI as this matches our needs here perfectly.
        // I.e. UI controls get shown / hidden whenever the Player API is called. This is needed due to the fact that on Android TV no touch events are received
        StyleConfig styleConfig = new StyleConfig();
        styleConfig.setPlayerUiJs("file:///android_asset/bitmovinplayer-ui.js");
        playerConfig.setStyleConfig(styleConfig);

        PlaybackConfig playbackConfig = new PlaybackConfig();
        playbackConfig.setAutoplayEnabled(true);
        playerConfig.setPlaybackConfig(playbackConfig);

        return playerConfig;
    }

    public void startPlayback(Video video, long startPosition) {

        Log.i(LOG_TAG, "startPlayback");

        // Create a new SourceItem. In this case we are loading a DASH source.
        SourceConfig sourceConfig = new SourceConfig(video.getVideoURL(), SourceType.Hls);

        bmPlayer.load(sourceConfig);
        bmPlayer.seek(startPosition*1F/1000);
        if (null != mediaSessionManager) {
            mediaSessionManager.updateMetadata(video);
        }

        // ---------------------------
        // [BEGIN] Vizbee Integration
        // ---------------------------

        setVizbeePlayerAdapterBasedOnAdapterChoice(video);

        // ---------------------------
        // [END] Vizbee Integration
        // ---------------------------
    }

    private void setVizbeePlayerAdapterBasedOnAdapterChoice(Video video) {

        VizbeePlayerAdapter vizbeePlayerAdapter = null;
        switch (playerAdapterChoice) {
            case MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION_COMPAT:
                Log.d(LOG_TAG, "1. mediaSessionManager.getMediaSessionObject() = " + mediaSessionManager.getMediaSessionObject());
                MediaSessionCompat mediaSessionCompat = (MediaSessionCompat) mediaSessionManager.getMediaSessionObject();
                vizbeePlayerAdapter = new BMMediaSessionCompatPlayerAdapter(bmPlayer, mediaSessionCompat);
                break;
            case MEDIA_SESSION_COMPAT_PLAYER_ADAPTER_WITH_MEDIA_SESSION:
                Log.d(LOG_TAG, "2. mediaSessionManager.getMediaSessionObject() = " + mediaSessionManager.getMediaSessionObject());
                MediaSession mediaSessionObject = (MediaSession) mediaSessionManager.getMediaSessionObject();
                vizbeePlayerAdapter = new BMMediaSessionCompatPlayerAdapter(bmPlayer, MediaSessionCompat.fromMediaSession(getContext(), mediaSessionObject));
                break;
            case SIMPLE_VIZBEE_PLAYER_ADAPTER:
                vizbeePlayerAdapter = new BMVizbeePlayerAdapter(bmPlayer, getActivity());
                break;
        }
        VideoInfo videoInfo = new VideoInfo(video.getGuid(), video.getTitle(), video.getSubtitle(),
                video.getImageURL(), video.getVideoURL(), video.isLive());
        Vizbee.getInstance().setPlayerAdapter(videoInfo, vizbeePlayerAdapter);
    }

    public void releasePlayer() {

        Log.i(LOG_TAG, "releasePlayer");

        if (null != bmPlayer) {

            Log.v(LOG_TAG, "releasePlayer");

            bmPlayer.unload();
            bmPlayer = null;
            playerView = null;
        }
        if (null != mediaSessionManager) {
            mediaSessionManager.destroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "frag: onResume");
        playerView.onResume();
        addEventListener();
        bmPlayer.play();
        if (null != mediaSessionManager) {
            mediaSessionManager.updatePlaybackState();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "frag: onStart");
        playerView.onStart();
    }

    @Override
    public void onPause() {
        removeEventListener();
        playerView.onPause();
        super.onPause();
        Log.d(LOG_TAG, "frag: onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "frag: onStop");
        if (null != playerView) {
            playerView.onStop();
        }
        stopPlayback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "frag: onDestroy");
        if (null != playerView) {
            playerView.onDestroy();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {

        // This method is called on key down and key up, so avoid being called twice
        if (playerView != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (handleUserInput(event.getKeyCode())) {
                return true;
            }
        }
        return false;
    }

    private boolean handleUserInput(int keycode) {

        Log.d(LOG_TAG, "Keycode " + keycode);

        switch (keycode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                togglePlay();
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                bmPlayer.play();
                if (null != mediaSessionManager) {
                    mediaSessionManager.updatePlaybackState();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                bmPlayer.pause();
                if (null != mediaSessionManager) {
                    mediaSessionManager.updatePlaybackState();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
            case KeyEvent.KEYCODE_BACK:
                stopPlayback();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                seekForward();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                seekBackward();
                break;
            default:
        }

        return false;
    }

    private void togglePlay() {
        if (bmPlayer.isPlaying()) {
            bmPlayer.pause();
        } else {
            bmPlayer.play();
        }
        if (null != mediaSessionManager) {
            mediaSessionManager.updatePlaybackState();
        }
    }

    private void stopPlayback() {
        bmPlayer.unload();
        if (null != mediaSessionManager) {
            mediaSessionManager.updatePlaybackState();
        }
        Log.i(LOG_TAG, "stopPlayback - Resetting Vizbee player adapter");
        Vizbee.getInstance().resetPlayerAdapter();
        getActivity().finish();
    }

    private void seekForward() {
        bmPlayer.seek(bmPlayer.getCurrentTime() + SEEKING_OFFSET);
    }

    private void seekBackward() {
        bmPlayer.seek(bmPlayer.getCurrentTime() - SEEKING_OFFSET);
    }

    private void addEventListener() {
        if (bmPlayer == null) return;

        bmPlayer.on(PlayerEvent.Error.class, onPlayerError);
        bmPlayer.on(SourceEvent.Error.class, onSourceError);
//        bmPlayer.on(PlayerEvent.TimeChanged.class, onTimeChanged);
    }

    private void removeEventListener() {
        if (bmPlayer == null) return;

        bmPlayer.off(onPlayerError);
        bmPlayer.off(onSourceError);
    }

    private final EventListener<PlayerEvent.Error> onPlayerError = errorEvent ->
            Log.e(LOG_TAG, "A player error occurred (" + errorEvent.getCode() + "): " + errorEvent.getMessage());

    private final EventListener<SourceEvent.Error> onSourceError = errorEvent ->
            Log.e(LOG_TAG, "A source error occurred (" + errorEvent.getCode() + "): " + errorEvent.getMessage());

    private boolean durationUpdatedToMediaSession = false;
//    private final EventListener<PlayerEvent.TimeChanged> onTimeChanged = timeChanged -> {
////        Log.v(LOG_TAG, "Player position changed time = " + timeChanged.getTime());
//        double duration = bmPlayer.getDuration();
//        if (!durationUpdatedToMediaSession && duration > 0) {
//            durationUpdatedToMediaSession = true;
//            mediaSessionManager.updateMetadata();
//        }
//        mediaSessionManager.updatePlaybackState();
//    };
}
