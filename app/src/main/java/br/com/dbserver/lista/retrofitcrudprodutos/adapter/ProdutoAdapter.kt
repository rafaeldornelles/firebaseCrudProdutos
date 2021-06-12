package br.com.dbserver.lista.retrofitcrudprodutos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.retrofitcrudprodutos.R
import br.com.dbserver.lista.retrofitcrudprodutos.model.Produto
import com.google.android.material.card.MaterialCardView

class ProdutoAdapter(var produtos: MutableList<Produto>, val listener: OnProdutoClickListener): RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return produtos.size
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.bind(produto, listener)
    }

    inner class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvNomeProduto: TextView = view.findViewById(R.id.tv_nome_produto)
        val tvPrecoProduto : TextView = view.findViewById(R.id.tv_preco_produto)
        val cardProduto: MaterialCardView = view.findViewById(R.id.card_produto)

        fun bind(produto:Produto, listener: OnProdutoClickListener){
            tvNomeProduto.text = produto.nome
            val precoFormatado = "R$ ${String.format("%.2f", produto.preco).replace('.',',')}"
            tvPrecoProduto.text = precoFormatado
            cardProduto.setOnClickListener {
                listener.onProdutoClick(produto)
            }
        }
    }

    interface OnProdutoClickListener{
        fun onProdutoClick(produto: Produto)
    }
}