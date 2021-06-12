package br.com.dbserver.lista.retrofitcrudprodutos.firebase

import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import br.com.dbserver.lista.retrofitcrudprodutos.firebase.extensions.fromHashMap
import br.com.dbserver.lista.retrofitcrudprodutos.firebase.extensions.toHashMap

class ProdutoClientFirestore {
    val db = Firebase.firestore
    val produtosCollection = db.collection("produtos")


    fun listar(
        onSuccess: (List<Produto>) -> Unit,
        onFailure: (message:String?) -> Unit
    ) {
        produtosCollection.get()
            .addOnSuccessListener {result ->
                val produtos = result.map {
                    Produto.fromHashMap(it)
                }
                onSuccess(produtos)
            }.addOnFailureListener{
                onFailure(it.message)
            }
    }

    fun buscarPorId(id: String,
                    onSuccess: (Produto) -> Unit,
                    onFailure: (message: String?) -> Unit){
        produtosCollection.document(id).get()
            .addOnSuccessListener {
                onSuccess(Produto.fromHashMap(it))
            }.addOnFailureListener{
                onFailure(it.message)
            }
    }

    fun inserir(produto: Produto,
                onSuccess: (Produto) -> Unit,
                onFailure: (message: String?) -> Unit){
        produtosCollection.add(produto.toHashMap()).addOnSuccessListener {
            onSuccess(Produto(it.id, produto.nome, produto.preco))
        }.addOnFailureListener{
            onFailure(it.message)
        }
    }

    fun deletar(id:String,
                onSuccess: () -> Unit,
                onFailure: (message: String?) -> Unit){
        produtosCollection.document(id).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener{
            onFailure(it.message)
        }
    }

    fun alterar(id: String,
                  produto: Produto,
                  onSuccess: (Produto) -> Unit,
                  onFailure: (message: String?) -> Unit){
        produtosCollection.document(id).set(produto.toHashMap()).addOnSuccessListener {
            onSuccess(Produto(id, produto.nome, produto.preco))
        }.addOnFailureListener{
            onFailure(it.message)
        }
    }
}

