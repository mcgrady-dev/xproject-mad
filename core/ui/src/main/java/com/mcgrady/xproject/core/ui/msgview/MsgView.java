/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.core.ui.msgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.mcgrady.xproject.core.ui.R;

/** 用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件 Created by mcgrady on 2021/6/3. */
public class MsgView extends AppCompatTextView {
    private Context context;
    private GradientDrawable gd_background = new GradientDrawable();
    private int backgroundColor;
    private int cornerRadius;
    private int strokeWidth;
    private int strokeColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;

    public MsgView(Context context) {
        this(context, null);
    }

    public MsgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MsgView);
        backgroundColor = ta.getColor(R.styleable.MsgView_mv_backgroundColor, Color.TRANSPARENT);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.MsgView_mv_cornerRadius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.MsgView_mv_strokeWidth, 0);
        strokeColor = ta.getColor(R.styleable.MsgView_mv_strokeColor, Color.TRANSPARENT);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.MsgView_mv_isRadiusHalfHeight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.MsgView_mv_isWidthHeightEqual, false);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isRadiusHalfHeight()) {
            setCornerRadius(getHeight() / 2);
        } else {
            setBgSelector();
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBgSelector();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = dp2px(cornerRadius);
        setBgSelector();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = dp2px(strokeWidth);
        setBgSelector();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBgSelector();
    }

    public void setIsRadiusHalfHeight(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        setBgSelector();
    }

    public void setIsWidthHeightEqual(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        setBgSelector();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    public boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);
        gd.setCornerRadius(cornerRadius);
        gd.setStroke(strokeWidth, strokeColor);
        gd.setShape(GradientDrawable.OVAL);
    }

    public void setBgSelector() {
        StateListDrawable bg = new StateListDrawable();

        setDrawable(gd_background, backgroundColor, strokeColor);
        bg.addState(new int[] {-android.R.attr.state_pressed}, gd_background);
        setBackground(bg);
    }
}
