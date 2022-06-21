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
package com.mcgrady.xproject.retromusic.misc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;
import com.mcgrady.xproject.retromusic.R;
import java.lang.ref.WeakReference;
import java.util.List;

/** @author Karim Abou Zeid (kabouzeid) */
public class UpdateToastMediaScannerCompletionListener
        implements MediaScannerConnection.OnScanCompletedListener {

    private final WeakReference<Activity> activityWeakReference;

    private final String couldNotScanFiles;
    private final String scannedFiles;
    private final List<String> toBeScanned;
    private int failed = 0;
    private int scanned = 0;
    private final Toast toast;

    @SuppressLint("ShowToast")
    public UpdateToastMediaScannerCompletionListener(Activity activity, List<String> toBeScanned) {
        this.toBeScanned = toBeScanned;
        scannedFiles = activity.getString(R.string.scanned_files);
        couldNotScanFiles = activity.getString(R.string.could_not_scan_files);
        toast = Toast.makeText(activity.getApplicationContext(), "", Toast.LENGTH_SHORT);
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onScanCompleted(final String path, final Uri uri) {
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            activity.runOnUiThread(
                    () -> {
                        if (uri == null) {
                            failed++;
                        } else {
                            scanned++;
                        }
                        String text =
                                " "
                                        + String.format(scannedFiles, scanned, toBeScanned.size())
                                        + (failed > 0
                                                ? " " + String.format(couldNotScanFiles, failed)
                                                : "");
                        toast.setText(text);
                        toast.show();
                    });
        }
    }
}
