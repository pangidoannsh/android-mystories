package com.pangidoannsh.mystories.data.api

import com.pangidoannsh.mystories.data.api.payload.Login
import com.pangidoannsh.mystories.data.api.payload.Register
import com.pangidoannsh.mystories.data.api.response.CreateStoryResponse
import com.pangidoannsh.mystories.data.api.response.DetailStoryResponse
import com.pangidoannsh.mystories.data.api.response.LoginResponse
import com.pangidoannsh.mystories.data.api.response.RegisterResponse
import com.pangidoannsh.mystories.data.api.response.StoriesResponse
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("No-Authentication: true")
    @POST("/v1/login")
    fun login(
        @Body payload: Login,
    ): Call<LoginResponse>

    @Headers("No-Authentication: true")
    @POST("/v1/register")
    fun register(
        @Body payload: Register,
    ): Call<RegisterResponse>

    @GET("/v1/stories")
    fun getStories(): Call<StoriesResponse>

    @GET("/v1/stories")
    suspend fun getStoriesApi(): StoriesResponse

    @GET("/v1/stories")
    fun getStories(
        @Query("size") size: Int,
        @Query("page") page: Int = 1,
        @Query("location") location: Int = 0
    ): Call<StoriesResponse>

    @GET("/v1/stories")
    suspend fun getStoriesPaging(
        @Query("size") size: Int,
        @Query("page") page: Int = 1,
        @Query("location") location: Int = 0
    ): StoriesResponse

    @Multipart
    @POST("/v1/stories")
    fun createStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ): Call<CreateStoryResponse>

    @GET("/v1/stories/{id}")
    fun getStoryById(
        @Path("id") id: String
    ): Call<DetailStoryResponse>
}