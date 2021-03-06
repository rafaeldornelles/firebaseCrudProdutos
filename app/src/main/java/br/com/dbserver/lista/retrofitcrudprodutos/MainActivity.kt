package br.com.dbserver.lista.retrofitcrudprodutos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.retrofitcrudprodutos.adapter.ProdutoAdapter
import br.com.dbserver.lista.retrofitcrudprodutos.firebase.ProdutoClientFirestore
import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val CHAVE_EXTRA_PRODUTO = "produto"
const val REQUEST_CODE_ADICIONAR_PRODUTO = 1
const val REQUEST_CODE_EDITAR_PRODUTO = 2

class MainActivity : AppCompatActivity(), ProdutoAdapter.OnProdutoClickListener {
    override fun onProdutoClick(produto: Produto) {
        val intent = Intent(this, FormProdutoActivity::class.java).apply {
            putExtra(CHAVE_EXTRA_PRODUTO, produto)
            startActivityForResult(this, REQUEST_CODE_EDITAR_PRODUTO)
        }
    }

    val rvProdutos by lazy { findViewById<RecyclerView>(R.id.produto_recyclerview) }
    val fabAddProduto by lazy { findViewById<FloatingActionButton>(R.id.fab_adicionar_produto) }
    val produtos: MutableList<Produto> = ArrayList()
    val produtoAdapter = ProdutoAdapter(produtos, this)

    val produtoClientFirestore = ProdutoClientFirestore()

    val userName: String by lazy{
        getSharedPreferences(NOME_SHARED_PREFERENCES, Context.MODE_PRIVATE).run {
            if (contains(CHAVE_USERNAME_PREFERENCE))
                getString(CHAVE_USERNAME_PREFERENCE, null)?: "Anônimo"
            else if (contains(CHAVE_EMAIL_PREFERENCE))
                getString(CHAVE_EMAIL_PREFERENCE, null)?: "Anônimo"
            else "Anônimo"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Bem vindo, $userName"
        setContentView(R.layout.activity_main)

        rvProdutos.adapter = produtoAdapter
        fabAddProduto.setOnClickListener {
            startActivityForResult(Intent(this, FormProdutoActivity::class.java), REQUEST_CODE_ADICIONAR_PRODUTO)
        }

    }

    override fun onResume() {
        super.onResume()
        carregarProdutos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            data?.getParcelableExtra<Produto>(CHAVE_EXTRA_PRODUTO)?.let {
                when (requestCode) {
                    REQUEST_CODE_ADICIONAR_PRODUTO -> adicionarProduto(it)
                    REQUEST_CODE_EDITAR_PRODUTO -> editarProduto(it)
                }
            }
        } else if (resultCode == RESULT_REMOVED){
            data?.getParcelableExtra<Produto>(CHAVE_EXTRA_PRODUTO)?.let{
                removerProduto(it)
            }
        }
    }

    fun adicionarProduto(produto:Produto){
        val onSuccess: (produtoAdicionado: Produto?) -> Unit = {
            it?.let {
                produtos.add(it)
                produtoAdapter.notifyDataSetChanged()
            }
        }
        val onFailure: (erro:String?) -> Unit = {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG)
            }
        }

        produtoClientFirestore.inserir(produto, onSuccess, onFailure)
    }

    fun editarProduto(produto: Produto){
        val onSuccess: (produtoAdicionado: Produto?) -> Unit = {
            it?.let {
                produtos.find {prod ->
                    it.id == prod.id
                }?.apply {
                    this.nome = it.nome
                    this.preco = it.preco
                }
                produtoAdapter.notifyDataSetChanged()
            }
        }
        val onFailure: (erro:String?) -> Unit = {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG)
            }
        }
        produtoClientFirestore.alterar(produto.id!!, produto, onSuccess, onFailure)
    }

    fun removerProduto(produto: Produto){
        val onSuccess: () -> Unit = {
            produtos.indexOfFirst { produto.id == it.id }.apply {
                if (this>=0 && this < produtos.size){
                    produtos.removeAt(this)
                    produtoAdapter.notifyItemRemoved(this)
                }
            }
        }
        val onFailure: (erro:String?) -> Unit = {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG)
            }
        }

        produtoClientFirestore.deletar(produto.id!!, onSuccess, onFailure)
    }

    fun carregarProdutos(){
        val onSuccess: (produtos: List<Produto>?) -> Unit = {
            it?.let {
                produtos.clear()
                produtos.addAll(it)
                produtoAdapter.notifyDataSetChanged()
            }
        }
        val onFailure: (erro: String?) -> Unit = {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        produtoClientFirestore.listar(onSuccess, onFailure)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_recarregar -> { carregarProdutos() }
        }
        return super.onOptionsItemSelected(item)
    }
}
