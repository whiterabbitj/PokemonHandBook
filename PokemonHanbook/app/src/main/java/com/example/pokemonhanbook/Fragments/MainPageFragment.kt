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
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import java.util.*


class MainPageFragment : Fragment() {

    // GLOBAL VARIABLES
    private val INCREMENT = 10   // USED FOR GETTING POKEMON DATA
    private var currList = arrayListOf<Pokemon>() // MAIN LIST USED TO POPULATE THE ADAPTER
    private var allNames = HashMap<String, Int>() // MAIN LIST THAT KEEPS ALL THE POKEMON NAMES
    private var listViewAdapter: ItemAdapter? = null // MAIN LIST VIEW ADAPTER
    private lateinit var listView: ListView // MAIN LIST VIEW
    private lateinit var btnLoadMore: AppCompatButton
    private lateinit var btnSearch: AppCompatButton
    private lateinit var asyncTask: PokemonData.AsyncGetPokemonListData
    private var currCount: Int = INCREMENT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // GET THE POKEMON DATA FROM THE API
        asyncTask = PokemonData.AsyncGetPokemonListData(1, INCREMENT, context!!, this)
        asyncTask.execute()
        //POPULATE THE LISTVIEW ADAPTER
        listViewAdapter = ItemAdapter(this.requireActivity(), currList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.pokemon_listview, container, false)

         // KEEPS THE COUNT OF THE NEXT ITEMS TO BE FETCHED
        val pokemonIds = arrayListOf<Int?>() // LIST OF INT OF THE MATCHING POKEMON IDS
        var tempList = arrayListOf<Pokemon>() // TEMPORARY LIST THAT STORES THE FETCHED LIST OF THE LISTVIEW ADAPTER ONCE THE SEARCH OCCURS

        // get all names of possible pokemons
        if(allNames.size==0)
            PokemonData.AsyncGetNames(this).execute()


        //Views
        val editTextSearch = root.findViewById<AppCompatEditText>(R.id.editTextSearch)
        val btnDeleteText = root.findViewById<AppCompatImageButton>(R.id.btnDeleteText)
        btnSearch = root.findViewById(R.id.btnSearch)

        //SET THE MAIN LIST VIEW ADAPTER
        listView = root.findViewById(R.id.lw_pokemonList)
        listView.adapter = listViewAdapter

        // List View Item CLick, Pass the Object AS Json as The Pokemon Class Object does not support parcelable
        // Use Jackson API to serialize the object found here  - https://github.com/FasterXML/jackson
        listView.setOnItemClickListener { _, _, position, _ ->
            asyncTask.cancel(true)
            val tempDataObj:Pokemon = listView.getItemAtPosition(position) as Pokemon
            if(asyncTask.isCancelled)
                PokemonData.GetSpecificCharacteristic(tempDataObj, this).execute()
        }

        btnLoadMore= root.findViewById(R.id.btnLoadMorePokemon)
        btnLoadMore.setOnClickListener {
            // Increment the precious counter and the current counter accordingly
            val previousCount: Int = currCount + 1
            currCount += INCREMENT
            // Fetch More Data With the Updated VAlues
            asyncTask = PokemonData.AsyncGetPokemonListData(previousCount, currCount, context!!, this)
            asyncTask.execute()
            setLoadingAnimation(true , "Load")
        }

        editTextSearch.doOnTextChanged { _, _, _, _ ->
            // check if not null
            if (editTextSearch.text?.isNotEmpty() == true){
                //if its is greater 3 chars hide/ show other control
                if(editTextSearch.text!!.length >= 3)
                     btnDeleteText.visibility = View.VISIBLE

            }
            else {
                btnDeleteText.visibility = View.GONE
                btnLoadMore.visibility = View.VISIBLE
                //update adapter as the user has deleted their search phrase
                if(tempList.size > 0){
                    currList.clear()
                    updateAdapter(tempList)
                }

            }
        }

