package com.example.pokemonhanbook.Adpaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pokemonhanbook.R
import me.sargunvohra.lib.pokekotlin.model.Pokemon


class ItemAdapter(val context: Context, val itemList: ArrayList<Pokemon>) : BaseAdapter() {

    private var mIPokemonItemSelectedListener:IPokemonSelected ?= null

    interface IPokemonSelected {
        /**
         * Called when an item is added.
         *
         */
        fun onPokemonSelected(fso :Pokemon)
    }

    /**
     * Sets the listener that should be notified of button clicked events.
     *
     * @param listener the listener to notify.
     */
    private lateinit var listener:IPokemonSelected

    fun setOnPokemonSelectedListener(listener: IPokemonSelected ){
        mIPokemonItemSelectedListener = listener
    }




    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder

        // Instantiate the NoticeDialogListener so we can send events to the host


        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.pokemon_listview_item, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
//

        listener = context as IPokemonSelected

        vh.tvPokemonName.text = itemList[position].name
        Glide.with(context).load(itemList[position].sprites.frontShiny).into(vh.ivProductImage)

        return view
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }
}


private class ViewHolder(view: View?) {
    val ivProductImage: ImageView = view?.findViewById(R.id.iv_Pokemon) as ImageView
    val tvPokemonName: TextView = view?.findViewById(R.id.tv_pokemonName) as TextView


}
