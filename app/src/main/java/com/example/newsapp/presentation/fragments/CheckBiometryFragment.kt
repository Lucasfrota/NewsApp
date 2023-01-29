package com.example.newsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentCheckBiometryBinding
import java.util.concurrent.Executor

class CheckBiometryFragment : Fragment() {

    private lateinit var binding: FragmentCheckBiometryBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_check_biometry,
            container,
            false)

        setUpBiometry()

        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricPrompt.authenticate(promptInfo)
            else -> {
                navigateToHeadlinesFragment()
            }
        }

        binding.checkBiometry.setOnClickListener { biometricPrompt.authenticate(promptInfo) }

        return binding.root
    }

    private fun setUpBiometry(){
        executor = ContextCompat.getMainExecutor(this.requireContext())
        biometricPrompt = BiometricPrompt(this, executor, biometricPromptCallback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(requireContext().getString(R.string.biometry_title))
            .setSubtitle(requireContext().getString(R.string.biometry_subtitle))
            .setNegativeButtonText(requireContext().getString(R.string.biometry_cancel))
            .build()
    }

    private fun navigateToHeadlinesFragment(){
        findNavController().navigate(R.id.action_checkBiometryFragment_to_headlineFragment)
    }

    private val biometricPromptCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(
            errorCode: Int,
            errString: CharSequence
        ) {
            super.onAuthenticationError(errorCode, errString)
            binding.message = requireContext().getString(R.string.biometry_request_warning)
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult
        ) {
            super.onAuthenticationSucceeded(result)
            navigateToHeadlinesFragment()
        }
    }
}