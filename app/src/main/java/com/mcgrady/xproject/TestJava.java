package com.mcgrady.xproject;

import android.view.View;

/**
 * Created by mcgrady on 2021/12/20.
 */
public class TestJava {


    private void main() {

        new Thread(() -> {

        }).start();

        new View(null).setOnClickListener(v -> {

        });

        new View(null).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
