package com.example.rentsafeplacemobile.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.rentsafeplacemobile.ApiService
import com.example.rentsafeplacemobile.LoginData
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.RetrofitClient
import com.example.rentsafeplacemobile.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val apiService: ApiService = RetrofitClient.apiService
    var onLoginSuccessListener: OnLoginSuccessListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val spannableString = SpannableString(binding.textViewRegister.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)

        binding.textViewRegister.text = spannableString

        binding.buttonLogin.setOnClickListener {
            loginUser()
        }

        binding.textViewRegister.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, RegisterFragment())
            transaction.addToBackStack(null) // Додати транзакцію в стек відстеження назад
            transaction.commit()
        }

        return view
    }

    private fun loginUser() {
        val email = binding.textInputEditTextEmail.text.toString()
        val password = binding.textInputEditTextPassword.text.toString()

        lifecycleScope.launch {
            try {
                val response = apiService.login(LoginData(email, password))
                if (response.isSuccessful) {
                    RetrofitClient.setAuthCredentials(email, password)
                    onLoginSuccessListener?.onLoginSuccess()
                }
            } catch (_: Exception) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    interface OnLoginSuccessListener {
        fun onLoginSuccess()
    }
}