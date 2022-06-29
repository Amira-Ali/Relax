package com.relax.utilities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;

public class leadingMarginSpan2 implements LeadingMarginSpan2 {
    private final int margin;
    private final int lines;
    private boolean wasDrawCalled = false;
    private int drawLineCount = 0;

    public leadingMarginSpan2(int lines, int margin) {
        this.margin = margin;
        this.lines = lines;
    }

    public int getLeadingMargin(boolean first) {
        boolean IsFirstMargin = first;
        // a different algorithm for api 21+
        if (Build.VERSION.SDK_INT >= 21) {
            this.drawLineCount = this.wasDrawCalled ? this.drawLineCount + 1 : 0;
            this.wasDrawCalled = false;
            IsFirstMargin = this.drawLineCount <= this.lines;
        }

        return IsFirstMargin ? this.margin : 0;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        this.wasDrawCalled = true;
    }

    public int getLeadingMarginLineCount() {
        return this.lines;
    }
}
