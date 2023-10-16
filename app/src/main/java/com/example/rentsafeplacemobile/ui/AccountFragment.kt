package com.example.rentsafeplacemobile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.rentsafeplacemobile.InfoViewModel
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.RetrofitClient
import com.example.rentsafeplacemobile.databinding.FragmentAccountBinding
import kotlinx.coroutines.launch
import java.util.Locale


class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InfoViewModel by viewModels()
    var listener: OnLanguageChangedListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLanguageChangedListener) {
            listener = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.getInfo()

        binding.buttonEdit.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, EditAccountFragment()) // Замінити фрагмент у контейнері на BuildingDetailFragment
            transaction.addToBackStack(null) // Додати транзакцію в стек відстеження назад
            transaction.commit()
        }

        binding.buttonLogout.setOnClickListener {
            RetrofitClient.logout()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, LoginFragment()) // Замінити фрагмент у контейнері на BuildingDetailFragment
            transaction.addToBackStack(null) // Додати транзакцію в стек відстеження назад
            transaction.commit()
        }

        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            val languageCode = if (isChecked) {
                "uk"
            } else {
                "en"
            }
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val resources = requireActivity().resources
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
            setUI()
            binding.buttonEdit.text = resources.getString(R.string.edit)
            binding.buttonChangePassword.text = resources.getString(R.string.change_password)
            binding.buttonLogout.text = resources.getString(R.string.logout)
            binding.buttonDeleteAccount.text = resources.getString(R.string.delete_account)
        }

        setUI()
        return view
    }

    fun setUI(){
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.info.collect { tenant ->
                    if (tenant != null) {
                        binding.textViewName.text = resources.getString(R.string.name) + " " + tenant.name
                        binding.textViewEmail.text = resources.getString(R.string.email) + " " + tenant.email
                        binding.textViewPhone.text = resources.getString(R.string.phone) + " " + tenant.phone
                        val photo = if (tenant.photo != null && tenant.photo != "") {
                            tenant.photo
                        } else {
                            "https://firebasestorage.googleapis.com/v0/b/rent-safe-place.appspot.com/o/images%2Fno-user-image-icon-27.jpg?alt=media&token=4de8c976-97da-4fa4-8799-96ae82c20803"
                        }
                        binding.imageViewAvatar.load(photo) { transformations(
                            CircleCropTransformation()
                        ) }
                    }
                }
            }
        }
        listener?.onLanguageChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    interface OnLanguageChangedListener {
        fun onLanguageChanged()
    }
}