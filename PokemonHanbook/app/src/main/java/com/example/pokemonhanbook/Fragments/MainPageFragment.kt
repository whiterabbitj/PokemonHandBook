package com.example.pokemonhanbook.Fragments

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.pokemonhanbook.API.PokemonData
import com.example.pokemonhanbook.Adpaters.ItemAdapter
import com.example.pokemonhanbook.R
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class MainPageFragment : Fragment(), ItemAdapter.IPokemonSelected{


    private val ICREMENTCOUNT = 10
    override fun onPokemonSelected(fso: Pokemon) {
        TODO("Not yet implemented")
    }
    var currList = arrayListOf<Pokemon>()
    var allNames = HashMap<String, Int>()
    var idPkemon  = arrayListOf<Int?>()
    var currCount:Int = 1
    val pokeData = PokemonData()


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)


        currList = pokeData.populateData( currList,1,ICREMENTCOUNT)!!
        allNames = pokeData.getAllNames()!!

        currCount = ICREMENTCOUNT

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater!!.inflate(R.layout.pokemon_listview, container, false)
        val listView = root.findViewById<ListView>(R.id.lw_pokemonList)

        val listViewAdapter = ItemAdapter(this.requireActivity(),currList)
        listViewAdapter.setOnPokemonSelectedListener(this)
        listView.adapter = listViewAdapter

        val btnLoadMore  = root.findViewById<AppCompatButton>(R.id.btnLoadMorePokemon)
        btnLoadMore.setOnClickListener {
            val previousCount = ICREMENTCOUNT+1
            currCount += ICREMENTCOUNT
            currList =  pokeData.populateData( currList,previousCount,currCount)!!
            listViewAdapter.notifyDataSetChanged()
        }

        val editTextSearch = root.findViewById<AppCompatEditText>(R.id.editTextSearch)
        editTextSearch.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                idPkemon.clear()
                if(text.length>3) {
                    for(hashMapVal in allNames)
                        if(hashMapVal.key.contains(text.toString()) || hashMapVal.key.equals(text))
                            idPkemon.add(allNames.getValue(hashMapVal.key))
                }

            if(idPkemon.size > 0){
                currList.clear()
                idPkemon.forEach { x->
                    if(x!= null){
                        pokeData.getSpecificPokemon(x)?.let { currList.add(it) }
                    }
                }
                listViewAdapter.notifyDataSetChanged()
            }
            }
        }

        //getCount ot the total items to be displayed



        return root
    }

}