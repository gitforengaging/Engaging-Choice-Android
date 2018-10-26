package com.aap.engagingchoice.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aap.engagingchoice.R;
import com.bumptech.glide.Glide;

/**
 * This class is custom view of GridView
 */
public class EcGridView extends FrameLayout {
    public ImageView mCoverIv;
    private ImageView mPoweredByIv;

    public EcGridView(Context context) {
        this(context, null);
    }

    public EcGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EcGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCoverIv = new ImageView(context, attrs, defStyleAttr);
        mPoweredByIv = new ImageView(context, attrs, defStyleAttr);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        int dp = (int) (getResources().getDimension(R.dimen.dp_40) / getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams paramsPoweredBy = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsPoweredBy.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        this.addView(mCoverIv, params);
        this.addView(mPoweredByIv, paramsPoweredBy);
        mPoweredByIv.setImageDrawable(getResources().getDrawable(R.drawable.powered_by_engaging_choice));
        mCoverIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void setImageInFilmView(int drawable) {
        mCoverIv.setBackgroundResource(drawable);
        mCoverIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mCoverIv.setScaleType(scaleType);
    }

    public void setloadUrl(Context context, String url) {
        Glide.with(context).load(url).into(mCoverIv);
    }

    public void setVisibilityOfPoweredBy(int visibility) {
        mPoweredByIv.setVisibility(visibility);
    }
}
