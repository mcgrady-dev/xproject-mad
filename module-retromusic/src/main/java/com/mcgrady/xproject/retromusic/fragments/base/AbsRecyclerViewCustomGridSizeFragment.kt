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
package com.mcgrady.xproject.retromusic.fragments.base

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFade
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.util.RetroUtil
import com.mcgrady.xproject.retromusic.util.logD

abstract class AbsRecyclerViewCustomGridSizeFragment<A : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> :
    AbsRecyclerViewFragment<A, LM>() {

    private var gridSize: Int = 0
    private var sortOrder: String? = null
    private var currentLayoutRes: Int = 0
    private val isLandscape: Boolean
        get() = RetroUtil.isLandscape

    val maxGridSize: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.max_columns_land)
        } else {
            resources.getInteger(R.integer.max_columns)
        }

    fun itemLayoutRes(): Int {
        return if (getGridSize() > maxGridSizeForList) {
            loadLayoutRes()
        } else R.layout.item_list
    }

    fun setAndSaveLayoutRes(layoutRes: Int) {
        saveLayoutRes(layoutRes)
        invalidateAdapter()
    }

    private val maxGridSizeForList: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.default_list_columns_land)
        } else resources.getInteger(R.integer.default_list_columns)

    fun getGridSize(): Int {
        if (gridSize == 0) {
            gridSize = if (isLandscape) {
                loadGridSizeLand()
            } else {
                loadGridSize()
            }
        }
        return gridSize
    }

    fun getSortOrder(): String? {
        if (sortOrder == null) {
            sortOrder = loadSortOrder()
        }
        return sortOrder
    }

    fun setAndSaveSortOrder(sortOrder: String) {
        this.sortOrder = sortOrder
        logD(sortOrder)
        saveSortOrder(sortOrder)
        setSortOrder(sortOrder)
    }

    fun setAndSaveGridSize(gridSize: Int) {
        val oldLayoutRes = itemLayoutRes()
        this.gridSize = gridSize
        if (isLandscape) {
            saveGridSizeLand(gridSize)
        } else {
            saveGridSize(gridSize)
        }
        recyclerView.isVisible = false
        invalidateLayoutManager()
        // only recreate the adapter and layout manager if the layout currentLayoutRes has changed
        if (oldLayoutRes != itemLayoutRes()) {
            invalidateAdapter()
        } else {
            setGridSize(gridSize)
        }
        val transition = MaterialFade().apply {
            addTarget(recyclerView)
        }
        TransitionManager.beginDelayedTransition(container, transition)
        recyclerView.isVisible = true
    }

    protected abstract fun setGridSize(gridSize: Int)

    protected abstract fun setSortOrder(sortOrder: String)

    protected abstract fun loadSortOrder(): String

    protected abstract fun saveSortOrder(sortOrder: String)

    protected abstract fun loadGridSize(): Int

    protected abstract fun saveGridSize(gridColumns: Int)

    protected abstract fun loadGridSizeLand(): Int

    protected abstract fun saveGridSizeLand(gridColumns: Int)

    protected abstract fun loadLayoutRes(): Int

    protected abstract fun saveLayoutRes(layoutRes: Int)
}
