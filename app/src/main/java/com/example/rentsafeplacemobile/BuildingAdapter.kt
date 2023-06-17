package com.example.rentsafeplacemobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rentsafeplacemobile.data.Building
import com.example.rentsafeplacemobile.databinding.BuildingItemBinding

class BuildingAdapter(private val buildings: MutableList<Building>) : RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder>() {

    private var itemClickListener: BuildingItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val binding = BuildingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuildingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val room = buildings[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return buildings.size
    }

    fun updateData(newData: List<Building>) {
        buildings.clear()
        buildings.addAll(newData)
        notifyDataSetChanged()
    }

    inner class BuildingViewHolder(private val binding: BuildingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(building: Building) {
            with(binding) {
                textViewBuildingName.text = building.name
                textViewBuildingDescription.text = building.description
                textViewBuildingPrice.text = building.price.toString() + binding.root.context.resources.getString(R.string.money)
                imageViewPhotoItem.load(building.photo)
                itemView.setOnClickListener {
                    itemClickListener?.onBuildingClicked(building)
                }
            }
        }

    }

    fun setItemClickListener(listener: BuildingItemClickListener) {
        itemClickListener = listener
    }

    interface BuildingItemClickListener {
        fun onBuildingClicked(building: Building)
    }
}