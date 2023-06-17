package com.example.rentsafeplacemobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.rentsafeplacemobile.ApiService
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.RetrofitClient
import com.example.rentsafeplacemobile.data.Tenant
import com.example.rentsafeplacemobile.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment: Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val apiService: ApiService = RetrofitClient.apiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonRegister.setOnClickListener {
            registerUser()
        }

        return view
    }

    private fun registerUser() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhone.text.toString()

        lifecycleScope.launch {
            try {
                val response = apiService.register(Tenant(null, email, password, name, phone, null))
                if (response.isSuccessful) {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayout, LoginFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    Toast.makeText(requireContext(), resources.getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                } else {

                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}