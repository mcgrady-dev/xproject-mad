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
package com.mcgrady.xproject.retromusic.views;

import android.graphics.drawable.GradientDrawable;

public class DrawableGradient extends GradientDrawable {
    public DrawableGradient(Orientation orientations, int[] colors, int shape) {
        super(orientations, colors);
        try {
            setShape(shape);
            setGradientType(GradientDrawable.LINEAR_GRADIENT);
            setCornerRadius(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DrawableGradient SetTransparency(int transparencyPercent) {
        this.setAlpha(255 - ((255 * transparencyPercent) / 100));
        return this;
    }
}
