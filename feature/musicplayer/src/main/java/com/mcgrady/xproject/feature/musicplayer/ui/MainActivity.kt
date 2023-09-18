package com.mcgrady.xproject.feature.musicplayer.ui


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mcgrady.xproject.core.extensions.getBottomInsets
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_SETTLING
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseActivity
import com.mcgrady.xproject.core.extensions.dip
import com.mcgrady.xproject.core.extensions.whichFragment
import com.mcgrady.xproject.feature.musicplayer.ui.base.BaseMediaPlayerFragment
import com.mcgrady.xproject.feature.musicplayer.R
import com.mcgrady.xproject.feature.musicplayer.databinding.MusicplayerActivityMainBinding
import com.mcgrady.xproject.feature.musicplayer.extensions.hide
import com.mcgrady.xproject.feature.musicplayer.extensions.peekHeightAnimate
import com.mcgrady.xproject.feature.musicplayer.extensions.show
import com.mcgrady.xproject.feature.musicplayer.ui.player.PlayerBottomSheetFragment
import com.mcgrady.xproject.feature.musicplayer.ui.player.MusicPlayerControllerFragment
import com.therouter.router.Route
import timber.log.Timber

@Route(path = "feature/musicplayer/main")
class MainActivity : BaseActivity() {

    private val binding: MusicplayerActivityMainBinding by viewBinding(MusicplayerActivityMainBinding::inflate)

    private lateinit var navController: NavController
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val panelState: Int
        get() = bottomSheetBehavior.state

    private var miniPlayerFragment: PlayerBottomSheetFragment? = null
    private var playerFragment: BaseMediaPlayerFragment? = null
    private var windowInsets: WindowInsetsCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {

            root.setOnApplyWindowInsetsListener { v, insets ->
                windowInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
                insets
            }
            supportFragmentManager.commit {
                replace(R.id.fl_player_container, MusicPlayerControllerFragment())
            }
            supportFragmentManager.executePendingTransactions()
            playerFragment = whichFragment<MusicPlayerControllerFragment>(R.id.fl_player_container)
//            miniPlayerFragment = whichFragment<MusicMiniPlayerFragment>(R.id.mini_player_fragment)

            bottomSheetBehavior = BottomSheetBehavior.from(binding.slidingPanel).apply {
                addBottomSheetCallback(bottomSheetCallback)
                isHideable = false
                significantVelocityThreshold = 300
            }

            setupNavigationController()

//            binding.slidingPanel.backgroundTintList = ColorStateList.valueOf(darkAccentColor())
//            binding.navView.backgroundTintList = ColorStateList.valueOf(darkAccentColor())
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onResume() {
        super.onResume()

        if (bottomSheetBehavior.state == STATE_EXPANDED) {
            setMiniPlayerAlphaProgress(1f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
    }

    private fun setupNavigationController() {
        //1
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment? ?: return
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)*/

        //2
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_download -> {
                    setBottomNavVisibility(visible = true, animate = false)
                }
            }
        }

        //3
        binding.slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)

                binding.slidingPanel.updateLayoutParams<ViewGroup.LayoutParams> {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }

                when (panelState) {
                    STATE_EXPANDED -> onPanelExpanded()
                    STATE_COLLAPSED -> onPanelCollapsed()
                }
            }
        })

//        val navController = findNavController(R.id.fragment_container_view)
//        val navInflater = navController.navInflater
//        val navGraph = navInflater.inflate(R.navigation.mobile_navigation)
//        navController.graph = navGraph
//        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
        //3
