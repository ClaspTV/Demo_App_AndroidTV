package tv.vizbee.demo.atvreceiver.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class VideoCatalogLoader extends AsyncTaskLoader<List<Video>> {

   private static final String TAG = VideoCatalogLoader.class.getSimpleName();

   public VideoCatalogLoader(Context context, String url) {
      super(context);
   }
   private List<Video> videos = null;

   @Override
   public List<Video> loadInBackground() {
      try {
         if (null == videos) {
            videos = VideoCatalog.allVideos();
         }
         return videos;
      } catch (Exception e) {
         Log.e(TAG, "Failed to fetch media data", e);
         return null;
      }
   }

   @Override
   protected void onStartLoading() {
      super.onStartLoading();
      forceLoad();
   }

   @Override
   protected void onStopLoading() {
      cancelLoad();
   }
}