        // search functionality - just using contains quite simple
        btnSearch.setOnClickListener {
            if (editTextSearch.text != null) {
                val typedSearchPhrase: String = editTextSearch.text.toString()
                pokemonIds.clear()
                if (typedSearchPhrase.length >= 3) {
                    //gets searched phrase
                    btnLoadMore.visibility = View.GONE
                    for (hashMapVal in allNames)
                        //get all the names from the preloaded list
                        if (hashMapVal.key.contains(typedSearchPhrase))
                            //ad the ids
                            pokemonIds.add(allNames.getValue(hashMapVal.key))

                    if (pokemonIds.size > 0) {
                        //cancel if the task of loading extra pokemon is running
                        asyncTask.cancel(true)
                        setLoadingAnimation(true, "Search")
                        if (currList.size > 0) {
                            //put the values in a tremporary list
                            if (tempList.size == 0)
                                tempList = ArrayList(currList)
                            currList.clear()
                            listViewAdapter?.notifyDataSetChanged()
                        }
                        // if the task is canceled tun another task - the canceled taskl causes listview items to be updated
                        if (asyncTask.isCancelled){
                            //get the specific pokemon by the ID
                            PokemonData.AsyncGetSpecificPokemon(this, pokemonIds).execute()

                        }
                    } else {
                        val toast = Toast.makeText(context, "Sorry Nothing Was Found", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        context,
                        "3 Characters Are Required to Proceed With the Search",
                        Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.show()
                }
            }

        }
        // delete Search criteria update the item adapter
        btnDeleteText.setOnClickListener {
            btnLoadMore.visibility = View.VISIBLE
            editTextSearch.setText("")
            btnDeleteText.visibility = View.GONE
            if(tempList.size > 0){
                currList.clear()
                updateAdapter(tempList)
            }
        }
        return root
    }

    // generic function that updated the listview item adapter keep the current scroll position as well
    fun updateAdapter(pokemon: ArrayList<Pokemon>) {
        pokemon.forEach { x-> currList.add(x) }
        val state = listView.onSaveInstanceState()
        listViewAdapter?.notifyDataSetChanged()
        if (state != null) {
            listView.onRestoreInstanceState(state)
        }
    }

    // updates all fetched from the APi Search
    fun updateNameList(params: HashMap<String,Int>){
        allNames = params
    }

    // helper function to update the buttons to notiufy about the process
    fun setLoadingAnimation(show:Boolean, type:String) {
        var tempButton: AppCompatButton? = null
        when (type) {
            "Search" ->
                if (show) {
                    btnSearch.isClickable = true
                    btnSearch.background = resources.getDrawable(R.drawable.pressed)
                    btnSearch.text = getString(R.string.searching)
                } else {
                    btnSearch.isClickable = true
                    btnSearch.background = resources.getDrawable(R.drawable.un_pressed)
                    btnSearch.text = getString(R.string.search)
                }
            "Load" ->
                if (show) {
                    btnLoadMore.isClickable = true
                    btnLoadMore.background = resources.getDrawable(R.drawable.pressed)
                    btnLoadMore.text = getString(R.string.loading)
                } else {
                    btnLoadMore.isClickable = true
                    btnLoadMore.background = resources.getDrawable(R.drawable.un_pressed)
                    btnLoadMore.text = getString(R.string.loadMore)
                }
        }
    }

    fun updateCurCountOnCancel(newCount:Int){
        currCount = newCount
    }
    fun updateCharAndShowdialog(currChar:String, json:String){
        //Show the Dialog prompet from the asynch task of fetching charecteristics
        val pokemonViewDialog = PokemonViewDialog()
        val args = Bundle()
        args.putString("jsonPokemonObject", json)
        args.putString("characteristics", currChar)
        pokemonViewDialog.arguments = args
        val fm = this.fragmentManager
        if (fm != null) {
            pokemonViewDialog.show(fm, "PokemonViewDialog")
        }
    }

}