package com.reviakin_package.pokemon_app.pojo

import com.google.gson.annotations.SerializedName

data class Sprites (

    @SerializedName("back_default") var backDefault : String,
    @SerializedName("back_female") var backFemale : String,
    @SerializedName("back_shiny") var backShiny : String,
    @SerializedName("back_shiny_female") var backShinyFemale : String,
    @SerializedName("front_default") var frontDefault : String,
    @SerializedName("front_female") var frontFemale : String,
    @SerializedName("front_shiny") var frontShiny : String,
    @SerializedName("front_shiny_female") var frontShinyFemale : String,

)