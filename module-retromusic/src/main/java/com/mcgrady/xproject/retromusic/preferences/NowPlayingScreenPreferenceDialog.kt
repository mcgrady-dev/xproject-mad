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
package com.mcgrady.xproject.retromusic.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.PreferenceNowPlayingScreenItemBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.retromusic.fragments.NowPlayingScreen
import com.mcgrady.xproject.retromusic.fragments.NowPlayingScreen.*
import com.mcgrady.xproject.retromusic.util.NavigationUtil
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.ViewUtil
import com.mcgrady.xproject.theme.common.prefs.supportv7.ATEDialogPreference

class NowPlayingScreenPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    private val mLayoutRes = R.layout.preference_dialog_now_playing_screen

    override fun getDialogLayoutResource(): Int {
        return mLayoutRes
    }

    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }
}

class NowPlayingScreenPreferenceDialog : DialogFragment(), ViewPager.OnPageChangeListener {

    private var viewPagerPosition: Int = 0

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        this.viewPagerPosition = position
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater
            .inflate(R.layout.preference_dialog_now_playing_screen, null)
        val viewPager = view.findViewById<ViewPager>(R.id.now_playing_screen_view_pager)
            ?: throw IllegalStateException("Dialog view must contain a ViewPager with id 'now_playing_screen_view_pager'")
        viewPager.adapter = NowPlayingScreenAdapter(requireContext())
        viewPager.addOnPageChangeListener(this)
        viewPager.pageMargin = ViewUtil.convertDpToPixel(32f, resources).toInt()
        viewPager.currentItem = PreferenceUtil.nowPlayingScreen.ordinal

        return materialDialog(R.string.pref_title_now_playing_screen_appearance)
            .setCancelable(false)
            .setPositiveButton(R.string.set) { _, _ ->
                val nowPlayingScreen = values()[viewPagerPosition]
                if (isNowPlayingThemes(nowPlayingScreen)) {
                    val result =
                        "${getString(nowPlayingScreen.titleRes)} theme is Pro version feature."
                    showToast(result)
                    NavigationUtil.goToProVersion(requireContext())
                } else {
                    PreferenceUtil.nowPlayingScreen = nowPlayingScreen
                }
            }
            .setView(view)
            .create()
            .colorButtons()
    }

    companion object {
        fun newInstance(): NowPlayingScreenPreferenceDialog {
            return NowPlayingScreenPreferenceDialog()
        }
    }
}

private class NowPlayingScreenAdapter(private val context: Context) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val nowPlayingScreen = values()[position]

        val inflater = LayoutInflater.from(context)
        val binding = PreferenceNowPlayingScreenItemBinding.inflate(inflater, collection, true)
        Glide.with(context).load(nowPlayingScreen.drawableResId).into(binding.image)
        binding.title.setText(nowPlayingScreen.titleRes)
        if (isNowPlayingThemes(nowPlayingScreen)) {
            binding.proText.show()
            binding.proText.setText(R.string.pro)
        } else {
            binding.proText.hide()
        }
        return binding.root
    }

    override fun destroyItem(
        collection: ViewGroup,
        position: Int,
        view: Any
    ) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return values().size
    }

    override fun isViewFromObject(view: View, instance: Any): Boolean {
        return view === instance
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(values()[position].titleRes)
    }
}

private fun isNowPlayingThemes(screen: NowPlayingScreen): Boolean {
    return (screen == Full || screen == Card || screen == Plain || screen == Blur || screen == Color || screen == Simple || screen == BlurCard || screen == Circle || screen == Adaptive) && !App.isProVersion()
}
