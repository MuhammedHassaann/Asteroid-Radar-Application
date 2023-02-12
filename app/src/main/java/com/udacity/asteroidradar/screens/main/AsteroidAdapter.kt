package com.udacity.asteroidradar.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidListElementBinding
import com.udacity.asteroidradar.models.Asteroid

class AsteroidAdapter : ListAdapter<Asteroid,
        AsteroidAdapter.AsteroidViewHolder>(AsteroidDiffCallBack()) {


    //(Asteroid) -> Unit used as a callback,
    //where the function is called in response to a click on an item in the RecyclerView
    var onItemClick: ((Asteroid) -> Unit)? = null

    //The AsteroidViewHolder class holds the views for a single item in the RecyclerView.
    class AsteroidViewHolder(
        private val binding: AsteroidListElementBinding)
        :RecyclerView.ViewHolder(binding.root) {

        //data in the Asteroid object is bound to the views in the item layout
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            //The executePendingBindings method ensures that the bindings are executed immediately.
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val asteroidListElementBinding=AsteroidListElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return AsteroidViewHolder(asteroidListElementBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        //The getItem method is used to retrieve the Asteroid object at the specified position.
        val asteroid =getItem(position)
        holder.bind(asteroid = asteroid)

        //setting an OnClickListener on the itemView of the ViewHolder
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(asteroid)
        }
    }

}

//The DiffCallback class is used to calculate the differences between two data sets
class AsteroidDiffCallBack: DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }

}