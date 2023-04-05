package tv.vizbee.demo.atvreceiver.player;

import android.annotation.SuppressLint;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.widget.PlaybackControlsRow;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;

import java.util.ArrayList;
import java.util.List;

import tv.vizbee.screen.api.Vizbee;
import tv.vizbee.screen.api.messages.VideoInfo;
import tv.vizbee.screen.api.messages.VideoTrackInfo;

import tv.vizbee.demo.atvreceiver.cast.exoplayer.ExoVizbeePlayerAdapter;
import tv.vizbee.demo.atvreceiver.data.Video;
import tv.vizbee.demo.atvreceiver.data.VideoCatalog;
import tv.vizbee.demo.atvreceiver.ui.VideoPlayerFragment;

@SuppressLint("LongLogTag")
public class ExoPlayerFragment extends VideoPlayerFragment implements Player.Listener {

    private static final String LOG_TAG = "VZBApp_ExoPlayerFragment";

    // Player
    private ExoPlayer mPlayer;

    // Player Adapter
    private LeanbackPlayerAdapter mPlayerAdapter;
    ExoVizbeePlayerAdapter exoVizbeePlayerAdapter;

    //---
    // Fragment Lifecycle
    //---

    public void onStop() {

        super.onStop();
        Log.v(LOG_TAG, "onStop");
    }

    //---
    // Exo Player
    //---

    public void initializePlayer() {

        if (null == mPlayer) {

            Log.v(LOG_TAG, "initializePlayer");

            mPlayer = new ExoPlayer.Builder(requireContext()).build();
            mPlayer.addListener(new Player.Listener() {
                @Override
                public void onMediaItemTransition(MediaItem mediaItem, int reason) {

                    CharSequence title = "";
                    CharSequence subtitle = "";
                    if (null != mediaItem) {
                        title = mediaItem.mediaMetadata.title;
                        subtitle = mediaItem.mediaMetadata.subtitle;
                    }
                    mPlayerGlue.setTitle(title);
                    mPlayerGlue.setSubtitle(subtitle);
                }

                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    Player.Listener.super.onPlaybackStateChanged(playbackState);

                    Log.v(LOG_TAG, "onPlaybackStateChanged " + playbackState);

                    if (playbackState == PlaybackState.STATE_STOPPED) {
                        getActivity().finish();
                    }
                }
            });

            mPlayerAdapter = new LeanbackPlayerAdapter(requireContext(), mPlayer, UPDATE_DELAY);
            mPlayerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

            VideoSupportFragmentGlueHost glueHost =
                    new VideoSupportFragmentGlueHost(ExoPlayerFragment.this);
            mPlayerGlue = new VideoPlayerGlue(getContext(), mPlayerAdapter, null);
            mPlayerGlue.setHost(glueHost);
            mPlayerGlue.setSeekEnabled(true);
        }
    }

    public void startPlayback(Video video, long startPosition) {

        Log.v(LOG_TAG, "startPlayback");

        // ---------------------------
        // [BEGIN] Vizbee Integration
        // ---------------------------

        VideoInfo videoInfo = new VideoInfo(video.getGuid(), video.getTitle(), video.getSubtitle(),
                video.getImageURL(), video.getVideoURL(), video.isLive());

        // Tracks for closed captions [Optional]
        final VideoTrackInfo englishCaptions = new VideoTrackInfo.Builder(1, VideoTrackInfo.TYPE_TEXT)
                .setContentId("https://assets.epix.com/webvtt/rings.vtt")
                .setContentType("text/vtt")
                .setLanguage("en-US")
                .setName("English")
                .setSubtype(VideoTrackInfo.SUBTYPE_SUBTITLES)
                .build();

        List<VideoTrackInfo> tracks = new ArrayList<VideoTrackInfo>() {{
            add(englishCaptions);
        }};
        videoInfo.setTracks(tracks);

        exoVizbeePlayerAdapter = new ExoVizbeePlayerAdapter(mPlayerGlue, mPlayer);
        Vizbee.getInstance().setPlayerAdapter(videoInfo, exoVizbeePlayerAdapter);

        // ---------------------------
        // [END] Vizbee Integration
        // ---------------------------

        List<MediaItem> mediaItems = new ArrayList<>();

        MediaItem firstMediaItem = new MediaItem.Builder()
                .setUri(video.getVideoURL())
                .setMediaMetadata(
                        new com.google.android.exoplayer2.MediaMetadata.Builder()
                                .setMediaUri(Uri.parse(video.getVideoURL()))
                                .setArtworkUri(Uri.parse(video.getImageURL()))
                                .setTitle(video.getTitle())
                                .setSubtitle(video.getSubtitle())
                                .build()
                ).build();
        mediaItems.add(firstMediaItem);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            VideoCatalog.allVideos().forEach(videoItem -> {
                mediaItems.add(new MediaItem.Builder()
                                .setUri(videoItem.getVideoURL())
                                .setMediaMetadata(
                                        new com.google.android.exoplayer2.MediaMetadata.Builder()
                                                .setMediaUri(Uri.parse(videoItem.getVideoURL()))
                                                .setArtworkUri(Uri.parse(videoItem.getImageURL()))
                                                .setTitle(videoItem.getTitle())
                                                .setSubtitle(video.getSubtitle())
                                                .build()
                                ).build()
                );
            });
        }

        mPlayer.setMediaItems(mediaItems);
        mPlayer.prepare();
        mPlayerGlue.playWhenPrepared();
        mPlayerGlue.seekTo(startPosition);
    }

    public void releasePlayer() {

        if (null != mPlayer) {

            Log.v(LOG_TAG, "releasePlayer");

            mPlayer.release();
            mPlayer = null;
            mPlayerAdapter = null;
        }

        // ---------------------------
        // [BEGIN] Vizbee Integration
        // ---------------------------

        Vizbee.getInstance().resetPlayerAdapter();

        // ---------------------------
        // [END] Vizbee Integration
        // ---------------------------
    }
}
