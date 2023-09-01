package com.example.pokemon.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObjectPokemon(
    var namePokemon: String = "",
    var urlDetailPok: String = "",
    var urlImgDetPok: String = "",
    var abilities: String = "",
    var form: String = "",

) : Parcelable