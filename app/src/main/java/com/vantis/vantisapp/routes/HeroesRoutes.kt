package com.vantis.vantisapp.routes

import com.vantis.vantisapp.models.Hero
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by LTI.Mario Josue Ortegon Chan on 4/20/2022.
 */
interface HeroesRoutes {

    //Peticion para listar los superheroes por id
    @GET("{id}")
    fun getHero(@Path("id") id: Int): Call<Hero>?

}