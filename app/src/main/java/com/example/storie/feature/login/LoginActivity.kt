package com.example.storie.feature.login

import android.content.DialogInterface
import android.content.Intent
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
import com.example.storie.component.AlertDialogFragment
import com.example.storie.databinding.ActivityLoginBinding
import com.example.storie.feature.home.HomeActivity
import com.example.storie.feature.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val mViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        setupObserver()
        setupClickListeners()
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewEffect.collectLatest {
                        observeEffect(it)
                    }
                }
            }
        }
    }

    private fun observeEffect(it: LoginViewEffect) {
        when (it) {
            is LoginViewEffect.OnLoading -> {
                showLoading(true)
            }

            is LoginViewEffect.OnSuccess -> {
                showLoading(false)
                navigateToHome()
            }

            is LoginViewEffect.OnError -> {
                showLoading(false)
                showErrorDialog(it.message)
            }
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.cpiLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToRegister() {
        val intent = Intent(
            this,
            RegisterActivity::class.java
        )
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(
            this,
            HomeActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setupClickListeners() {
        binding.tvLabelDonTHaveAnAccountRegister.setOnClickListener {
            navigateToRegister()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.lytEmail.edLoginEmail.text.toString()
            val password = binding.lytPassword.edLoginPassword.text.toString()
            mViewModel.onEvent(
                LoginViewEvent.Login(
                    email,
                    password
                )
            )
        }
    }
}