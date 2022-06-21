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
package com.mcgrady.xproject.theme.common;

import android.graphics.Color;
import android.view.Menu;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.mcgrady.xproject.theme.ATHActivity;
import com.mcgrady.xproject.theme.R;
import com.mcgrady.xproject.theme.util.ATHUtil;
import com.mcgrady.xproject.theme.util.ToolbarContentTintHelper;

public class ATHToolbarActivity extends ATHActivity {

    private Toolbar toolbar;

    public static int getToolbarBackgroundColor(@Nullable Toolbar toolbar) {
        if (toolbar != null) {
            return ATHUtil.INSTANCE.resolveColor(toolbar.getContext(), R.attr.colorSurface);
        }
        return Color.BLACK;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar toolbar = getATHToolbar();
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
                this, toolbar, menu, getToolbarBackgroundColor(toolbar));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(this, getATHToolbar());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        this.toolbar = toolbar;
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getATHToolbar() {
        return toolbar;
    }
}
