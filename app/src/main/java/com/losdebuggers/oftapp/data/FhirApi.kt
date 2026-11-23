package com.losdebuggers.oftapp.data

import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface FhirApi {
    @GET
    suspend fun obtener(@Url ruta: String): JsonObject
}

object FhirNetwork {
    val api: FhirApi = Retrofit.Builder()
        .baseUrl("https://hapi.fhir.org/baseR4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FhirApi::class.java)

    fun recursos(json: JsonObject): List<JsonObject> =
        json.getAsJsonArray("entry")?.map {
            it.asJsonObject.getAsJsonObject("resource")
        } ?: emptyList()
}
