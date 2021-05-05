package com.example.pokemonhanbook.Dialog

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.pokemonhanbook.R
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import java.lang.Exception

class PokemonViewDialog : DialogFragment() {

    private var jsonObject: String? = null
    private var pokeObject: Pokemon? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        jsonObject = arguments?.getString(KEY)

        try{
            //Getting JSON String converting it to an object as the Pokemodel class is not parcelable
            //
            pokeObject =  jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readerFor(Pokemon::class.java).readValue<Pokemon>(jsonObject)
            jsonObject= "";
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        val view = inflater.inflate(R.layout.pokemon_individual, container, false)
        activity?.let { Glide.with(it.applicationContext).load(pokeObject?.sprites?.frontDefault).into(view.findViewById<ImageView>(R.id.iv_Pokemon)) }




        return view;
    }


    companion object {
        private const val KEY = "jsonPokemonObject"

        @JvmStatic
        fun newInstance(json: String): PokemonViewDialog {
            return PokemonViewDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY, json)
                }
            }
        }
    }

}