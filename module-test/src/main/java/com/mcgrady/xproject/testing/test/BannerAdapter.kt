package com.mcgrady.xproject.testing.test

import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * Created by mcgrady on 2022/1/5.
 */
abstract class BannerAdapter<T, VH : RecyclerView.ViewHolder?>(datas: List<T>?) : RecyclerView.Adapter<VH>() {

    protected var mDatas: List<out T>? = mutableListOf()

    /**
     * 设置实体集合（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param datas
     */
    fun setDatas(datas: List<T>?) {
        var datas = datas
        if (datas == null) {
            datas = ArrayList()
        }
        mDatas = datas
        notifyDataSetChanged()
    }

}