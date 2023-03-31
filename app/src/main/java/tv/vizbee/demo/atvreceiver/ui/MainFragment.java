package tv.vizbee.demo.atvreceiver.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.Collections;
import java.util.List;

import tv.vizbee.demo.atvreceiver.R;
import tv.vizbee.demo.atvreceiver.data.Video;
import tv.vizbee.demo.atvreceiver.data.VideoCatalog;
import tv.vizbee.demo.atvreceiver.data.VideoCatalogLoader;
import tv.vizbee.demo.atvreceiver.presenter.VideoCardPresenter;

public class MainFragment extends BrowseFragment implements LoaderManager.LoaderCallbacks<List<Video>> {

   private static final String LOG_TAG = "VZBApp_MainFragment";;

   private Drawable mDefaultBackground;
   private DisplayMetrics mMetrics;
   private BackgroundManager mBackgroundManager;
   private ArrayObjectAdapter mCategoryRowAdapter;

   //----
   // LifeCycle
   //----

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {

      Log.v(LOG_TAG, "onActivityCreated");
      super.onActivityCreated(savedInstanceState);

      // set title
      setTitle(getString(R.string.browse_title));

      // categories (vod/live)
      mCategoryRowAdapter = new ArrayObjectAdapter(new ListRowPresenter());
      setAdapter(mCategoryRowAdapter);

      getLoaderManager().initLoader(0, null, this);

      setupEventListeners();

      // background image
      prepareBackgroundManager();
   }

   @Override
   public void onDestroy() {
      Log.v(LOG_TAG, "onDestroy");
      super.onDestroy();
   }

   //----
   // Event listeners
   //----

   private void setupEventListeners() {

      // changes view background with the selected video image
      setOnItemViewClickedListener(new ItemViewClickedListener());

      // play on selection of video
      setOnItemViewSelectedListener(new ItemViewSelectedListener());
   }

   //----
   // View Background
   //----

   private void prepareBackgroundManager() {

      mBackgroundManager = BackgroundManager.getInstance(getActivity());
      mBackgroundManager.attach(getActivity().getWindow());

      mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
      mMetrics = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
   }

   private void updateBackground(String uri) {

      int width = mMetrics.widthPixels;
      int height = mMetrics.heightPixels;
      Glide.with(getActivity())
              .load(uri)
              .centerCrop()
              .error(mDefaultBackground)
              .into(new SimpleTarget<Drawable>(width, height) {
                 @Override
                 public void onResourceReady(Drawable resource,
                                             Transition<? super Drawable> transition) {
                    mBackgroundManager.setDrawable(resource);
                 }
              });
   }

   //----
   // LoaderCallbacks
   //----

   @Override
   public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
      return new VideoCatalogLoader(getActivity(), null);
   }

   @Override
   public void onLoadFinished(Loader<List<Video>> loader, List<Video> data) {

      VideoCardPresenter videoCardPresenter = new VideoCardPresenter();

      mCategoryRowAdapter.clear();

      int i;
      for (i = 0; i < VideoCatalog.VIDEO_CATEGORY.length; i++) {
         if (i != 0) {
            Collections.shuffle(data);
         }
         ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(videoCardPresenter);
         for (int j = 0; j < data.size(); j++) {
            listRowAdapter.add(data.get(j));
         }
         HeaderItem header = new HeaderItem(i, VideoCatalog.VIDEO_CATEGORY[i]);
         mCategoryRowAdapter.add(new ListRow(header, listRowAdapter));
      }
   }

   @Override
   public void onLoaderReset(Loader<List<Video>> loader) {
      mCategoryRowAdapter.clear();
   }

   //----
   // Item Select/Click
   //----

   private final class ItemViewSelectedListener implements OnItemViewSelectedListener {

      @Override
      public void onItemSelected(
              Presenter.ViewHolder itemViewHolder,
              Object item,
              RowPresenter.ViewHolder rowViewHolder,
              Row row) {
         if (item instanceof Video) {
            updateBackground(((Video) item).getImageURL());
         }
      }
   }

   private final class ItemViewClickedListener implements OnItemViewClickedListener {

      @Override
      public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                RowPresenter.ViewHolder rowViewHolder, Row row) {

         if (item instanceof Video) {
            Video movie = (Video) item;
            Log.d(LOG_TAG, "Item: " + item.toString());
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(MainActivity.VIDEO, movie);
            startActivity(intent);
         }
      }
   }
}
