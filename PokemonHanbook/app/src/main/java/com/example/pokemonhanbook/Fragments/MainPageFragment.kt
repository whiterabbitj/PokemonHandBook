package com.example.pokemonhanbook.Fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.pokemonhanbook.API.PokemonData
import com.example.pokemonhanbook.Adpaters.ItemAdapter
import com.example.pokemonhanbook.Dialog.PokemonViewDialog
import com.example.pokemonhanbook.R
import com.fasterxml.jackson.databind.ObjectMapper
import me.sargunvohra.lib.pokekotlin.model.Pokemon


class MainPageFragment : Fragment(), ItemAdapter.IPokemonSelected{


    private val ICREMENTCOUNT = 5
    override fun onPokemonSelected(fso: Pokemon) {




    }
    var currList = arrayListOf<Pokemon>()
    var allNames = HashMap<String, Int>()
    var idPkemon  = arrayListOf<Int?>()
    var currCount:Int = ICREMENTCOUNT
    val pokeData = PokemonData()


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        currList = pokeData.populateData( currList,1,ICREMENTCOUNT)!!
        allNames = pokeData.getAllNames()!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.pokemon_listview, container, false)
        val listView = root.findViewById<ListView>(R.id.lw_pokemonList)

        val listViewAdapter = ItemAdapter(this.requireActivity(),currList)
        listViewAdapter.setOnPokemonSelectedListener(this)
        listView.adapter = listViewAdapter

        listView.setOnItemClickListener { parent, view, position, id ->

            val mapper = ObjectMapper()
            val jsonToPass : String = mapper.writeValueAsString(listView.getItemAtPosition(position))
            val pokemonViewDialog = PokemonViewDialog()
            val args = Bundle()
            args.putString("jsonPokemonObject", jsonToPass);
            pokemonViewDialog.setArguments(args)
            val fm = this.fragmentManager
            if (fm != null) {
                pokemonViewDialog.show(fm, "PokemonViewDialog")
            }
        }

        val btnLoadMore  = root.findViewById<AppCompatButton>(R.id.btnLoadMorePokemon)
        btnLoadMore.setOnClickListener {
            val previousCount :Int=currCount+1
            currCount += ICREMENTCOUNT
            currList =  pokeData.populateData( currList,previousCount,currCount)!!
            listViewAdapter.notifyDataSetChanged()
        }

        val editTextSearch = root.findViewById<AppCompatEditText>(R.id.editTextSearch)
        val btnDeleteText = root.findViewById<AppCompatImageButton>(R.id.btnDeleteText)
        val btnSearch = root.findViewById<AppCompatButton>(R.id.btnSearch)

        editTextSearch.doOnTextChanged { text, start, before, count ->
            if(editTextSearch.text?.isNotEmpty() == true)
                btnDeleteText.visibility = View.VISIBLE
            else
                btnDeleteText.visibility = View.GONE
        }

        btnSearch.setOnClickListener {
            if (editTextSearch.text != null) {
                val typedSearchPhrase:String = editTextSearch.text.toString()
                idPkemon.clear()
                if(typedSearchPhrase.length>=3) {
                    for(hashMapVal in allNames)
                        if(hashMapVal.key.contains(typedSearchPhrase) || hashMapVal.key.equals(typedSearchPhrase))
                            idPkemon.add(allNames.getValue(hashMapVal.key))
                }
                else
                {
                    val toast = Toast.makeText(context, "3 Characters Are Required to Proceed With the Search", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.show()
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
                else{
                val toast = Toast.makeText(context, "Sorry Nothing Was Found", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
                }
            }
        }


        btnDeleteText.setOnClickListener{
            editTextSearch.setText("")
            btnDeleteText.visibility = View.GONE
        }




        return root
    }
}