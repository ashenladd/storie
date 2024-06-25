package com.example.storie.feature.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storie.R
import com.example.storie.databinding.ActivityHomeBinding
import com.example.storie.domain.model.CarouselModel
import com.example.storie.feature.detail.DetailActivity
import com.example.storie.feature.home.adapter.CarouselAdapter
import com.example.storie.feature.home.adapter.StoriesPagingAdapter
import com.example.storie.feature.login.LoginActivity
import com.example.storie.feature.maps.MapsActivity
import com.example.storie.feature.post.PostActivity
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.HeroCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val mViewModel by viewModels<HomeViewModel>()

    private val mStoriesPagingAdapter: StoriesPagingAdapter by lazy {
        StoriesPagingAdapter()
    }

    private val mCarouselAdapter: CarouselAdapter by lazy {
        CarouselAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                0,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        setupObserver()
        fetchUsername()
        setupCarousel()
        setupAdapter()
        setupToolbar()
        setupClickListeners()
    }

    private fun fetchUsername() {
        mViewModel.onEvent(HomeViewEvent.FetchUsername)
    }

    private fun setupClickListeners() {
        binding.apply {
            fabAddStory.setOnClickListener {
                navigateToPost()
            }
        }
    }

    private fun setupToolbar() {
        binding.apply {
            btnAccount.setOnClickListener {
                showUserMenu(it)
            }
            tbHome.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.setting -> {
                        showSettingMenu(findViewById(R.id.setting))
                        true
                    }

                    R.id.map -> {
                        navigateToMaps()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun navigateToMaps() {
        val intent = Intent(
            this,
            MapsActivity::class.java
        )
        startActivity(intent)
    }

    private fun setupAdapter() {
        binding.apply {
            rvCarousel.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
            rvStories.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            mStoriesPagingAdapter.setOnItemClickListener(object :
                StoriesPagingAdapter.OnItemClickListener {
                override fun onItemClick(id: String) {
                    navigateToDetail(id)
                }
            })

            rvCarousel.adapter = mCarouselAdapter
            rvStories.adapter = mStoriesPagingAdapter
        }
    }

    private fun navigateToDetail(id: String) {
        Intent(
            this,
            DetailActivity::class.java
        ).apply {
            putExtra(
                DetailActivity.EXTRA_ID,
                id
            )
        }.also {
            startActivity(it)
        }
    }

    private fun showUserMenu(v: View) {
        val popup = PopupMenu(
            this,
            v
        )
        popup.menuInflater.inflate(
            R.menu.menu_popup_account,
            popup.menu
        )

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    mViewModel.onEvent(HomeViewEvent.OnLogout)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun showSettingMenu(v: View) {
        val popup = PopupMenu(
            this,
            v
        )
        popup.menuInflater.inflate(
            R.menu.menu_popup_setting,
            popup.menu
        )

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun setupCarousel() {
        val carouselModels = listOf(
            CarouselModel(
                title = getString(R.string.title_carousel_1),
                imageAsset = R.drawable.img_story1
            ),
            CarouselModel(
                title = getString(R.string.title_carousel_2),
                imageAsset = R.drawable.img_story2
            ),
            CarouselModel(
                title = getString(R.string.title_carousel_3),
                imageAsset = R.drawable.img_story3
            ),
        )

        mCarouselAdapter.submitList(carouselModels)
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        binding.tvTitleHome.text = getString(
                            R.string.title_home,
                            it.username
                        )
                    }
                }
                launch {
                    mViewModel.viewEffect.collectLatest {
                        Log.d(
                            "HomeActivity",
                            "setupObserver: $it"
                        )
                        observeEffect(it)
                    }
                }
            }
        }
        mViewModel.storiesPagingData.observe(this@HomeActivity) {
            mStoriesPagingAdapter.submitData(
                lifecycle,
                it
            )
        }
    }


    private fun observeEffect(it: HomeViewEffect) {
        when (it) {
            is HomeViewEffect.OnLoading -> {
                showLoading(true)
                showRetry(false)
            }

            is HomeViewEffect.OnSuccess -> {
                showLoading(false)
                showRetry(false)
            }

            is HomeViewEffect.OnError -> {
                showLoading(false)
            }

            is HomeViewEffect.OnLogout -> {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(
            this,
            LoginActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun navigateToPost() {
        val intent = Intent(
            this,
            PostActivity::class.java
        )
        startActivity(intent)
    }

    private fun showRetry(isError: Boolean) {
        binding.apply {
            lytRetry.root.visibility = if (isError) View.VISIBLE else View.GONE
            lytRetry.btnRetry.setOnClickListener {
                mViewModel.onEvent(HomeViewEvent.OnRetry)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiYourStory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
    }
}