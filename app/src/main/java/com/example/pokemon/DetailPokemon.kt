package com.example.pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemon.Adapter.PokemonListAdapter
import com.example.pokemon.DB.SQLKontrol
import com.example.pokemon.PopUp.LoadingCostume
import com.example.pokemon.ViewModel.PokemonViewModel
import com.example.pokemon.data.ObjectPokemon
import com.example.pokemon.databinding.ActivityDetailPokemonBinding
import org.json.JSONArray

class DetailPokemon : AppCompatActivity() {
    private lateinit var bind : ActivityDetailPokemonBinding

    private lateinit var pokViewmodel: PokemonViewModel
    private var bundreceive = Bundle()
    private var urlDetail = ""
    private val adpterAbil = PokemonListAdapter()
    private val typeAdapter = PokemonListAdapter()
    private val loading = LoadingCostume()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailPokemonBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)

        if (intent.extras != null) {
            bundreceive = intent.extras!!
            urlDetail = bundreceive.getString("url").toString()
        }

        val sqlKontrol = SQLKontrol(this)
        if (sqlKontrol.cekData()) { Log.e( "data sqlite : ", sqlKontrol.getResponse()) }

        pokViewmodel = ViewModelProviders.of(this)[PokemonViewModel::class.java]

        bind.rcAbilities.setHasFixedSize(true)
        bind.rcAbilities.layoutManager = LinearLayoutManager(this)
        bind.rcAbilities.adapter = adpterAbil

        bind.rcPoketype.setHasFixedSize(true)
        bind.rcPoketype.layoutManager = LinearLayoutManager(this)
        bind.rcPoketype.adapter = typeAdapter

        pokViewmodel.setDetPokListener(object : PokemonViewModel.RequestListener() {
            override fun OnStart() {
                super.OnStart()
                loading.show(this@DetailPokemon, "Loading Data..")
                bind.clDetpokemon.visibility = View.GONE
            }

            override fun OnData() {
                super.OnData()
                bind.clDetpokemon.visibility = View.VISIBLE
            }

            override fun OnFinish() {
                super.OnFinish()
                loading.dismiss()
            }
        })

        pokViewmodel.setDataDetPoke(urlDetail)
        pokViewmodel.getDetailPokData().observe(this) {
            Log.e( "Data Detail : ", it.toString())
            Glide.with(this)
                .load(it.urlImgDetPok)
                .centerCrop()
                .error(R.drawable.pokemon)
                .into(bind.imgDetpokemon)

            bind.tvvlNamepokemon.text = it.namePokemon.toUpperCase()

            setAdapter(it.abilities)
            setTypeAdapter(it.form)
        }
    }

    fun setAdapter(abilitisJson: String) {
        val json = JSONArray(abilitisJson)
        val obj = ArrayList<ObjectPokemon>()
        for (i in 0 until json.length()) {
            val jRes = json.getJSONObject(i)
            val ob = ObjectPokemon()
            val jsonOb = jRes.getJSONObject("ability")
            ob.namePokemon = jsonOb.getString("name")
            obj.add(ob)
        }
        adpterAbil.setLData(obj)
    }

    fun setTypeAdapter(typeAdp: String) {
        val json = JSONArray(typeAdp)
        val obj = ArrayList<ObjectPokemon>()
        for (i in 0 until json.length()) {
            val jRes = json.getJSONObject(i)
            val ob = ObjectPokemon()
            val jsonVal = jRes.getJSONObject("type")
            ob.namePokemon = jsonVal.getString("name")
            obj.add(ob)
        }
        typeAdapter.setLData(obj)
    }
}