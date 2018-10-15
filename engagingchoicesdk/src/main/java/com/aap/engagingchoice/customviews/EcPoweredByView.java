package com.aap.engagingchoice.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aap.engagingchoice.R;

/**
 * This class is custom view of Poweredby logo
 */
public class EcPoweredByView extends LinearLayout {


    public EcPoweredByView(Context context) {
        this(context, null);
    }

    public EcPoweredByView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EcPoweredByView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ImageView poweredByIv = new ImageView(context, attrs, defStyleAttr);
        poweredByIv.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        TextView poweredByTv = new TextView(context, attrs, defStyleAttr);
        int sp = (int) (getResources().getDimension(R.dimen.sp_10) / getResources().getDisplayMetrics().density);
        poweredByTv.setTextSize(sp);
        poweredByTv.setTextColor(getResources().getColor(R.color.grey));
        poweredByTv.setText(getResources().getString(R.string.powered_by));
        poweredByTv.setTypeface(poweredByTv.getTypeface(), Typeface.ITALIC);
        LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTv.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout.LayoutParams paramsIv = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int dp = (int) (getResources().getDimension(R.dimen.dp_2) / getResources().getDisplayMetrics().density);
        paramsIv.setMargins(dp, 0, 0, 0);
        setOrientation(HORIZONTAL);
        this.addView(poweredByTv, paramsTv);
        this.addView(poweredByIv, paramsIv);
    }
}
