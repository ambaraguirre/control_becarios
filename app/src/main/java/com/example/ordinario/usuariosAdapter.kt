package com.example.ordinario

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ordinario.databinding.ItemBinding


class usuariosAdapter(private val context: Context, private val listUsuarios: List<Usuarios>, private var optionsMenuItemClickListener: OptionsMenuClickListener):RecyclerView.Adapter<usuariosAdapter.ViewHolder>() {


    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    inner class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the single_item view
        // that is used to hold list item
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(listUsuarios[position]){
                //holder.imageView.setImageResource(ItemsViewModel.imagen)
                // sets the text to the textview from our itemHolder class
                binding.nombre.text = this.nombre
                // sets the text to the textview from our itemHolder class
                binding.correo.text= this.correo
                binding.textViewOptions.setOnClickListener{
                    optionsMenuItemClickListener.onOptionsMenuClicked(position)
                }
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listUsuarios.size
    }
}