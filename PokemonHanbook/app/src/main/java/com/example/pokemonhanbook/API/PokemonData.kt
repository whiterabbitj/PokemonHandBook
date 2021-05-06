package com.example.pokemonhanbook.API

import android.content.Context
import android.os.AsyncTask
import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import android.widget.ListView
import com.example.pokemonhanbook.Adpaters.ItemAdapter
import com.example.pokemonhanbook.Fragments.MainPageFragment
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import java.lang.Exception


class PokemonData {


//     class populateData (pokemonListDta:ArrayList<Pokemon>, prevItems:Int,totalItems:Int) : ArrayList<Pokemon>?
//     {
//         val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//         StrictMode.setThreadPolicy(policy)
//         try {
//             val pokeApi = PokeApiClient()
//             var tempCounter = prevItems
//             while(tempCounter <= totalItems)
//             {
//                 val item : Pokemon = pokeApi.getPokemon(tempCounter)
//                 pokemonListDta.add(item)
//                 tempCounter++
//             }
//         }
//         catch (e:NetworkOnMainThreadException){
//             return null
//         }
//         return pokemonListDta
//     }


    class AsynchGetPokemonListData(
        pokemonListDta: ArrayList<Pokemon>,
        prevItems: Int,
        totalItems: Int,
        activity: Context,
        listViewAdapter: ItemAdapter?,
        mainPageFrag: MainPageFragment
    ) :
        AsyncTask<ArrayList<Pokemon>, Pokemon, ArrayList<Pokemon>>() {
        private var mainFrag: MainPageFragment? = null
        private val pokeApi = PokeApiClient()
        private var tempCounter = prevItems
        private var allItems = totalItems
        private var pokemonItems = pokemonListDta

        override fun onPostExecute(result: ArrayList<Pokemon>) {
            mainFrag?.updateAdapter(pokemonItems)
        }

        init {
            mainFrag = mainPageFrag
        }

        override fun doInBackground(vararg params: ArrayList<Pokemon>): ArrayList<Pokemon>? {
            try {
                while (tempCounter <= allItems) {
                    val item: Pokemon = pokeApi.getPokemon(tempCounter)
                    tempCounter++
                    publishProgress(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return pokemonItems

        }

        override fun onProgressUpdate(vararg values: Pokemon) {
            super.onProgressUpdate(*values)
            pokemonItems.add(values[0])
            mainFrag?.updateAdapter(pokemonItems)

        }


    }
    fun getAllNames () : HashMap<String,Int >?
    {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val returnHash : HashMap<String,Int > = HashMap()
        try {
            val pokeApi = PokeApiClient()
                val item : List<NamedApiResource> = pokeApi.getPokemonList(0 , 889).results
                item.forEach { x ->  returnHash.put(x.name , x.id)}
        }
        catch (e:NetworkOnMainThreadException){
            return null
        }
        return returnHash
    }

    fun getSpecificPokemon(_id:Int) : Pokemon? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val pokeApi = PokeApiClient()
        val returnVal = pokeApi.getPokemon(_id)
//        try {
//            val pokeApi = PokeApiClient()
//            val returnVal = pokeApi.getPokemon(_id)
//        }
//        catch (e:NetworkOnMainThreadException){
//        }
        return returnVal

    }
}