package tv.vizbee.demo.atvreceiver.player;

import android.content.Context;
import android.util.Log;

import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;

import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;

import java.util.concurrent.TimeUnit;

public class VideoPlayerGlue extends PlaybackTransportControlGlue<LeanbackPlayerAdapter> {

   private static final String LOG_TAG = VideoPlayerGlue.class.getSimpleName();

   private static final long TEN_SECONDS = TimeUnit.SECONDS.toMillis(10);

   /** Listens for when skip to next and previous actions have been dispatched. */
   public interface OnActionClickedListener {

      /** Skip to the previous item in the queue. */
      void onPrevious();

      /** Skip to the next item in the queue. */
      void onNext();
   }

   private final OnActionClickedListener mActionListener;

   private final PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
   private final PlaybackControlsRow.SkipNextAction mSkipNextAction;
   private final PlaybackControlsRow.FastForwardAction mFastForwardAction;
   private final PlaybackControlsRow.RewindAction mRewindAction;

   public VideoPlayerGlue(
           Context context,
           LeanbackPlayerAdapter playerAdapter,
           OnActionClickedListener actionListener) {
      super(context, playerAdapter);

      mActionListener = actionListener;

      mSkipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(context);
      mSkipNextAction = new PlaybackControlsRow.SkipNextAction(context);
      mFastForwardAction = new PlaybackControlsRow.FastForwardAction(context);
      mRewindAction = new PlaybackControlsRow.RewindAction(context);
   }

   @Override
   protected void onCreatePrimaryActions(ArrayObjectAdapter primaryActionsAdapter) {
      super.onCreatePrimaryActions(primaryActionsAdapter);

      Log.v(LOG_TAG, "onCreatePrimaryActions - SkipPrevious, Rewind, FastForward, SkipNext");
      primaryActionsAdapter.add(mSkipPreviousAction);
      primaryActionsAdapter.add(mRewindAction);
      primaryActionsAdapter.add(mFastForwardAction);
      primaryActionsAdapter.add(mSkipNextAction);
   }

   @Override
   public void onActionClicked(Action action) {

      Log.v(LOG_TAG, "onActionClicked - " + action);

      if (action == mRewindAction) {
         rewind();
      } else if (action == mFastForwardAction) {
         fastForward();
      }else {
         super.onActionClicked(action);
      }
   }

   @Override
   public void play() {
      super.play();

      Log.v(LOG_TAG, "play");
   }

   @Override
   public void pause() {
      super.pause();

      Log.v(LOG_TAG, "pause");
   }

   @Override
   public void onHostStop() {
      super.onHostStop();

      Log.v(LOG_TAG, "onHostStop");
   }

   @Override
   public void next() {

      Log.v(LOG_TAG, "next");
      mActionListener.onNext();
   }

   @Override
   public void previous() {

      Log.v(LOG_TAG, "previous");
      mActionListener.onPrevious();
   }

   /** Skips backwards 10 seconds. */
   public void rewind() {

      Log.v(LOG_TAG, "rewind 10 seconds");
      long newPosition = getCurrentPosition() - TEN_SECONDS;
      newPosition = (newPosition < 0) ? 0 : newPosition;
      getPlayerAdapter().seekTo(newPosition);
   }

   /** Skips forward 10 seconds. */
   public void fastForward() {

      Log.v(LOG_TAG, "fastForward 10 seconds");
      if (getDuration() > -1) {
         long newPosition = getCurrentPosition() + TEN_SECONDS;
         newPosition = Math.min(newPosition, getDuration());
         getPlayerAdapter().seekTo(newPosition);
      }
   }
}
