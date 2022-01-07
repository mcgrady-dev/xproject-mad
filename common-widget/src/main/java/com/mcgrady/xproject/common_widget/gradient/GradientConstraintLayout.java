package com.mcgrady.xproject.common_widget.gradient;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by mcgrady on 2021/5/24.
 */
public class GradientConstraintLayout extends ConstraintLayout {
    private GradientBackground gradientBackground;


    public GradientConstraintLayout(Context context) {
        super(context);
        this.gradientBackground = new GradientBackground(context, null);
    }

    public GradientConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gradientBackground = new GradientBackground(context, attrs);
    }

    public GradientConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.gradientBackground = new GradientBackground(context, attrs);
    }

    /**
     * <p>Configure the gradient background for this layout.</p>
     * @param startColor The start color for the gradient
     * @param endColor The end color for the gradient
     * @param orientation The orientation for the gradient {@link  GradientDrawable.Orientation  GradientDrawable.Orientation}
     */

    public void setGradientBackgroundConfig(final int startColor, final int endColor, final GradientDrawable.Orientation orientation) {
        this.gradientBackground
                .setStartColor(startColor)
                .setEndColor(endColor)
                .setOrientation(orientation)
                .generate();
        setBackground();
    }
    /**
     * <p>Configure the start color for the gradient background for this layout.</p>
     * @param startColor The start color for the gradient
     */
    public GradientConstraintLayout setStartColor(final int startColor) {
        this.gradientBackground.setStartColor(startColor).generate();
        return this;
    }

    /**
     * <p>Configure the end color for the gradient background for this layout.</p>
     * @param endColor The end color for the gradient
     */
    public GradientConstraintLayout setEndColor(final int endColor) {
        this.gradientBackground.setEndColor(endColor).generate();
        return this;
    }

    /**
     * <p>Configure the top left corner radius</p>
     * @param radius The radius to set
     */
    public GradientConstraintLayout setRadiusCornerTopLeft(final float radius) {
        this.gradientBackground.setRadiusCornerTopLeft(radius);
        return this;
    }

    /**
     * <p>Configure the top right corner radius</p>
     * @param radius The radius to set
     */
    public GradientConstraintLayout setRadiusCornerTopRight(final float radius) {
        this.gradientBackground.setRadiusCornerTopRight(radius);
        return this;
    }

    /**
     * <p>Configure the top left corner radius</p>
     * @param radius The radius to set
     */
    public GradientConstraintLayout setRadiusCornerBottomRight(final float radius) {
        this.gradientBackground.setRadiusCornerBottomRight(radius);
        return this;
    }

    /**
     * <p>Configure the top left corner radius</p>
     * @param radius The radius to set
     */
    public GradientConstraintLayout setRadiusCornerBottomLeft(final float radius) {
        this.gradientBackground.setRadiusCornerBottomLeft(radius);
        return this;
    }

    /**
     * <p>Configure the orientation for the gradient background for this layout.</p>
     * @param orientation The orientation for the gradient {@link  GradientDrawable.Orientation  GradientDrawable.Orientation}
     */
    public GradientConstraintLayout setOrientation(final GradientDrawable.Orientation orientation) {
        this.gradientBackground.setOrientation(orientation).generate();
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setBackground();
    }

    public void setBackground() {
        final GradientDrawable background = this.gradientBackground.generate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(background);
        } else {
            this.setBackgroundDrawable(background);
        }
    }
}
