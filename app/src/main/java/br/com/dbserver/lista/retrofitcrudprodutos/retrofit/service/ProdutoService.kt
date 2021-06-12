package br.com.dbserver.lista.retrofitcrudprodutos.retrofit.service

import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import retrofit2.Call
import retrofit2.http.*

interface ProdutoService {
    @GET("produtos")
    fun listar(): Call<List<Produto>>

    @GET("produtos/{id}")
    fun buscarPorId(@Path("id") id: Int) : Call<Produto>

    @POST("produtos")
    fun inserir(@Body produto: Produto) : Call<Produto>

    @DELETE("produtos/{id}")
    fun deletar(@Path("id") id:Int) : Call<Unit>

    @PUT("produtos/{id}")
    fun alterar(@Path("id") id:Int, @Body produto: Produto) : Call<Produto>
}