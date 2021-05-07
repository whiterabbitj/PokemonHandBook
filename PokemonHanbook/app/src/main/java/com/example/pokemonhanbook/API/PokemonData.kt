package com.example.pokemonhanbook.API

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import com.example.pokemonhanbook.Fragments.MainPageFragment
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Characteristic
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.Pokemon


class PokemonData {

//    Using https://github.com/PokeAPI/pokekotlin For fetching of the data

    //asynch task for getting the list data total count of the pokemons is 898 I am getting every 10 of them upon the user click
    class AsyncGetPokemonListData(
        prevItems: Int,
        totalItems: Int,
        activity: Context,
        mainPageFrag: MainPageFragment
    ) :

        AsyncTask<Pokemon, Pokemon, ArrayList<Pokemon>>() {
        private var mainFrag: MainPageFragment? = null
        private val pokeApi = PokeApiClient()
        private var tempCounter = prevItems
        private var allItems = totalItems
        private var pokemonList:ArrayList<Pokemon> = arrayListOf()
        private var act = activity
        var progressDialog: ProgressDialog? = null

        override fun onPostExecute(result: ArrayList<Pokemon>) {
            mainFrag?.updateAdapter(result)
            mainFrag?.setLoadingAnimation(false,"Load")
            progressDialog?.dismiss()
        }

        init {
            mainFrag = mainPageFrag
        }

        override fun doInBackground(vararg params: Pokemon): ArrayList<Pokemon>? {
            var item: Pokemon? = null
            try {
                //check if it is smaller than current item counter
                while (tempCounter <= allItems && tempCounter <= 898) {
                    //break if it is cancelled
                    if (isCancelled)
                        break

                    item = pokeApi.getPokemon(tempCounter)
                    pokemonList.add(item)
                    tempCounter++
                }
            }  catch (t: Throwable) {
                t.printStackTrace()
                return null
            }
            return pokemonList
        }
        override fun onCancelled() {
            super.onCancelled()
            //if cancelled reset the item count
            mainFrag?.updateCurCountOnCancel(allItems-10)
            mainFrag?.setLoadingAnimation(false , "Load")
        }

        override fun onPreExecute() {
            if (allItems == 10) {
                progressDialog = ProgressDialog(act);
                progressDialog!!.setCancelable(false);
                progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog!!.setTitle("Catching some Pokemon");
                progressDialog!!.setMessage("Please Wait...");
                progressDialog!!.show()
            }
        }

        override fun onProgressUpdate(vararg values: Pokemon) {
            super.onProgressUpdate(*values)
        }
    }

    //get all the names available put them in the hashmap to search for later
    class AsyncGetNames(mainPageFrag: MainPageFragment) :

        AsyncTask<HashMap<String, Int>, NamedApiResource, HashMap<String, Int>>() {
        private var mainFrag: MainPageFragment? = null
        private val pokeApi = PokeApiClient()
        private val returnHash: HashMap<String, Int> = HashMap()

        override fun onPostExecute(result: HashMap<String, Int>?) {
            mainFrag?.updateNameList(returnHash)
        }

        init {
            mainFrag = mainPageFrag
        }

        override fun doInBackground(vararg params: HashMap<String, Int>): HashMap<String, Int>? {
            try {
                val item: List<NamedApiResource> = pokeApi.getPokemonList(0, 898).results
                item.forEach { x -> publishProgress(x) }

            } catch (t: Throwable) {
                t.printStackTrace()
                return null
            }
            return returnHash

        }

        override fun onProgressUpdate(vararg values: NamedApiResource) {
            super.onProgressUpdate(*values)
            returnHash[values[0].name] = values[0].id
        }
    }

    //get a specific pokemon based on Id
    class AsyncGetSpecificPokemon(mainPageFrag: MainPageFragment, idList: ArrayList<Int?>) :
        AsyncTask<ArrayList<Pokemon>, Pokemon, ArrayList<Pokemon>>() {
        private var mainFrag: MainPageFragment? = null
        private val pokeApi = PokeApiClient()
        private val pokeList = arrayListOf<Pokemon>()
        private val idListData = idList

        override fun onPostExecute(result: ArrayList<Pokemon>) {
            mainFrag?.updateAdapter(result)
            mainFrag?.setLoadingAnimation(false,"Search")
        }

        init {
            mainFrag = mainPageFrag
        }

        override fun doInBackground(vararg params: ArrayList<Pokemon>): ArrayList<Pokemon>? {
            try {
                var item:Pokemon
                idListData.forEach { x ->
                    run {
                        item = x?.let { pokeApi.getPokemon(it) }!!
                        pokeList.add(item)
                        publishProgress(item)
                    }
                }
            }  catch (t: Throwable) {
                t.printStackTrace()
                return null
            }
            return pokeList

        }
        override fun onPreExecute() {
//                mainFrag?.setLoadingAnimation(true)
        }

        override fun onProgressUpdate(vararg values: Pokemon) {
            super.onProgressUpdate(*values)
        }
    }

    //get pokemon charesteristics
    class GetSpecificCharacteristic(id: Int) :
        AsyncTask<Characteristic,Characteristic, String>() {
        private val pokeApi = PokeApiClient()
        private var item : Characteristic? = null
        private val idPokemon = id


        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
        }
        init {
        }
        override fun doInBackground(vararg params: Characteristic):String? {
            try {
                item = pokeApi.getCharacteristic(idPokemon)
            } catch (t: Throwable) {
                t.printStackTrace()
                return "Nothing is available"
            }
            return item!!.descriptions[2].description
        }

        override fun onPreExecute() {
        }

        override fun onProgressUpdate(vararg values: Characteristic) {
            super.onProgressUpdate(*values)
        }
    }


}


