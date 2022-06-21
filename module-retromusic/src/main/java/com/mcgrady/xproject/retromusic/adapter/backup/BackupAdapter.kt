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
package com.mcgrady.xproject.retromusic.adapter.backup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.ItemListBackupBinding
import java.io.File

class BackupAdapter(
    val activity: FragmentActivity,
    var dataSet: MutableList<File>,
    val backupClickedListener: BackupClickedListener
) : RecyclerView.Adapter<BackupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBackupBinding.inflate(LayoutInflater.from(activity), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = dataSet[position].nameWithoutExtension
    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun swapDataset(dataSet: List<File>) {
        this.dataSet = ArrayList(dataSet)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemListBackupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.menu.setOnClickListener { view ->
                val popupMenu = PopupMenu(activity, view)
                popupMenu.inflate(R.menu.menu_backup)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    return@setOnMenuItemClickListener backupClickedListener.onBackupMenuClicked(
                        dataSet[bindingAdapterPosition],
                        menuItem
                    )
                }
                popupMenu.show()
            }
            itemView.setOnClickListener {
                backupClickedListener.onBackupClicked(dataSet[bindingAdapterPosition])
            }
        }
    }

    interface BackupClickedListener {
        fun onBackupClicked(file: File)

        fun onBackupMenuClicked(file: File, menuItem: MenuItem): Boolean
    }
}
