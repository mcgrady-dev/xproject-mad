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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.retromusic.R
import java.io.File

class StorageAdapter(
    val storageList: List<Storage>,
    val storageClickListener: StorageClickListener
) :
    RecyclerView.Adapter<StorageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_storage,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(storageList[position])
    }

    override fun getItemCount(): Int {
        return storageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)

        fun bindData(storage: Storage) {
            title.text = storage.title
        }

        init {
            itemView.setOnClickListener { storageClickListener.onStorageClicked(storageList[bindingAdapterPosition]) }
        }
    }
}

interface StorageClickListener {
    fun onStorageClicked(storage: Storage)
}

class Storage {
    lateinit var title: String
    lateinit var file: File
}
