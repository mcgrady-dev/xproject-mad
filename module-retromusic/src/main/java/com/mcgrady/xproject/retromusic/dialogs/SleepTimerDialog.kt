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
package com.mcgrady.xproject.retromusic.dialogs

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.DialogSleepTimerBinding
import com.mcgrady.xproject.retromusic.extensions.addAccentColor
import com.mcgrady.xproject.retromusic.extensions.materialDialog
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.service.MusicService
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_PENDING_QUIT
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_QUIT
import com.mcgrady.xproject.retromusic.util.MusicUtil
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.util.VersionUtils

class SleepTimerDialog : DialogFragment() {

    private var seekArcProgress: Int = 0
    private lateinit var timerUpdater: TimerUpdater
    private lateinit var dialog: AlertDialog

    private var _binding: DialogSleepTimerBinding? = null
    private val binding get() = _binding!!

    private val shouldFinishLastSong: CheckBox get() = binding.shouldFinishLastSong
    private val seekBar: SeekBar get() = binding.seekBar
    private val timerDisplay: TextView get() = binding.timerDisplay

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        timerUpdater = TimerUpdater()
        _binding = DialogSleepTimerBinding.inflate(layoutInflater)

        val finishMusic = PreferenceUtil.isSleepTimerFinishMusic
        shouldFinishLastSong.apply {
            addAccentColor()
            isChecked = finishMusic
        }
        seekBar.apply {
            addAccentColor()
            seekArcProgress = PreferenceUtil.lastSleepTimerValue
            updateTimeDisplayTime()
            progress = seekArcProgress
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (i < 1) {
                    seekBar.progress = 1
                    return
                }
                seekArcProgress = i
                updateTimeDisplayTime()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                PreferenceUtil.lastSleepTimerValue = seekArcProgress
            }
        })

        materialDialog(R.string.action_sleep_timer).apply {
            if (PreferenceUtil.nextSleepTimerElapsedRealTime > System.currentTimeMillis()) {
                seekBar.isVisible = false
                shouldFinishLastSong.isVisible = false
                timerUpdater.start()
                setPositiveButton(android.R.string.ok, null)
                setNegativeButton(R.string.cast_stop) { _, _ ->
                    timerUpdater.cancel()
                    val previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE)
                    if (previous != null) {
                        val am = requireContext().getSystemService<AlarmManager>()
                        am?.cancel(previous)
                        previous.cancel()
                        Toast.makeText(
                            requireContext(),
                            requireContext().resources.getString(R.string.sleep_timer_canceled),
                            Toast.LENGTH_SHORT
                        ).show()
                        val musicService = MusicPlayerRemote.musicService
                        if (musicService != null && musicService.pendingQuit) {
                            musicService.pendingQuit = false
                            Toast.makeText(
                                requireContext(),
                                requireContext().resources.getString(R.string.sleep_timer_canceled),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                seekBar.isVisible = true
                shouldFinishLastSong.isVisible = true
                setPositiveButton(R.string.action_set) { _, _ ->
                    PreferenceUtil.isSleepTimerFinishMusic = shouldFinishLastSong.isChecked
                    val minutes = seekArcProgress
                    val pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)
                    val nextSleepTimerElapsedTime =
                        SystemClock.elapsedRealtime() + minutes * 60 * 1000
                    PreferenceUtil.nextSleepTimerElapsedRealTime = nextSleepTimerElapsedTime.toInt()
                    val am = requireContext().getSystemService<AlarmManager>()
                    am?.setExact(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        nextSleepTimerElapsedTime,
                        pi
                    )

                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.sleep_timer_set, minutes),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            setView(binding.root)
            dialog = create()
        }
        return dialog
    }

    private fun updateTimeDisplayTime() {
        timerDisplay.text = "$seekArcProgress min"
    }

    private fun makeTimerPendingIntent(flag: Int): PendingIntent? {
        return PendingIntent.getService(
            requireActivity(), 0, makeTimerIntent(),
            flag or if (VersionUtils.hasMarshmallow())
                PendingIntent.FLAG_IMMUTABLE
            else 0
        )
    }

    private fun makeTimerIntent(): Intent {
        val intent = Intent(requireActivity(), MusicService::class.java)
        return if (shouldFinishLastSong.isChecked) {
            intent.setAction(ACTION_PENDING_QUIT)
        } else intent.setAction(ACTION_QUIT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        timerUpdater.cancel()
        _binding = null
    }

    private inner class TimerUpdater :
        CountDownTimer(
            PreferenceUtil.nextSleepTimerElapsedRealTime - SystemClock.elapsedRealtime(),
            1000
        ) {

        override fun onTick(millisUntilFinished: Long) {
            timerDisplay.text = MusicUtil.getReadableDurationString(millisUntilFinished)
        }

        override fun onFinish() {}
    }
}
