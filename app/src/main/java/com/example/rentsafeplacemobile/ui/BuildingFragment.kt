package com.example.rentsafeplacemobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentsafeplacemobile.databinding.FragmentBuildingBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rentsafeplacemobile.BuildingAdapter
import com.example.rentsafeplacemobile.BuildingViewModel
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.data.Building
import kotlinx.coroutines.launch

class BuildingFragment : Fragment(), BuildingAdapter.BuildingItemClickListener {
    private var _binding: FragmentBuildingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BuildingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBuildingBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()
        setupButton()
        viewModel.uiState.observe(viewLifecycleOwner) { newData ->
            (binding.recyclerView.adapter as? BuildingAdapter)?.updateData(newData)
        }
        return view
    }


    private fun setupRecyclerView() {
        val buildingAdapter = BuildingAdapter(mutableListOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = buildingAdapter
        }
        buildingAdapter.setItemClickListener(this)

    }
    private fun setupButton() {
        binding.buttonGetPrice.setOnClickListener {
            val city = binding.textInputEditTextCity.text.toString()
            viewModel.getStatistics(city)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.averagePriceState.collect { averagePrice ->
                    binding.textViewPrice.text = averagePrice?.toString() ?: resources.getString(R.string.unknown)
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBuildingClicked(building: Building) {
        val buildingId = building.id
        val buildingDetailFragment = BuildingDetailFragment.newInstance(buildingId)
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, buildingDetailFragment) // Замінити фрагмент у контейнері на BuildingDetailFragment
        transaction.addToBackStack(null) // Додати транзакцію в стек відстеження назад
        transaction.commit()
    }
}