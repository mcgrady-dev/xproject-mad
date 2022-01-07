package com.mcgrady.xproject.testing.test;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by mcgrady on 2022/1/5.
 */
public class ImageAdapter extends BannerAdapter<DataBean, ImageHolder>{

    public ImageAdapter(@Nullable List<? extends DataBean> datas) {
        super(datas);
    }

    public void updateDatas(List<DataBean> datas) {
        getMDatas().clear();
        getMDatas().addAll(datas);
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
