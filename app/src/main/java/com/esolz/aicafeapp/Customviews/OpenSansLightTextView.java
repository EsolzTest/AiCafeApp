package com.esolz.aicafeapp.Customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ltp on 15/07/15.
 */
public class OpenSansLightTextView extends TextView {

    public OpenSansLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public OpenSansLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public OpenSansLightTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public void init() {

        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "OpenSans-Light.ttf"));

    }

}


