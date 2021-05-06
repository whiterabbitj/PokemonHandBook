package com.example.pokemonhanbook.Fragments

import android.os.Bundle
import android.os.Parcelable
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


class MainPageFragment : Fragment(), ItemAdapter.IPokemonSelected {


    private val ICREMENTCOUNT = 10
    override fun onPokemonSelected(fso: Pokemon) {


    }

    private var currList = arrayListOf<Pokemon>()
    private var allNames = HashMap<String, Int>()
    private var idPkemon = arrayListOf<Int?>()
    private var currCount: Int = ICREMENTCOUNT
    private val pokeData = PokemonData()
    private var listViewAdapter: ItemAdapter? = null
    private lateinit var listView: ListView
    var state: Parcelable? = null
    var pokemonListAsynch: PokemonData.AsynchGetPokemonListData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonListAsynch = PokemonData.AsynchGetPokemonListData(
            currList, 1, ICREMENTCOUNT,
            context!!, listViewAdapter, this
        ).execute() as PokemonData.AsynchGetPokemonListData
        allNames = pokeData.getAllNames()!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.pokemon_listview, container, false)

        listView = root.findViewById<ListView>(R.id.lw_pokemonList)
        listView.setOnItemClickListener { parent, view, position, id ->

            val mapper = ObjectMapper()
            val jsonToPass: String = mapper.writeValueAsString(listView.getItemAtPosition(position))
            val pokemonViewDialog = PokemonViewDialog()
            val args = Bundle()
            args.putString("jsonPokemonObject", jsonToPass);
            pokemonViewDialog.setArguments(args)
            val fm = this.fragmentManager
            if (fm != null) {
                pokemonViewDialog.show(fm, "PokemonViewDialog")
            }
        }

        val btnLoadMore = root.findViewById<AppCompatButton>(R.id.btnLoadMorePokemon)
        btnLoadMore.setOnClickListener {
            val previousCount: Int = currCount + 1
            currCount += ICREMENTCOUNT
            pokemonListAsynch = PokemonData.AsynchGetPokemonListData(
                currList, previousCount, currCount,
                context!!, listViewAdapter, this
            ).execute() as PokemonData.AsynchGetPokemonListData
        }


        val editTextSearch = root.findViewById<AppCompatEditText>(R.id.editTextSearch)
        val btnDeleteText = root.findViewById<AppCompatImageButton>(R.id.btnDeleteText)
        val btnSearch = root.findViewById<AppCompatButton>(R.id.btnSearch)

        editTextSearch.doOnTextChanged { text, start, before, count ->
            if (editTextSearch.text?.isNotEmpty() == true)
                btnDeleteText.visibility = View.VISIBLE
            else
                btnDeleteText.visibility = View.GONE
        }

        btnSearch.setOnClickListener {
            if (editTextSearch.text != null) {
                val typedSearchPhrase: String = editTextSearch.text.toString()
                idPkemon.clear()
                if (typedSearchPhrase.length >= 3) {
                    for (hashMapVal in allNames)
                        if (hashMapVal.key.contains(typedSearchPhrase) || hashMapVal.key.equals(typedSearchPhrase))
                            idPkemon.add(allNames.getValue(hashMapVal.key))
                } else {
                    val toast = Toast.makeText(
                        context,
                        "3 Characters Are Required to Proceed With the Search",
                        Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.show()
                }

                if (idPkemon.size > 0) {
                    currList.clear()
                    idPkemon.forEach { x ->
                        if (x != null) {
                            pokeData.getSpecificPokemon(x)?.let { currList.add(it) }
                        }
                    }
                    listViewAdapter!!.notifyDataSetChanged()
                } else {
                    val toast = Toast.makeText(context, "Sorry Nothing Was Found", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.show()
                }
            }
        }
//

        btnDeleteText.setOnClickListener {
            editTextSearch.setText("")
            btnDeleteText.visibility = View.GONE
        }

        return root
    }

    public fun updateAdapter(pokemons: ArrayList<Pokemon>) {
        state = listView.onSaveInstanceState()
        listViewAdapter = ItemAdapter(this.requireActivity(), pokemons)
        listView.adapter = listViewAdapter
        listViewAdapter?.notifyDataSetChanged()
        if (state != null) {
            listView.onRestoreInstanceState(state);
        }
    }

}