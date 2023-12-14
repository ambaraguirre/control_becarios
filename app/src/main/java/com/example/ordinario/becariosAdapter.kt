package com.example.ordinario

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ordinario.databinding.ItemBecariosBinding


class becariosAdapter(private val context: Context, private val listBecarios: List<Becarios>, private var optionsMenuItemClickListener: OptionsMenuClickListener):
    RecyclerView.Adapter<becariosAdapter.ViewHolder>() {


    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    inner class ViewHolder(val binding: ItemBecariosBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the single_item view
        // that is used to hold list item
        val binding = ItemBecariosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(listBecarios[position]){
                //holder.imageView.setImageResource(ItemsViewModel.imagen)
                // sets the text to the textview from our itemHolder class
                Glide.with(context).load(this.imagen).into(binding.imgBecario)
                binding.nombre.text = this.nombre
                binding.cuenta.text = this.nocuenta
                // sets the text to the textview from our itemHolder class

                binding.textViewOptions.setOnClickListener{
                    optionsMenuItemClickListener.onOptionsMenuClicked(position)
                }
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listBecarios.size
    }
}