package com.example.rentsafeplacemobile.ui

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
import com.example.rentsafeplacemobile.BuildingViewModel
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.databinding.FragmentRealtorBinding
import kotlinx.coroutines.launch

class RealtorFragment: Fragment() {
    private var _binding: FragmentRealtorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuildingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRealtorBinding.inflate(inflater, container, false)
        val view = binding.root

        val realtorId = arguments?.getLong(KEY_REALTOR_ID)

        if (realtorId != null) {
            viewModel.getRealtor(realtorId)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.realtor.collect { realtor ->
                    if (realtor != null) {
                        binding.textViewName.text = realtor.name
                        binding.textViewEmail.text = resources.getString(R.string.email) + " " +  realtor.email
                        binding.textViewPhone.text = resources.getString(R.string.phone) + " " +  realtor.phone
                        val photo = if (realtor.photo != "") {
                            realtor.photo
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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_REALTOR_ID = "realtor_id"

        fun newInstance(realtorId: Long): RealtorFragment {
            val args = Bundle().apply {
                putLong(KEY_REALTOR_ID, realtorId)
            }
            val fragment = RealtorFragment()
            fragment.arguments = args
            return fragment
        }
    }
}