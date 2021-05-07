package com.example.pokemonhanbook.Activities

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonhanbook.Adpaters.ItemAdapter
import com.example.pokemonhanbook.R
import me.sargunvohra.lib.pokekotlin.model.Pokemon


class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
    }
}