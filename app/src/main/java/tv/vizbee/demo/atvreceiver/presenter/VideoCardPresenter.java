package tv.vizbee.demo.atvreceiver.presenter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;

import tv.vizbee.demo.atvreceiver.R;
import tv.vizbee.demo.atvreceiver.data.Video;

public class VideoCardPresenter extends Presenter {

    private static final String LOG_TAG = VideoCardPresenter.class.getSimpleName();

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        Log.v(LOG_TAG, "onCreateViewHolder");

        sDefaultBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.selected_background);

        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.video_backup);

        ImageCardView cardView =
                new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        Log.v(LOG_TAG, "onBindViewHolder");

        Video video = (Video) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        if (video.getImageURL() != null) {
            cardView.setTitleText(video.getTitle());
            cardView.setContentText(video.getTitle());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            Glide.with(viewHolder.view.getContext())
                    .load(video.getImageURL())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

        Log.v(LOG_TAG, "onUnbindViewHolder");

        // remove references to images so that the garbage collector can free up memory
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {

        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }
}
