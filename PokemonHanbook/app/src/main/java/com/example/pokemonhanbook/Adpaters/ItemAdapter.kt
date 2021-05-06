package com.example.pokemonhanbook.Adpaters

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.marginBottom
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

        //POPULATE VIEWS ACCORDINGLY HERE SETTING UP THE TEXTVIEWS
        vh.llTypes.removeAllViews()
        itemList[position].types.forEach { x->
            val textViewAbility = TextView(context)
            val llps = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            )
            llps.setMargins(0,0,0,3)
            textViewAbility.layoutParams =llps
            textViewAbility.setPadding(3,3,3,3)
            textViewAbility.setTextColor(Color.WHITE)
            textViewAbility.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            textViewAbility.setBackgroundResource(context.resources.getIdentifier(x.type.name, "drawable", context.packageName))
            textViewAbility.text = String.format("%s%s" , x.type.name[0].toUpperCase(), x.type.name.substring(1))
            vh.llTypes.addView(textViewAbility)
        }

        vh.tvPokemonName.text = String.format("%s%s%s",itemList[position].id.toString(), itemList[position].name[0].toUpperCase(),itemList[position].name.substring(1))
        Glide.with(context).load(itemList[position].sprites.frontDefault).into(vh.ivProductImage)
//        Glide.with(context).load(context.resources.getIdentifier("icon"+itemList[position].id.toString(), "drawable", context.packageName)).into(vh.ivProductImage)


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
    val llTypes : LinearLayoutCompat = view?.findViewById(R.id.llTypes) as LinearLayoutCompat
    val ivProductImage: ImageView = view?.findViewById(R.id.iv_Pokemon) as ImageView
    val tvPokemonName: TextView = view?.findViewById(R.id.tv_pokemonName) as TextView


}
