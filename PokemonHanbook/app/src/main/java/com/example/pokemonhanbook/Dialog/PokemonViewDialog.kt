package com.example.pokemonhanbook.Dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.pokemonhanbook.R
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class PokemonViewDialog : DialogFragment() {

    private var jsonObject: String? = null
    private var pokeObject: Pokemon? = null
    private var characteristic: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jsonObject = arguments?.getString(jsonObj)
        characteristic = arguments?.getString(charString)
        try {
            //Getting JSON String converting it to an object as the Pokemodel class is not parcelable,
            //possible to pass only the strings, but less to change if there are changes in the future
            pokeObject = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readerFor(Pokemon::class.java).readValue<Pokemon>(jsonObject)
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            jsonObject = null
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.pokemon_individual, container, false)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        //set Photos for the courasel view
        val photos: ArrayList<String> = arrayListOf()
        pokeObject?.sprites?.frontDefault?.let { photos.add(it) }
        pokeObject?.sprites?.backDefault?.let { photos.add(it) }
        pokeObject?.sprites?.frontShiny?.let { photos.add(it) }
        val carouselView = view.findViewById<CarouselView>(R.id.carouselView)
        val imageListener = ImageListener { position, imageView ->
            Glide.with(this).load(photos[position]).into(imageView)
        }
        if (carouselView != null) {
            carouselView.pageCount = 3
            carouselView.setImageListener(imageListener)
        }
        //set Name
        view.findViewById<AppCompatTextView>(R.id.tv_pokemonNameDialog).text = pokeObject?.name?.substring(0, 1)?.let {
            String.format(
                "%s%s",
                it.toUpperCase(),
                pokeObject?.name?.substring(1)

            )
        }
        //set all the other values
        val weight = pokeObject?.weight
        view.findViewById<AppCompatTextView>(R.id.tv_PokemonWeightVal).text =
            String.format("%s kg", ((weight?.toDouble())!! / 10).toString())
        val height = pokeObject?.height
        val heightM = pokeObject?.height
        view.findViewById<AppCompatTextView>(R.id.tv_PokemonHeightVal).text =
            String.format("%s m", ((heightM?.toDouble())!! / 10).toString())

        var tempAbilityString: String = ""
        pokeObject?.abilities?.forEach { x -> tempAbilityString += x.ability.name + "\n" }
        view.findViewById<AppCompatTextView>(R.id.tvAbilitiesVal).text = tempAbilityString

        view.findViewById<AppCompatTextView>(R.id.tcChar).text = characteristic


        //populate types with coressponding colours need to refactor this
        pokeObject?.types?.forEach { x ->
            val textViewAbility = TextView(context)
            val llps = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            )
            llps.setMargins(0, 0, 0, 4)
            textViewAbility.layoutParams = llps
            textViewAbility.setPadding(3, 3, 3, 3)
            textViewAbility.setTextColor(Color.WHITE)
            textViewAbility.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                textViewAbility.setBackgroundResource(
                    context!!.resources.getIdentifier(
                        x.type.name,
                        "drawable",
                        context!!.packageName
                    )
                )

            textViewAbility.text = String.format("%s%s", x.type.name[0].toUpperCase(), x.type.name.substring(1))
            view.findViewById<LinearLayoutCompat>(R.id.llTypes).addView(textViewAbility)
        }

        return view
    }


    companion object {
        private const val jsonObj = "jsonPokemonObject"
        private const val charString = "characteristics"

        @JvmStatic
        fun newInstance(json: String,charString:String ): PokemonViewDialog {
            return PokemonViewDialog().apply {
                arguments = Bundle().apply {
                    putString(jsonObj, json)
                    putString(jsonObj, charString)
                }
            }
        }
    }

}