//        return findNavController(R.id.nav_host_fragment_container).navigateUp()
    }

    private fun collapsePanel() {
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

    fun expandPanel() {
        bottomSheetBehavior.state = STATE_EXPANDED
    }

    private fun handleBackPress(): Boolean {
        if (panelState == STATE_EXPANDED) {
            collapsePanel()
            return true
        }
        return false
    }



    open fun onPanelCollapsed() {
        setMiniPlayerAlphaProgress(0F)
        // restore values
//        animateNavigationBarColor(surfaceColor())
//        setLightStatusBarAuto()
//        setLightNavigationBarAuto()
//        setTaskDescriptionColor(taskColor)
        //playerFragment?.onHide()
    }

    open fun onPanelExpanded() {
        setMiniPlayerAlphaProgress(1F)
//        onPaletteColorChanged()
        //playerFragment?.onShow()
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (progress < 0) return
        val alpha = 1 - progress
        miniPlayerFragment?.view?.alpha = 1 - (progress / 0.2F)
        miniPlayerFragment?.view?.isGone = alpha == 0f
//        if (!isLandscape) {
            binding.navView.translationY = progress * 500
            binding.navView.alpha = alpha
//        }
        binding.flPlayerContainer.alpha = (progress - 0.2F) / 0.2F
    }

    fun setBottomNavVisibility(
        visible: Boolean,
        animate: Boolean = false,
        hideBottomSheet: Boolean = false,
    ) {
        if (visible xor binding.navView.isVisible) {
            val mAnimate = animate && bottomSheetBehavior.state == STATE_COLLAPSED
            if (mAnimate) {
                if (visible) {
                    binding.navView.bringToFront()
                    binding.navView.show()
                } else {
                    binding.navView.hide()
                }
            } else {
                binding.navView.isVisible = visible
                if (visible && bottomSheetBehavior.state != STATE_EXPANDED) {
                    binding.navView.bringToFront()
                }
            }
        }
        hideBottomSheet(
            hide = hideBottomSheet,
            animate = animate,
            isBottomNavVisible = visible && binding.navView is BottomNavigationView
        )
    }

    fun hideBottomSheet(
        hide: Boolean,
        animate: Boolean = false,
        isBottomNavVisible: Boolean = binding.navView.isVisible && binding.navView is BottomNavigationView,
    ) {
        val heightOfBar = windowInsets.getBottomInsets() + dip(R.dimen.mini_player_height)
        val heightOfBarWithTabs = heightOfBar + dip(R.dimen.bottom_nav_height)
        if (hide) {
            bottomSheetBehavior.peekHeight = -windowInsets.getBottomInsets()
            bottomSheetBehavior.state = STATE_COLLAPSED
//            libraryViewModel.setFabMargin(
//                this,
//                if (isBottomNavVisible) dip(R.dimen.bottom_nav_height) else 0
//            )
        } else {
//            if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
                binding.slidingPanel.elevation = 0F
                binding.navView.elevation = 5F
                if (isBottomNavVisible) {
                    Timber.d("List")
                    if (animate) {
                        bottomSheetBehavior.peekHeightAnimate(this, heightOfBarWithTabs)
                    } else {
                        bottomSheetBehavior.peekHeight = heightOfBarWithTabs
                    }
//                    libraryViewModel.setFabMargin(
//                        this,
//                        dip(R.dimen.bottom_nav_mini_player_height)
//                    )
//                } else {
//                    logD("Details")
//                    if (animate) {
//                        bottomSheetBehavior.peekHeightAnimate(this, heightOfBar).doOnEnd {
//                            binding.slidingPanel.bringToFront()
//                        }
//                    } else {
//                        bottomSheetBehavior.peekHeight = heightOfBar
//                        binding.slidingPanel.bringToFront()
//                    }
//                    libraryViewModel.setFabMargin(this, dip(R.dimen.mini_player_height))
//                }
            }
        }
    }

    private val onBackPressedCallback by lazy {
        object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.d("Handle back press ${bottomSheetBehavior.state}")
                if (!handleBackPress()) {
                    remove()
                    onBackPressedDispatcher.onBackPressed()
                }
            }

        }
    }

    private val bottomSheetCallback by lazy {
        object: BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                onBackPressedCallback.isEnabled = newState == STATE_EXPANDED
                when (newState) {
                    STATE_EXPANDED -> {
                        onPanelExpanded()
//                        if (PreferenceUtil.lyricsScreenOn && PreferenceUtil.showLyrics) {
//                            keepScreenOn(true)
//                        }
                    }

                    STATE_COLLAPSED -> {
                        onPanelCollapsed()
//                        if ((PreferenceUtil.lyricsScreenOn && PreferenceUtil.showLyrics) || !PreferenceUtil.isScreenOnEnabled) {
//                            keepScreenOn(false)
//                        }
                    }

                    STATE_SETTLING, STATE_DRAGGING -> {
//                        if (fromNotification) {
                            binding.navView.bringToFront()
//                            fromNotification = false
//                        }
                    }

                    STATE_HIDDEN -> {
//                        MusicPlayerRemote.clearQueue()
                    }

                    else -> {
                        Timber.d("Do a flip")
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setMiniPlayerAlphaProgress(slideOffset)

            }

        }
    }
}