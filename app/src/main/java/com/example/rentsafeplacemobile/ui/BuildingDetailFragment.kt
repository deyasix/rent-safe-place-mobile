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
import com.bumptech.glide.Glide
import com.example.rentsafeplacemobile.BuildingViewModel
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.databinding.FragmentBuildingDetailBinding
import kotlinx.coroutines.launch

class BuildingDetailFragment : Fragment() {
    private var _binding: FragmentBuildingDetailBinding? = null
    private val binding get() = _binding!!
    private var realtorId: Long? = null

    private val viewModel: BuildingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBuildingDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val buildingId = arguments?.getLong(KEY_BUILDING_ID)

        if (buildingId != null) {
            viewModel.getBuildingDetails(buildingId)
        }

        binding.buttonRealtor.setOnClickListener {
            realtorId?.let {
                val realtorFragment = RealtorFragment.newInstance(it)
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.frameLayout,realtorFragment) // Замінити фрагмент у контейнері на BuildingDetailFragment
                transaction.addToBackStack(null) // Додати транзакцію в стек відстеження назад
                transaction.commit()
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.buildingDetails.collect { building ->
                    if (building != null) {
                        realtorId = building.realtor.id
                        // Встановити дані про приміщення у відповідні елементи UI
                        binding.textViewName.text = building.name
                        binding.textViewType.text = resources.getString(R.string.type) + " " + building.type
                        binding.textViewSquare.text = resources.getString(R.string.square) + " " +  building.square.toString()
                        binding.textViewPrice.text = resources.getString(R.string.price) + " " + building.price.toString() + resources.getString(R.string.money)
                        binding.textViewPets.text = resources.getString(R.string.pets_allowed) + " " + if (building.isPetAllowed) resources.getString(R.string.yes)  else resources.getString(R.string.no)
                        binding.textViewDescription.text = resources.getString(R.string.description) + " " + building.description
                        binding.textViewCategory.text = resources.getString(R.string.category) + " " + building.category
                        binding.textViewCity.text = resources.getString(R.string.city) + " " + building.city
                        binding.textViewAddress.text = resources.getString(R.string.address) + " " + building.address
                        binding.textViewRealtor.text = resources.getString(R.string.realtor) + " " + building.realtor.name
                        building.photo?.let { photo ->
                            Glide.with(requireContext())
                                .load(photo)
                                .into(binding.imageView)
                        }
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
        private const val KEY_BUILDING_ID = "building_id"

        fun newInstance(buildingId: Long): BuildingDetailFragment {
            val args = Bundle().apply {
                putLong(KEY_BUILDING_ID, buildingId)
            }
            val fragment = BuildingDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
