package com.example.storie.feature.detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.storie.R
import com.example.storie.component.AlertDialogFragment
import com.example.storie.core.utils.DateConstant.MONTH_DAY_YEAR_TIME_HOUR_MINUTE
import com.example.storie.core.utils.parseFormatDate
import com.example.storie.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val mViewModel by viewModels<DetailViewModel>()

    private val detailId by lazy {
        intent.getStringExtra(EXTRA_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
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

        setupData()
        setupObserver()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.apply {
            appBarDetail.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.storyState.collectLatest {
                        binding.apply {
                            tvDetailDescription.text = it.description
                            tvTimestamp.text = getString(
                                R.string.format_created_at,
                                "Unknown"
                            )
                            Glide.with(this@DetailActivity)
                                .load(it.photoUrl)
                                .placeholder(R.drawable.ic_place_holder)
                                .into(ivDetailPhoto)
                            if (it.createdAt.isNotEmpty()) {
                                appBarDetail.toolbar.title = it.name
                                tvTimestamp.text = getString(
                                    R.string.format_created_at,
                                    it.createdAt.parseFormatDate(toFormat = MONTH_DAY_YEAR_TIME_HOUR_MINUTE)
                                )
                                tvDetailName.text = getString(
                                    R.string.format_detail_nama,
                                    it.name
                                )
                            }
                        }

                    }
                }

                launch {
                    mViewModel.viewEffect.collectLatest {
                        observeEffect(it)
                    }
                }

            }
        }
    }

    private fun observeEffect(it: DetailViewEffect) {
        when (it) {
            is DetailViewEffect.OnLoading -> {
                showLoading(true)
            }

            is DetailViewEffect.OnSuccess -> {
                showLoading(false)
            }

            is DetailViewEffect.OnError -> {
                showLoading(false)
                showErrorDialog(it.message)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorDialog(message: String) {
        val dialog = AlertDialogFragment()
        val bundle = Bundle()
        bundle.putString(
            "title",
            "Error"
        )
        bundle.putString(
            "message",
            message
        )
        bundle.putString(
            "positiveButtonTitle",
            "OK"
        )
        dialog.arguments = bundle
        dialog.show(
            supportFragmentManager,
            "error_dialog"
        )
        dialog.setOnPositiveButtonClickListener(object : AlertDialogFragment.ButtonClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })
    }

    private fun setupData() {
        mViewModel.getDetailStory(detailId ?: "")
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}