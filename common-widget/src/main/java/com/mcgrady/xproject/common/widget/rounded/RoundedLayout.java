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
package com.mcgrady.xproject.common.widget.rounded;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.mcgrady.xproject.common.widget.R;
import com.mcgrady.xproject.common.widget.gradient.GradientBackground;

/** Created by mcgrady on 2021/5/28. */
public class RoundedLayout extends ConstraintLayout {
    private Path mClipPath = new Path();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mLayoutBounds = new RectF();
    private final float[] mRoundCornerRadii = new float[8];
    private boolean mIsLaidOut = false;

    private int mRoundedCornerRadius;
    private boolean mRoundAsCircle;
    private boolean mRoundTopLeft;
    private boolean mRoundTopRight;
    private boolean mRoundBottomLeft;
    private boolean mRoundBottomRight;

    private int mRoundingBorderWidth;
    private int mRoundingBorderColor;
    private int bgColor, bgStartColor, bgEndColor;

    private GradientDrawable.Orientation orientation;

    private GradientBackground gradientBackground;

    private GradientDrawable mBackground = new GradientDrawable();

    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public RoundedLayout(Context context) {
        super(context);
        initLayouts(context, null, 0, 0);
    }

    public RoundedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayouts(context, attrs, 0, 0);
    }

    public RoundedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayouts(context, attrs, defStyleAttr, 0);
    }

    private void initLayouts(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (isInEditMode()) {
            return;
        }

        TypedArray a =
                context.obtainStyledAttributes(
                        attrs, R.styleable.RoundedLayout, defStyleAttr, defStyleRes);
        mRoundedCornerRadius =
                a.getDimensionPixelSize(R.styleable.RoundedLayout_rlRoundedCornerRadius, 0);
        mRoundAsCircle = a.getBoolean(R.styleable.RoundedLayout_rlRoundAsCircle, false);
        mRoundTopLeft = a.getBoolean(R.styleable.RoundedLayout_rlRoundTopLeft, true);
        mRoundTopRight = a.getBoolean(R.styleable.RoundedLayout_rlRoundTopRight, true);
        mRoundBottomLeft = a.getBoolean(R.styleable.RoundedLayout_rlRoundBottomLeft, true);
        mRoundBottomRight = a.getBoolean(R.styleable.RoundedLayout_rlRoundBottomRight, true);
        mRoundingBorderWidth =
                a.getDimensionPixelSize(R.styleable.RoundedLayout_rlRoundingBorderWidth, 0);
        mRoundingBorderColor = a.getColor(R.styleable.RoundedLayout_rlRoundingBorderColor, 0);
        bgColor = a.getColor(R.styleable.RoundedLayout_rlBgColor, 0);
        bgStartColor = a.getColor(R.styleable.RoundedLayout_rlBgStartColor, 0);
        bgEndColor = a.getColor(R.styleable.RoundedLayout_rlBgEndColor, 0);
        if (a.hasValue(R.styleable.RoundedLayout_rlColorOrientation)) {
            int attrOrientation =
                    a.getInt(
                            R.styleable.RoundedLayout_rlColorOrientation,
                            GradientDrawable.Orientation.LEFT_RIGHT.ordinal());
            intToOrientation(attrOrientation);
        }
        a.recycle();

        setWillNotDraw(false);
        mPaint.setColor(mRoundingBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRoundingBorderWidth * 2);

        this.gradientBackground = new GradientBackground(context, null);

        if (bgStartColor != 0 && bgEndColor != 0) {
            int colors[] = {bgStartColor, bgEndColor};
            final GradientDrawable.Orientation validOrientation =
                    this.orientation == null
                            ? GradientDrawable.Orientation.LEFT_RIGHT
                            : this.orientation;

            mBackground.setColors(colors);
            mBackground.setOrientation(validOrientation);
        } else {
            mBackground.setColor(bgColor | 0x00000000);
        }
        mBackground.setCornerRadii(buildRoundCornerRadii(mRoundedCornerRadius));

        setBackground();
    }

    @Override
    public void setBackground(Drawable background) {
        // super.setBackground(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        // super.setBackgroundDrawable(background);
    }

    public void setBackground() {
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        //            super.setBackground(mBackground);
        //        } else {
        super.setBackgroundDrawable(mBackground);
        //        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        adjustClipPathBounds();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLayoutBounds.set(0, 0, right - left, bottom - top);
        if (!mIsLaidOut) {
            mIsLaidOut = true;
            adjustClipPathBounds();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsLaidOut = false;
    }

    private void adjustClipPathBounds() {
        if (!mIsLaidOut) {
            return;
        }
        float cornerRadius = mRoundedCornerRadius;
        if (mRoundAsCircle) {
            cornerRadius = Math.max(mLayoutBounds.width(), mLayoutBounds.height()) / 2f;
        }
        mClipPath.reset();
        mClipPath.addRoundRect(
                mLayoutBounds, buildRoundCornerRadii(cornerRadius), Path.Direction.CW);
        mClipPath.close();

        mBackground.setCornerRadii(buildRoundCornerRadii(cornerRadius));
    }

    private float[] buildRoundCornerRadii(float cornerRadius) {
        mRoundCornerRadii[0] = mRoundTopLeft ? cornerRadius : 0f;
        mRoundCornerRadii[1] = mRoundTopLeft ? cornerRadius : 0f;
        mRoundCornerRadii[2] = mRoundTopRight ? cornerRadius : 0f;
        mRoundCornerRadii[3] = mRoundTopRight ? cornerRadius : 0f;
        mRoundCornerRadii[4] = mRoundBottomRight ? cornerRadius : 0f;
        mRoundCornerRadii[5] = mRoundBottomRight ? cornerRadius : 0f;
        mRoundCornerRadii[6] = mRoundBottomLeft ? cornerRadius : 0f;
        mRoundCornerRadii[7] = mRoundBottomLeft ? cornerRadius : 0f;
        return mRoundCornerRadii;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clipPath(mClipPath);
        super.draw(canvas);
        if (mRoundingBorderWidth > 0 && mRoundingBorderColor != 0) {
            canvas.drawPath(mClipPath, mPaint);
        }
    }

    /**
     * Setts the width of the View rounded border.
     *
     * @param width View the width of the rounded border . Unit px
     */
    public void setRoundingBorderWidth(int width) {
        if (width != mRoundingBorderWidth) {
            mRoundingBorderWidth = width;
            mPaint.setStrokeWidth(mRoundingBorderWidth * 2);
            postInvalidate();
        }
    }

    /**
     * Setts the color of the View rounded border.
     *
     * @param color View the color of the rounded border
     */
    public void setRoundingBorderColor(int color) {
        if (color != mRoundingBorderColor) {
            mRoundingBorderColor = color;
            mPaint.setColor(mRoundingBorderColor);
            mBackground.setColor(mRoundingBorderColor | 0xFF000000);
            postInvalidate();
        }
    }

    public void setBackgroundColors(int startColor, int endColor) {
        boolean hasChange = false;
        if (startColor != bgStartColor) {
            bgStartColor = startColor;
            hasChange = true;
        }

        if (endColor != bgEndColor) {
            bgEndColor = endColor;
            hasChange = true;
        }

        if (hasChange) {
            int colors[] = {bgStartColor, bgEndColor};
            mBackground.setColors(colors);

            final GradientDrawable.Orientation validOrientation =
                    this.orientation == null
                            ? GradientDrawable.Orientation.LEFT_RIGHT
                            : this.orientation;
            mBackground.setOrientation(validOrientation);
            mBackground.setCornerRadii(buildRoundCornerRadii(mRoundedCornerRadius));
            //            postInvalidate();

        }
    }

    /**
     * Set's whether to round as circle
     *
     * @param asCircle Whether to round as circle
     */
    public void setRoundAsCircle(boolean asCircle) {
        if (asCircle != mRoundAsCircle) {
            mRoundAsCircle = asCircle;
            adjustClipPathBounds();
            postInvalidate();
        }
    }

    /**
     * Set the radius of the corner angle and whether the edges are enabled
     *
     * @param cornerRadius The radius of the angle of the circle. Unit px
     */
    public void setRoundedCornerRadius(int cornerRadius) {
        setRoundedCornerRadius(cornerRadius, true, true, true, true);
    }

    /**
     * Set the radius of the corner angle and whether the edges are enabled
     *
     * @param cornerRadius The radius of the angle of the circle. Unit px
     * @param topLeft Whether to enable top left fillet
     * @param topRight Whether to enable top right fillet
     * @param bottomLeft Whether to enable bottom left fillet
     * @param bottomRight Whether to enable bottom right fillet
     */
    public void setRoundedCornerRadius(
            int cornerRadius,
            boolean topLeft,
            boolean topRight,
            boolean bottomRight,
            boolean bottomLeft) {
        if (mRoundedCornerRadius != cornerRadius
                || mRoundTopLeft != topLeft
                || mRoundTopRight != topRight
                || mRoundBottomLeft != bottomLeft
                || mRoundBottomRight != bottomRight) {
            mRoundedCornerRadius = cornerRadius;
            mRoundTopLeft = topLeft;
            mRoundTopRight = topRight;
            mRoundBottomLeft = bottomLeft;
            mRoundBottomRight = bottomRight;
            adjustClipPathBounds();
            postInvalidate();
        }
    }

    /**
     * Gets the angle of the corner of the View fillet.
     *
     * @return Returns the current angle of the corner of the View fillet
     */
    public int getRoundedCornerRadius() {
        return mRoundedCornerRadius;
    }

    /**
     * Gets whether to round as circle
     *
     * @return Returns whether to round as circle
     */
    public boolean isRoundAsCircle() {
        return mRoundAsCircle;
    }

    /**
     * Gets whether to enable top left fillet
     *
     * @return Returns whether to enable top left fillet
     */
    public boolean isRoundTopLeft() {
        return mRoundTopLeft;
    }

    /**
     * Gets whether to enable top right fillet
     *
     * @return Returns whether to enable top right fillet
     */
    public boolean isRoundTopRight() {
        return mRoundTopRight;
    }

    /**
     * Gets whether to enable bottom left fillet
     *
     * @return Returns whether to enable bottom left fillet
     */
    public boolean isRoundBottomLeft() {
        return mRoundBottomLeft;
    }

    /**
     * Gets whether to enable bottom right fillet
     *
     * @return Returns whether to enable bottom right fillet
     */
    public boolean isRoundBottomRight() {
        return mRoundBottomRight;
    }

    /**
     * Gets the width of the View rounded border. Unit px
     *
     * @return Returns the current width of the View Rounded Border
     */
    public int getRoundingBorderWidth() {
        return mRoundingBorderWidth;
    }

    /**
     * Gets the color of the View rounded border.
     *
     * @return Returns the current color of the View Rounded Border
     */
    public int getRoundingBorderColor() {
        return mRoundingBorderColor;
    }

    private GradientDrawable.Orientation intToOrientation(final int original) {
        switch (original) {
            case 1:
                return GradientDrawable.Orientation.TR_BL;
            case 2:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 3:
                return GradientDrawable.Orientation.BR_TL;
            case 4:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 5:
                return GradientDrawable.Orientation.BL_TR;
            case 6:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 7:
                return GradientDrawable.Orientation.TL_BR;
            case 0:
            default:
                return GradientDrawable.Orientation.TOP_BOTTOM;
        }
    }
}
