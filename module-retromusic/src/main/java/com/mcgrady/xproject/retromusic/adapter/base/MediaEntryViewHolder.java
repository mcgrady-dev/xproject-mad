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
package com.mcgrady.xproject.retromusic.adapter.base;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.card.MaterialCardView;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.mcgrady.xproject.retromusic.R;

public class MediaEntryViewHolder extends AbstractDraggableSwipeableItemViewHolder
        implements View.OnLongClickListener, View.OnClickListener {

    @Nullable public View dragView;

    @Nullable public View dummyContainer;

    @Nullable public ImageView image;

    @Nullable public MaterialCardView imageContainerCard;

    @Nullable public FrameLayout imageContainer;

    @Nullable public TextView imageText;

    @Nullable public MaterialCardView imageTextContainer;

    @Nullable public View mask;

    @Nullable public AppCompatImageView menu;

    @Nullable public View paletteColorContainer;

    @Nullable public TextView text;

    @Nullable public TextView text2;

    @Nullable public TextView time;

    @Nullable public TextView title;

    public MediaEntryViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        text = itemView.findViewById(R.id.text);
        text2 = itemView.findViewById(R.id.text2);

        image = itemView.findViewById(R.id.image);
        time = itemView.findViewById(R.id.time);

        imageText = itemView.findViewById(R.id.imageText);
        imageTextContainer = itemView.findViewById(R.id.imageTextContainer);
        imageContainerCard = itemView.findViewById(R.id.imageContainerCard);
        imageContainer = itemView.findViewById(R.id.imageContainer);

        menu = itemView.findViewById(R.id.menu);
        dragView = itemView.findViewById(R.id.drag_view);
        paletteColorContainer = itemView.findViewById(R.id.paletteColorContainer);
        mask = itemView.findViewById(R.id.mask);
        dummyContainer = itemView.findViewById(R.id.dummy_view);

        if (imageContainerCard != null) {
            imageContainerCard.setCardBackgroundColor(Color.TRANSPARENT);
        }
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Nullable
    @Override
    public View getSwipeableContainerView() {
        return null;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void setImageTransitionName(@NonNull String transitionName) {
        itemView.setTransitionName(transitionName);
        /* if (imageContainerCard != null) {
            imageContainerCard.setTransitionName(transitionName);
        }
        if (image != null) {
            image.setTransitionName(transitionName);
        }*/
    }
}
