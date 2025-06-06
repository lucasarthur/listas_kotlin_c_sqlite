package com.example.cap08

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity:AppCompatActivity() {

    private lateinit var list_view_produtos: ListView
    private lateinit var txt_total: TextView
    private lateinit var produtosAdapter: ProdutoAdapter

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list_view_produtos = findViewById(R.id.list_view_produtos)
        txt_total = findViewById(R.id.txt_total)

        val btn_adicionar = findViewById<Button>(R.id.btn_adicionar)

        produtosAdapter = ProdutoAdapter(this)
        list_view_produtos.adapter = produtosAdapter

        iniciarObservadorDoBanco()

        list_view_produtos.setOnItemLongClickListener { adapterView, view, position, id ->
            val item = produtosAdapter.getItem(position)
            if (item != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val db = ListaComprasDatabase.getDatabase(applicationContext)
                    db.produtoDao().deletar(item)
                }
            }
            true
        }

        btn_adicionar.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarObservadorDoBanco() {
        lifecycleScope.launch {
            val db = ListaComprasDatabase.getDatabase(applicationContext)
            db.produtoDao().getTodos().collect { listaDeProdutos ->
                produtosAdapter.clear()
                produtosAdapter.addAll(listaDeProdutos)

                val soma = listaDeProdutos.sumOf {it.valor * it.quantidade}
                val f = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                txt_total.text = "TOTAL: ${f.format(soma)}"
            }
        }
    }
}
