package br.com.dbserver.lista.retrofitcrudprodutos.retrofit

import br.com.dbserver.lista.retrofitcrudprodutos.retrofit.service.ProdutoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppRetrofit {
    companion object{
        private const val BASE_URL = "http://10.0.2.2:3000"
        val retrofit : Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val produtoService by lazy {
            retrofit.create(ProdutoService::class.java)
        }
    }
}