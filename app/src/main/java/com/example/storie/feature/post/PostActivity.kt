package com.example.storie.feature.post

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.storie.core.utils.reduceFileImage
import com.example.storie.core.utils.uriToFile
import com.example.storie.databinding.ActivityPostBinding
import com.example.storie.feature.post.CameraActivity.Companion.CAMERAX_RESULT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    private val mViewModel by viewModels<PostViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    "Permission request granted",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Permission request denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            mViewModel.onEvent(PostViewEvent.OnPickMedia(uri))
        } else {
            Log.d(
                "Photo Picker",
                "No media selected"
            )
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            val uri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (uri != null) {
                mViewModel.onEvent(PostViewEvent.OnPickMedia(uri))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        setupPermission()
        setupToolbar()
        setupClickListeners()
        setupObserver()
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        it.uri?.let { uri -> showImage(uri) }
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

    private fun startCamera() {
        val intent = Intent(
            this,
            CameraActivity::class.java
        )
        launcherIntentCameraX.launch(intent)
    }

    private fun observeEffect(it: PostViewEffect) {
        when (it) {
            is PostViewEffect.OnLoading -> {
                showLoading(true)
            }

            is PostViewEffect.OnSuccess -> {
                showLoading(false)
                onBackPressedDispatcher.onBackPressed()
            }

            is PostViewEffect.OnError -> {
                showLoading(false)
                Toast.makeText(
                    this,
                    "Post failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiPost.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnPhoto.setOnClickListener {
                startCamera()
            }
            buttonAdd.setOnClickListener {
                val uri = mViewModel.viewState.value.uri
                if (uri != null) {
                    mViewModel.onEvent(
                        PostViewEvent.OnUpload(
                            file = uriToFile(
                                uri,
                                this@PostActivity
                            ).reduceFileImage(),
                            description = lytPostContent.edAddDescription.text.toString()
                        )
                    )
                } else {
                    Toast.makeText(
                        this@PostActivity,
                        "Please select an image",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setupPermission() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showImage(uri: Uri) {
        binding.ivPostImage.setImageURI(uri)
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}