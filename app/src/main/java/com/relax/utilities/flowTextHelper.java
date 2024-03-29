package com.relax.utilities;

import android.text.SpannableString;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class flowTextHelper {

    private static final String TAG = "Flow";
    private static boolean mNewClassAvailable;

    /* class initialization fails when this throws an exception */
    static {
        try {
            Class.forName("android.text.style.LeadingMarginSpan$LeadingMarginSpan2");
            mNewClassAvailable = true;
        } catch (Exception ex) {
            mNewClassAvailable = false;
        }
    }

    public static void tryFlowText(String text, View thumbnailView, TextView messageView, Display display, int addPadding) {
        if (!mNewClassAvailable) return;
        //Get height and width of the image and height of the text line
        thumbnailView.measure(display.getWidth(), display.getHeight());
        int height = thumbnailView.getMeasuredHeight();
        int width = thumbnailView.getMeasuredWidth() + addPadding;
        messageView.measure(width, height);
        int padding = messageView.getTotalPaddingTop();
        float textLineHeight = messageView.getPaint().getTextSize();
        // Set the span according to the number of lines and width of the image
        int lines = Math.round((height - padding) / textLineHeight);
        SpannableString ss = new SpannableString(text);
        //For an html text you can use this line: SpannableStringBuilder ss = (SpannableStringBuilder)Html.fromHtml(text);
        ss.setSpan(new leadingMarginSpan2(lines, width), 0, lines, 0);
        messageView.setText(ss);

        // Align the text with the image by removing the rule that the text is to the right of the image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageView.getLayoutParams();
        params.setMargins(5, 0, 0, 0);
        int[] rules = params.getRules();
        rules[RelativeLayout.RIGHT_OF] = 0;
    }
}


