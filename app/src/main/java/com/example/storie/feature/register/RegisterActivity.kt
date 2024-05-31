package com.example.storie.feature.register

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
import com.example.storie.databinding.ActivityRegisterBinding
import com.example.storie.feature.LoginSelectorActivity
import com.example.storie.feature.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val mViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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

    private fun observeEffect(it: RegisterViewEffect) {
        when (it) {
            is RegisterViewEffect.OnLoading -> {
                showLoading(true)
            }

            is RegisterViewEffect.OnSuccess -> {
                showLoading(false)
                showSuccessDialog(it.message)
            }

            is RegisterViewEffect.OnError -> {
                showLoading(false)
                showErrorDialog(it.message)
            }
        }
    }

    private fun showSuccessDialog(message: String) {
        val dialog = AlertDialogFragment()
        val bundle = Bundle()
        bundle.putString(
            "title",
            "Success"
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
            "success_dialog"
        )
        dialog.setOnPositiveButtonClickListener(object : AlertDialogFragment.ButtonClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                navigateToLoginSelector()
            }
        })
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
        binding.cpiRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        val intent = Intent(
            this,
            LoginActivity::class.java
        )
        startActivity(intent)
    }

    private fun navigateToLoginSelector() {
        val intent = Intent(
            this,
            LoginSelectorActivity::class.java
        )
        startActivity(intent)
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val nama = binding.lytUsername.edRegisterName.text.toString()
            val email = binding.lytEmail.edRegisterEmail.text.toString()
            val password = binding.lytPassword.edRegisterPassword.text.toString()
            mViewModel.onEvent(
                RegisterViewEvent.Register(
                    nama,
                    email,
                    password
                )
            )
        }
        binding.tvLabelAlreadyHaveAccountRegister.setOnClickListener {
            navigateToLogin()
        }
    }
}