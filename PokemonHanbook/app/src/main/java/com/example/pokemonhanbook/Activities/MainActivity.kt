package com.example.pokemonhanbook.Activities

import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.pokemonhanbook.API.PokemonData
import com.example.pokemonhanbook.Adpaters.ItemAdapter
import com.example.pokemonhanbook.R
import com.google.android.material.tabs.TabLayout
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites



class MainActivity : AppCompatActivity(),  ItemAdapter.IPokemonSelected
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
    }

    override fun onPokemonSelected(fso: Pokemon) {
        TODO("Not yet implemented")
    }

}
//class MainActivity: AppCompatActivity(),  ItemAdapter.IPokemonSelected {


//    lateinit var  mark: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val extras: Bundle? = intent.extras
//        setContentView(R.layout.mainactivity)
//        val MainGragment: Fragment = findViewById(R.id.view_pager)
////
////        mark = findViewById(R.id.mark)
////        fab = findViewById(R.id.checkCart)
//
//        val tabs: TabLayout = findViewById(R.id.tabs)
//        tabs.isSmoothScrollingEnabled = true
//
//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
//
//        var currList: ArrayList<Pokemon> = arrayListOf<Pokemon>()
//        var currPhotos: ArrayList<PokemonSprites> = arrayListOf()
//        val pokeData :PokemonData = PokemonData()
//
//        currList = pokeData.populateData( currList,1,12)!!
//
//
//
//
//        val temp = 0
//
////        sampleImages = GetData().makeAddRequest()
//
////        val distinctCategoryList = itemsList.distinctBy { x -> x.itemType }.map { x -> x.itemType }
//
////        for (i in 1..distinctCategoryList.count())
////            tabs.addTab(tabs.newTab().setText(itemsList.distinctBy { x -> x.itemType }[i-1].itemType))
//
////        val pagerViewAdapter = SectionsPagerAdapter(
////            supportFragmentManager,
////            tabs.tabCount,
////            itemsList,
////            pokeApi.getTypeList(0,15)
//////            distinctCategoryList
////        )
////        viewPager.adapter = pagerViewAdapter
////        tabs.setupWithViewPager(viewPager, true)
//
////        val carouselView = findViewById<CarouselView>(R.id.carouselView)
////        if (carouselView != null) {
////            carouselView.pageCount = sampleImages.size
////            carouselView.setImageListener(imageListener)
////        }
//
////        fab.setOnClickListener {
////            val intent = Intent(this, ChekoutActivity::class.java)
////            intent.putExtra("cartItems", itemsListAdded)
////            startActivity(intent)
////        }
//
//
//
//    }
////
////    var imageListener = ImageListener {
////            position, imageView -> Glide.with(this.applicationContext).load(sampleImages[position].addUrl).into(imageView)
////    }
//
//    override fun onPokemonSelected(fso: Pokemon) {
//        TODO("Not yet implemented")
//    }
//
//
//}