package com.example.storie.feature

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storie.core.DataStoreManager
import com.example.storie.databinding.ActivityLoginSelectorBinding
import com.example.storie.feature.home.HomeActivity
import com.example.storie.feature.login.LoginActivity
import com.example.storie.feature.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginSelectorActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private lateinit var binding: ActivityLoginSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginSelectorBinding.inflate(layoutInflater)
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

        setupClickListeners()
        playAnimation()
    }

    override fun onStart() {
        super.onStart()

        val applicationScope = CoroutineScope(Dispatchers.IO)
        applicationScope.launch {
            val isLoggedIn = dataStoreManager.isLoggedIn().first()
            if (isLoggedIn) {
                val intent = Intent(
                    this@LoginSelectorActivity,
                    HomeActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

    private fun playAnimation() {
        val x = ObjectAnimator.ofFloat(
            binding.ivBlogging,
            "scaleX",
            1f,
            1.1f
        ).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val y = ObjectAnimator.ofFloat(
            binding.ivBlogging,
            "scaleY",
            1f,
            1.1f
        ).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val title = ObjectAnimator.ofFloat(
            binding.tvHeadline,
            View.ALPHA,
            1f
        ).setDuration(500)
        val desc = ObjectAnimator.ofFloat(
            binding.tvBody,
            View.ALPHA,
            1f
        ).setDuration(500)

        AnimatorSet().apply {
            playTogether(
                x,
                y
            )
            playSequentially(
                title,
                desc
            )
            start()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(
            this,
            LoginActivity::class.java
        )
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(
            this,
            RegisterActivity::class.java
        )
        startActivity(intent)
    }

    private fun setupClickListeners() {
        binding.btnSignin.setOnClickListener {
            navigateToLogin()
        }
        binding.btnSignup.setOnClickListener {
            navigateToRegister()
        }
    }
}