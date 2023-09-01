package com.example.pokemon

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.pokemon.Adapter.PokemonListAdapter
import com.example.pokemon.DB.SQLKontrol
import com.example.pokemon.PopUp.LoadingCostume
import com.example.pokemon.ViewModel.PokemonViewModel
import com.example.pokemon.data.ObjectPokemon
import com.example.pokemon.databinding.ActivityMainBinding
import com.example.pokemon.databinding.ActivitySplashScreenBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var pokemonViewModel: PokemonViewModel
    private val loading = LoadingCostume()

    private val pokemonAdapter = PokemonListAdapter()
    private var pokelist = ArrayList<ObjectPokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        pokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel::class.java)

        pokemonViewModel.setListPokListener(object : PokemonViewModel.RequestListener() {
            override fun OnStart() {
                super.OnStart()
                loading.show(this@MainActivity, "Loading Data..")
                binding.rcPokemon.visibility = View.GONE
            }

            override fun OnData() {
                super.OnData()
                binding.rcPokemon.visibility = View.VISIBLE
            }

            override fun OnFinish() {
                super.OnFinish()
                loading.dismiss()
            }
        })

        binding.rcPokemon.setHasFixedSize(true)
        binding.rcPokemon.layoutManager = LinearLayoutManager(this)
        binding.rcPokemon.adapter = pokemonAdapter

        pokemonViewModel.setPokekList()
        pokemonViewModel.getListPokek().observe(this){
            this.pokelist = it
            pokemonAdapter.setLData(it)
        }

        pokemonAdapter.setClickListener(object : PokemonListAdapter.ClickListener() {
            override fun OnItemListClick(dtOrd: ObjectPokemon) {
                super.OnItemListClick(dtOrd)
                val bundle = Bundle()
                val inten = Intent(this@MainActivity, DetailPokemon::class.java)
                bundle.putString("url", dtOrd.urlDetailPok)
                inten.putExtras(bundle)
                startActivity(inten)
            }
        })

        val searcIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searcIcon.setColorFilter(Color.BLACK)
        val searchText = binding.searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(Color.BLACK)
        binding.searchView.setOnQueryTextListener(object :  SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                pokemonAdapter.setLData(pokelist)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pokemonAdapter.filter.filter(newText)
                pokemonAdapter.setLData(pokelist)
                return true
            }

        })

    }
}