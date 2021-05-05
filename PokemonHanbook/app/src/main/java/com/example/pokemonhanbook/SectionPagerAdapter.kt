package com.example.pokemonhanbook

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonType

import java.util.stream.Collectors


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentManager, numberOfTabs: Int, items:List<NamedApiResource>, titles:List<PokemonType>): FragmentPagerAdapter(fm) {

    private var fragment: Fragment? = null
    private var noOfTabs: Int = numberOfTabs
    private var pokemonList: List<NamedApiResource> = items
    private var titleList: List<PokemonType> = titles

    override fun getItem(position: Int): Fragment {

        for (i in 0..noOfTabs){ // populate fragments accordingly  each title determines a type of fragment
//            if (i == position){
//                val title =  titleList[i]
//                fragment = if(title == "Cart")
//                    CartFragment.newInstance(pokemonList)
//                else {
//                    PlaceholderFragment.newInstance(
//                        i,
//                        title,
//                        pokemonList.stream().filter { x -> x.itemType == title }.collect(
//                            Collectors.toList()
//                        ) as ArrayList<ItemList>
//                    )
//                }
//                break
//            }
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return noOfTabs
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position].type.name

    }
}
