package com.example.pokemon.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.R
import com.example.pokemon.data.ObjectPokemon
import java.net.BindException
import java.util.Locale

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.PokemonListAdapterViewHolder>(), Filterable {
    private var clickListener: ClickListener? = null

    private var lData = ArrayList<ObjectPokemon>()

    @SuppressLint("NotifyDataSetChanged")
    fun setLData(lData: ArrayList<ObjectPokemon>) {
        this.lData.clear()
        this.lData.addAll(lData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pokemon_layout, parent, false)
        return PokemonListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lData.size
    }

    override fun onBindViewHolder(holder: PokemonListAdapterViewHolder, position: Int) {
        holder.Bind(position)
    }

    override fun getFilter(): Filter {
       return object : Filter() {
           override fun performFiltering(constrain: CharSequence?): FilterResults {
               var seacrhChar = constrain.toString()
               if (!seacrhChar.isEmpty() || !seacrhChar.isNullOrEmpty()) {
                   val resulList = ArrayList<ObjectPokemon>()
                   for (item in lData) {
                       if (item.namePokemon.lowercase(Locale.ROOT).contains(seacrhChar.lowercase(Locale.ROOT))) resulList.add(item)
                   }
                   lData = resulList
               }
               var filterResult = FilterResults()
               filterResult.values = lData
               return filterResult
           }

           override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
               lData = p1?.values as ArrayList<ObjectPokemon>
               notifyDataSetChanged()
           }

       }
    }

    inner class PokemonListAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val Tittleval: TextView
        private val clPokeList: ConstraintLayout
        private val cvPokeList: CardView

        init {
            Tittleval = itemview.findViewById(R.id.tvvl_namepokemon)
            clPokeList = itemview.findViewById(R.id.cl_apterpoke)
            cvPokeList = itemview.findViewById(R.id.cv_pokeapter)
        }
        fun Bind(position: Int) {
            val obj = lData[position]
            Tittleval.text = obj.namePokemon.toUpperCase()

            Tittleval.setTextColor(Color.BLACK)

            cvPokeList.setOnClickListener {
                clickListener?.OnItemListClick(obj)
            }
        }
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    open class ClickListener : IFaceClickListener {
        override fun OnItemListClick(dtOrd: ObjectPokemon) {

        }

        override fun OnBtnProcClick() {

        }

    }

    protected interface IFaceClickListener {
        fun OnItemListClick(dtOrd: ObjectPokemon)
        fun OnBtnProcClick()
    }
}