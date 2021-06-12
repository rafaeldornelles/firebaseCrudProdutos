package br.com.dbserver.lista.retrofitcrudprodutos

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import com.google.android.material.button.MaterialButton

const val RESULT_REMOVED = 9

class FormProdutoActivity : AppCompatActivity() {
    val etNomeProduto: EditText by lazy{ findViewById<EditText>(R.id.et_nome_produto)}
    val etPrecoProduto: EditText by lazy{ findViewById<EditText>(R.id.et_preco_produto)}
    val btSalvar: MaterialButton by lazy{ findViewById<MaterialButton>(R.id.bt_salvar_produto)}

    lateinit var produto: Produto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_produto)

        produto = intent.hasExtra(CHAVE_EXTRA_PRODUTO).let {
            intent.getParcelableExtra<Produto>(CHAVE_EXTRA_PRODUTO)
        } ?: Produto(0, "", 0.0)

        if (produto.id != 0){
            etNomeProduto.setText(produto.nome)
            etPrecoProduto.setText(String.format("%.2f", produto.preco))
        }

        btSalvar.setOnClickListener{
            val nomeProduto = etNomeProduto.text.toString()
            val precoProduto = etPrecoProduto.text.toString()
            if (nomeProduto.isNotEmpty() && precoProduto.isNotEmpty()){
                produto.nome = nomeProduto
                produto.preco = precoProduto.toDouble()
                val resultIntent = Intent().apply {
                    putExtra(CHAVE_EXTRA_PRODUTO, produto)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_remove, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete -> {
                AlertDialog.Builder(this)
                    .setTitle("Atenção")
                    .setMessage("Tem certeza de que deseja remover o ítem ${produto.nome}?")
                    .setPositiveButton("Sim"){ dialogInterface, i ->
                        val resultIntent = Intent().apply {
                            putExtra(CHAVE_EXTRA_PRODUTO, produto)
                        }
                        setResult(RESULT_REMOVED, resultIntent)
                        finish()
                    }
                    .setNegativeButton("Não", null)
                    .create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
