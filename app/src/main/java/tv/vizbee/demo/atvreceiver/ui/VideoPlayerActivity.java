package tv.vizbee.demo.atvreceiver.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.cast.tv.CastReceiverContext;
import com.google.android.gms.cast.tv.media.MediaManager;

import org.json.JSONException;
import org.json.JSONObject;

import tv.vizbee.demo.atvreceiver.cast.VizbeeWrapper;
import tv.vizbee.demo.atvreceiver.player.BitMovinPlayerFragment;
import tv.vizbee.demo.atvreceiver.player.ExoPlayerFragment;

@SuppressLint("LongLogTag")
public class VideoPlayerActivity extends FragmentActivity {

   private static final String LOG_TAG = "VZBApp_VideoPlayerActivity";

   private VideoPlayerFragment videoPlayerFragment;

   @SuppressLint("LongLogTag")
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      Log.d(LOG_TAG, "onCreate");

      // ---------------------------
      // [BEGIN] Vizbee Integration [OPTIONAL]
      // ---------------------------

      // set custom metrics attributes to Vizbee
      JSONObject customAttrs = new JSONObject();
      try {
         customAttrs.put("PLAYER_NAME", "EXOPLAYER");
         VizbeeWrapper.getInstance().getVizbeeOptions().addCustomMetricsAttributes(customAttrs);
      } catch (JSONException e) {
         Log.e(LOG_TAG, "exception while creating custom attributes", e);
      }

      // ---------------------------
      // [BEGIN] Vizbee Integration [OPTIONAL]
      // ---------------------------

      // can switch b/w BitMovinPlayerFragment and ExoPlayerFragment for testing.
      videoPlayerFragment = new ExoPlayerFragment();
      if (null == savedInstanceState) {
         getSupportFragmentManager()
                 .beginTransaction()
                 .replace(android.R.id.content, videoPlayerFragment)
                 .commit();
      }
   }

   /**
    * Handling of the load video intent by passing it to CastReceiverContext's MediaManager is
    * required for the start video (AppAdapter::onStart()) to work.
    * For this Demo app, VideoPlayerActivity is responsible for loading the video.
    * In case your app has a single activity that shows videos and also loads them, you need to
    * handle load video intent in that activity's onStart() and onNewIntent()
    */
   @Override
   protected void onStart() {
      super.onStart();

      // ---------------------------
      // [BEGIN] Vizbee Integration
      // ---------------------------

      Log.d(LOG_TAG, "onStart");
      MediaManager mediaManager = CastReceiverContext.getInstance().getMediaManager();
      // Pass the intent to the SDK. You can also do this in onCreate().
      if (mediaManager.onNewIntent(getIntent())) {
         // If the SDK recognizes the intent, you should early return.
         Log.w(LOG_TAG, "onStart: SDK recognises new intent");
         return;
      }

      // ---------------------------
      // [END] Vizbee Integration
      // ---------------------------

      // if the SDK doesn’t recognize the intent, you can handle the intent with
      // your own logic.
   }

   /**
    * Handling of the load video intent by passing it to CastReceiverContext's MediaManager is
    * required for the start video (AppAdapter::onStart()) to work.
    * For this Demo app, VideoPlayerActivity is responsible for loading the video.
    * In case your app has a single activity that shows videos and also loads them, you need to
    * handle load video intent in that activity's onStart() and onNewIntent()
    */
   @Override
   protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);

      Log.d(LOG_TAG, "onNewIntent");

      // ---------------------------
      // [BEGIN] Vizbee Integration
      // ---------------------------

      MediaManager mediaManager = CastReceiverContext.getInstance().getMediaManager();
      if (mediaManager.onNewIntent(intent)) {
         // if the SDK recognizes the intent, you should early return.
         Log.i(LOG_TAG, "onNewIntent: SDK recognises new intent");
         return;
      }

      // ---------------------------
      // [END] Vizbee Integration
      // ---------------------------

      // if the SDK doesn’t recognize the intent, you can handle the intent with
      // your own logic.
      videoPlayerFragment.processIntent(intent);
   }

   @Override
   public void onResume() {
      super.onResume();
      Log.d(LOG_TAG, "onResume");
   }

   @Override
   public void onPause() {
      super.onPause();
      Log.d(LOG_TAG, "onPause");
   }

   @Override
   protected void onStop() {
      super.onStop();
      Log.d(LOG_TAG, "onStop");
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      Log.d(LOG_TAG, "onDestroy");
   }

   @SuppressLint("RestrictedApi")
   @Override
   public boolean dispatchKeyEvent(KeyEvent event) {

      if (videoPlayerFragment  instanceof BitMovinPlayerFragment) {
         return ((BitMovinPlayerFragment) videoPlayerFragment).dispatchKeyEvent(event);
      }

      // Make sure to return super.dispatchKeyEvent(event) so that any key not handled yet will work as expected
      return super.dispatchKeyEvent(event);
   }

}
