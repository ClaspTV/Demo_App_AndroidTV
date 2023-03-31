package tv.vizbee.demo.atvreceiver.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import tv.vizbee.demo.atvreceiver.ATVVZBDemoApplication;
import tv.vizbee.demo.atvreceiver.R;
import tv.vizbee.demo.atvreceiver.cast.applifecycle.SampleAppReadyModel;
import tv.vizbee.demo.atvreceiver.cast.applifecycle.VizbeeAppLifecycleAdapter;

public class MainActivity extends Activity {

   private static final String LOG_TAG = "VZBApp::MainActivity";

   public static final String VIDEO = "Video";
   public static final String POSITION = "position";

   private SampleAppReadyModel appReadyModel;
   private VizbeeAppLifecycleAdapter appLifecycleAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      Log.v(LOG_TAG, "onCreate " + getIntent());
      setContentView(R.layout.activity_main);

      ATVVZBDemoApplication application = (ATVVZBDemoApplication) getApplication();
      appLifecycleAdapter = application.getAppLifecycleAdapter();
      appReadyModel = new SampleAppReadyModel(this);
      appLifecycleAdapter.setAppReady(appReadyModel);
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

   @Override
   protected void onDestroy() {
      super.onDestroy();
      Log.v(LOG_TAG, "onDestroy");
      appLifecycleAdapter.clearAppReady();
      appReadyModel = null;
   }
}
