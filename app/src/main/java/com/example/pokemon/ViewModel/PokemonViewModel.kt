package com.example.pokemon.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemon.DB.SQLKontrol
import com.example.pokemon.data.ObjectPokemon
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val cntx: Context

    init {
        this.cntx = application
    }

    private var listPokListener: RequestListener? = null
    private val mListPok = MutableLiveData<ArrayList<ObjectPokemon>>()

    fun setListPokListener(requestListener: RequestListener) {
        this.listPokListener = requestListener
    }

    fun getListPokek() : MutableLiveData<ArrayList<ObjectPokemon>> {
        return mListPok
    }

    fun setPokekList() {
        listPokListener?.OnStart()
        val sqlKontrol = SQLKontrol(cntx)
        val client = AsyncHttpClient()
        client.setTimeout(15000)
        client.get(cntx, "https://pokeapi.co/api/v2/pokemon", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val results = responseBody?.let { String(it) }
                try {
                    val json = JSONObject(results)
                    val jsonResult = json.getJSONArray("results")
                    val obj = ArrayList<ObjectPokemon>()
                    for (i in 0 until jsonResult.length()) {
                        val jo = jsonResult.getJSONObject(i)
                        val ob = ObjectPokemon()
                        ob.namePokemon = jo.getString("name")
                        ob.urlDetailPok = jo.getString("url")

                        obj.add(ob)
                    }
                    listPokListener?.OnData()
                    mListPok.value = obj
                    sqlKontrol.setSaveResponse(results)
                } catch (e: JSONException) {
                    Toast.makeText(cntx, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(cntx, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show()}
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Toast.makeText(cntx, "Error : $statusCode", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish() {
                super.onFinish()
                listPokListener?.OnFinish()
            }

        })
    }

    private var detPokListener: RequestListener? = null
    private val mDetPokemon = MutableLiveData<ObjectPokemon>()

    fun setDetPokListener(requestListener: RequestListener) {
        this.detPokListener = requestListener
    }

    fun getDetailPokData() : MutableLiveData<ObjectPokemon> {
        return mDetPokemon
    }

    fun setDataDetPoke(url: String) {
        detPokListener?.OnStart()
        val client = AsyncHttpClient()
        client.setTimeout(30000)
        client.get(cntx, url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                try {
                    val json = JSONObject(result)
                    val ob = ObjectPokemon()
                    ob.abilities = json.getString("abilities")
                    ob.form = json.getString("types")
                    val imgJson = json.getJSONObject("sprites")
                    ob.urlImgDetPok = imgJson.getString("front_default")
                    ob.namePokemon = json.getString("name")

                    detPokListener?.OnData()
                    mDetPokemon.value = ob
                } catch (e: JSONException) {
                    Toast.makeText(cntx, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(cntx, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Toast.makeText(cntx, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish() {
                super.onFinish()
                detPokListener?.OnFinish()
            }
        })
    }

    /**
     * interface listener
     */
    open class RequestListener : IFaceRequestListener {
        override fun OnStart() {}
        override fun OnFinish() {}
        override fun OnData() {}
    }

    protected interface IFaceRequestListener {
        fun OnStart()
        fun OnFinish()
        fun OnData()
    }
}
