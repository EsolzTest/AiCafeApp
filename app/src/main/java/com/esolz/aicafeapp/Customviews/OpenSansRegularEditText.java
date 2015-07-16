package com.esolz.aicafeapp.Customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ltp on 15/07/15.
 */
public class OpenSansRegularEditText extends EditText {

    public OpenSansRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public OpenSansRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public OpenSansRegularEditText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public void init() {

        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "OpenSans-Regular.ttf"));

    }

}
