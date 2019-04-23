package com.example.masterdex.Adapters

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.masterdex.Entities.Pokemon
import com.example.masterdex.R
import kotlinx.android.synthetic.main.item_list.view.*
import org.json.JSONObject

class PokemonAdapter(var items: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonHolder>(), View.OnClickListener {

    private lateinit var listener: View.OnClickListener

    override fun onClick(v: View?) {
        if (listener != null){
            listener.onClick(v)
        }
    }

    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        view.setOnClickListener(this)

        return PokemonHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {

        holder.bind(items[position])

    }

    fun setList(items:List<Pokemon>) {
        this.items = items
        notifyDataSetChanged()
    }
    class PokemonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Pokemon) = with(itemView){
            pokemonId.text = "Pokédex #"+item.id
            pokemonName.text = "Name: "+item.name.capitalize()
            if(item.id != "Empty") {
                Glide.with(this.context).load(JSONObject(item.data).getJSONObject("sprites").getString("front_default")).into(Imagen)
            }
            tag = item.id
        }

    }

}