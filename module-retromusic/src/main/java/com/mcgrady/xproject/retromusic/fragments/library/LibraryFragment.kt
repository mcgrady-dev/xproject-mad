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
package com.mcgrady.xproject.retromusic.fragments.library

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.cast.framework.CastButtonFactory
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.FragmentLibraryBinding
import com.mcgrady.xproject.retromusic.dialogs.CreatePlaylistDialog
import com.mcgrady.xproject.retromusic.dialogs.ImportPlaylistDialog
import com.mcgrady.xproject.retromusic.extensions.whichFragment
import com.mcgrady.xproject.retromusic.fragments.base.AbsMainActivityFragment
import com.mcgrady.xproject.retromusic.model.CategoryInfo
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.ThemeStore
import com.mcgrady.xproject.theme.common.ATHToolbarActivity.getToolbarBackgroundColor
import com.mcgrady.xproject.theme.util.ToolbarContentTintHelper

class LibraryFragment : AbsMainActivityFragment(R.layout.fragment_library) {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLibraryBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity.setBottomNavVisibility(true)
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.title = null
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(
                R.id.action_search,
                null,
                navOptions
            )
        }
        setupNavigationController()
        setupTitle()
    }

    private fun setupTitle() {
        val color = ThemeStore.accentColor(requireContext())
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        val appName = "Retro <span  style='color:$hexColor';>Music</span>".parseAsHtml()
        binding.appNameText.text = appName
    }

    private fun setupNavigationController() {
        val navHostFragment = whichFragment<NavHostFragment>(R.id.fragment_container)
        val navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.library_graph)

        val categoryInfo: CategoryInfo = PreferenceUtil.libraryCategory.first { it.visible }
        if (categoryInfo.visible) {
            navGraph.setStartDestination(categoryInfo.category.id)
        }
        navController.graph = navGraph
        NavigationUI.setupWithNavController(mainActivity.navigationView, navController)
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.appBarLayout.setExpanded(true, true)
        }
    }

    override fun onPrepareMenu(menu: Menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), binding.toolbar)
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            binding.toolbar,
            menu,
            getToolbarBackgroundColor(binding.toolbar)
        )
        // Setting up cast button
        CastButtonFactory.setUpMediaRouteButton(requireContext(), menu, R.id.action_cast)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController().navigate(
                R.id.settings_fragment,
                null,
                navOptions
            )
            R.id.action_import_playlist -> ImportPlaylistDialog().show(
                childFragmentManager,
                "ImportPlaylist"
            )
            R.id.action_add_to_playlist -> CreatePlaylistDialog.create(emptyList()).show(
                childFragmentManager,
                "ShowCreatePlaylistDialog"
            )
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
