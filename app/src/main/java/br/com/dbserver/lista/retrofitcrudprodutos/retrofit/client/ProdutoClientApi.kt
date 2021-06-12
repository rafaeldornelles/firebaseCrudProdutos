package br.com.dbserver.lista.retrofitcrudprodutos.retrofit.client

import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import br.com.dbserver.lista.retrofitcrudprodutos.retrofit.AppRetrofit
import br.com.dbserver.lista.retrofitcrudprodutos.retrofit.service.ProdutoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutoClientApi(val produtoService: ProdutoService = AppRetrofit.produtoService) {

    fun listar( onSuccess: (produtos: List<Produto>?) -> Unit,
                onFailure: (erro: String?) -> Unit ){
        produtoService.listar().enqueue(object : Callback<List<Produto>> {
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                onFailure(t.message)
            }

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful){
                    onSuccess(response.body())
                }else{
                    onFailure("Erro na requisição: ${response.code()}")
                }
            }

        })
    }

    fun buscarPorId(id: Int,
                    onSuccess: (produto: Produto?) -> Unit,
                    onFailure: (erro: String?) -> Unit){
        produtoService.buscarPorId(id).enqueue(object : Callback<Produto>{
            override fun onFailure(call: Call<Produto>, t: Throwable) {
                onFailure(t.message)
            }

            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                if (response.isSuccessful){
                    onSuccess(response.body())
                }else{
                    onFailure("Erro na requisição: ${response.code()}")
                }
            }

        })
    }

    fun inserir(produto: Produto,
                onSuccess: (produto: Produto?) -> Unit,
                onFailure: (erro: String?) -> Unit){
        produtoService.inserir(produto).enqueue(object : Callback<Produto>{
            override fun onFailure(call: Call<Produto>, t: Throwable) {
                onFailure(t.message)
            }

            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                if (response.isSuccessful){
                    onSuccess(response.body())
                }else{
                    onFailure("Erro na requisição: ${response.code()}")
                }
            }

        })
    }

    fun deletar(id: Int,
                onSuccess: () -> Unit,
                onFailure: (erro: String?) -> Unit){
        produtoService.deletar(id).enqueue(object : Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onFailure(t.message)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    onSuccess()
                }else{
                    onFailure("Erro na requisição: ${response.code()}")
                }
            }

        })

    }

    fun alterar(id: Int, produto: Produto,
                onSuccess: (Produto?) -> Unit,
                onFailure: (erro: String?) -> Unit){
        produtoService.alterar(id, produto).enqueue(object : Callback<Produto>{
            override fun onFailure(call: Call<Produto>, t: Throwable) {
                onFailure(t.message)
            }

            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                if (response.isSuccessful){
                    onSuccess(response.body())
                }else{
                    onFailure("Erro na requisição: ${response.code()}")
                }            }

        })
    }
}