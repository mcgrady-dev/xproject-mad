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
package com.mcgrady.xproject.theme.common.prefs.supportv7;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.mcgrady.xproject.theme.common.prefs.supportv7.dialogs.ATEListPreferenceDialogFragmentCompat;
import com.mcgrady.xproject.theme.common.prefs.supportv7.dialogs.ATEPreferenceDialogFragment;

/** @author Karim Abou Zeid (kabouzeid) */
public abstract class ATEPreferenceFragmentCompat extends PreferenceFragmentCompat {
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback) {
            ((OnPreferenceDisplayDialogCallback) getCallbackFragment())
                    .onPreferenceDisplayDialog(this, preference);
            return;
        }

        if (this.getActivity() instanceof OnPreferenceDisplayDialogCallback) {
            ((OnPreferenceDisplayDialogCallback) this.getActivity())
                    .onPreferenceDisplayDialog(this, preference);
            return;
        }

        if (getFragmentManager().findFragmentByTag("androidx.preference.PreferenceFragment.DIALOG")
                == null) {
            DialogFragment dialogFragment = onCreatePreferenceDialog(preference);

            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(
                        this.getFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
                return;
            }
        }

        super.onDisplayPreferenceDialog(preference);
    }

    @Nullable
    public DialogFragment onCreatePreferenceDialog(Preference preference) {
        if (preference instanceof ATEListPreference) {
            return ATEListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
        } else if (preference instanceof ATEDialogPreference) {
            return ATEPreferenceDialogFragment.newInstance(preference.getKey());
        }
        return null;
    }
}
