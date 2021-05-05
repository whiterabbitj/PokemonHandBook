package com.example.pokemonhanbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.pokemonhanbook.Adpaters.ItemAdapter
import me.sargunvohra.lib.pokekotlin.model.Pokemon


/**
 * A placeholder fragment containing a simple view.
 */

class MainViewFragment : Fragment() {

    private var itemsList: ArrayList<Pokemon> = arrayListOf()
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        itemsList = arguments?.getParcelableArrayList(ARG_LIST)!!
//        title = arguments?.getString(ARG_TITLE)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.pokemon_listview, container, false)
        val listView = root.findViewById<ListView>(R.id.lw_pokemonList)


        val listViewAdapter = ItemAdapter(this.requireActivity(), itemsList)
        listView.adapter = listViewAdapter

        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_TITLE = "title"
        private const val ARG_LIST = "item_list"
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): MainViewFragment {
            return MainViewFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }




}