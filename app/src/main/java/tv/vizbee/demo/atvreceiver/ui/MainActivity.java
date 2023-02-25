package tv.vizbee.demo.atvreceiver.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import tv.vizbee.demo.atvreceiver.R;

import tv.vizbee.demo.atvreceiver.cast.VizbeeWrapper;

public class MainActivity extends Activity {

   private static final String LOG_TAG = "VZBApp::MainActivity";

   public static final String VIDEO = "Video";
   public static final String POSITION = "position";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      Log.v(LOG_TAG, "onCreate " + getIntent());
      setContentView(R.layout.activity_main);

      VizbeeWrapper.getInstance().init(getApplication(), this);
   }

   @Override
   protected void onStart() {
      super.onStart();
      Log.v(LOG_TAG, "onStart");
   }

   @Override
   public void onPause() {
      super.onPause();
      Log.v(LOG_TAG, "onPause");
   }

   @Override
   public void onResume() {
      super.onResume();
      Log.v(LOG_TAG, "onResume");
   }

   @Override
   protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      Log.v(LOG_TAG, "onNewIntent");
   }
}
