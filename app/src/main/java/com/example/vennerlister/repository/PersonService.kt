package com.example.vennerlister.repository


import com.example.vennerlister.models.Person
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface PersonService {


    @GET("persons")
    fun GetByUserId(@Query("user_id") userId: String): Call <List<Person>>

    @POST("persons")
    fun savePerson(@Body person: Person): Call<Person>

    @DELETE("persons/{id}")
    fun delete(@Path("id") id:Int): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id:Int, @Body person: Person): Call<Person>

}

