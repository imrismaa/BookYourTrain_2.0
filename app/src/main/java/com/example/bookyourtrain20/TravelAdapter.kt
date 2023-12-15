package com.example.bookyourtrain20

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookyourtrain20.databinding.ItemTravelBinding

typealias OnClickTravel = (Travel) -> Unit
class TravelAdapter(
    private var listTravel: List<Travel>,
    private val onClickTravel: OnClickTravel) :
    RecyclerView.Adapter<TravelAdapter.ItemTravelViewHolder>() {

    inner class ItemTravelViewHolder(private val binding: ItemTravelBinding) :
        RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(travel: Travel) {
                with(binding) {
                    txtDeparture.text = "Destination\n${travel.destination}"
                    txtDestination.text = "Departure\n${travel.departure}"
                    itemView.setOnClickListener {
                        onClickTravel(travel)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTravelViewHolder {
        val binding = ItemTravelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemTravelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTravel.size
    }

    override fun onBindViewHolder(holder: ItemTravelViewHolder, position: Int) {
        holder.bind(listTravel[position])
    }
}