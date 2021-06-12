package br.com.dbserver.lista.retrofitcrudprodutos.firebase.extensions


import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot


private const val CHAVE_NOME = "nome"
private const val CHAVE_PRECO = "preco"

fun Produto.toHashMap(): HashMap<String, Any>{
    return hashMapOf(
        "nome" to this.nome,
        "preco" to this.preco
    )
}

fun Produto.Companion.fromHashMap(document: QueryDocumentSnapshot): Produto{
    return Produto(
        document.id,
        document.get(CHAVE_NOME) as String,
        document.get(CHAVE_PRECO).toString().toDouble()
    )
}
fun Produto.Companion.fromHashMap(document: DocumentSnapshot): Produto{
    return Produto(
        document.id,
        document.get(CHAVE_NOME) as String,
        document.get(CHAVE_PRECO).toString().toDouble()
    )}

