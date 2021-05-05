package com.example.pokemonhanbook.API

import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import org.json.JSONException

class PokemonData {


     fun populateData (pokemonListDta:ArrayList<Pokemon>, prevItems:Int,totalItems:Int) : ArrayList<Pokemon>?
     {
         val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
         StrictMode.setThreadPolicy(policy)
         try {
             val pokeApi = PokeApiClient()
             var tempCounter = prevItems
             while(tempCounter <= totalItems)
             {
                 val item : Pokemon = pokeApi.getPokemon(tempCounter)
                 pokemonListDta.add(item)
                 tempCounter++
             }
         }
         catch (e:NetworkOnMainThreadException){
             return null
         }
         return pokemonListDta
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