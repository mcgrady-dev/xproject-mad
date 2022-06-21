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
package com.mcgrady.xproject.retromusic.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.PreferenceDialogLibraryCategoriesListitemBinding
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.retromusic.model.CategoryInfo
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.SwipeAndDragHelper
import com.mcgrady.xproject.retromusic.util.SwipeAndDragHelper.ActionCompletionContract
import com.mcgrady.xproject.theme.ThemeStore.Companion.accentColor

class CategoryInfoAdapter :
    RecyclerView.Adapter<CategoryInfoAdapter.ViewHolder>(),
    ActionCompletionContract {
    var categoryInfos: MutableList<CategoryInfo> =
        PreferenceUtil.libraryCategory.toMutableList()
        @SuppressLint("NotifyDataSetChanged")
            set(value) {
                field = value
                notifyDataSetChanged()
            }
    private val touchHelper: ItemTouchHelper
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return categoryInfos.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryInfo = categoryInfos[position]
        holder.binding.checkbox.isChecked = categoryInfo.visible
        holder.binding.title.text =
            holder.binding.title.resources.getString(categoryInfo.category.stringRes)
        holder.itemView.setOnClickListener {
            if (!(categoryInfo.visible && isLastCheckedCategory(categoryInfo))) {
                categoryInfo.visible = !categoryInfo.visible
                holder.binding.checkbox.isChecked = categoryInfo.visible
            } else {
                holder.itemView.context.showToast(R.string.you_have_to_select_at_least_one_category)
            }
        }
        holder.binding.dragView.setOnTouchListener { _: View?, event: MotionEvent ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder)
            }
            false
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            PreferenceDialogLibraryCategoriesListitemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        val categoryInfo = categoryInfos[oldPosition]
        categoryInfos.removeAt(oldPosition)
        categoryInfos.add(newPosition, categoryInfo)
        notifyItemMoved(oldPosition, newPosition)
    }

    private fun isLastCheckedCategory(categoryInfo: CategoryInfo): Boolean {
        if (categoryInfo.visible) {
            for (c in categoryInfos) {
                if (c !== categoryInfo && c.visible) {
                    return false
                }
            }
        }
        return true
    }

    class ViewHolder(val binding: PreferenceDialogLibraryCategoriesListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.checkbox.buttonTintList =
                ColorStateList.valueOf(accentColor(binding.checkbox.context))
        }
    }

    init {
        val swipeAndDragHelper = SwipeAndDragHelper(this)
        touchHelper = ItemTouchHelper(swipeAndDragHelper)
    }
}
