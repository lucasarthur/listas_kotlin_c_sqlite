package com.example.cap08

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale
import coil.load

class ProdutoAdapter(contexto: Context) : ArrayAdapter<Produto>(contexto, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View

        if (convertView == null) {
            v = LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false)
        } else {
            v = convertView
        }

        val txt_produto = v.findViewById<TextView>(R.id.txt_item_produto)
        val txt_qtd = v.findViewById<TextView>(R.id.txt_item_qtd)
        val txt_valor = v.findViewById<TextView>(R.id.txt_item_valor)
        val img_produto = v.findViewById<ImageView>(R.id.img_item_foto)

        val item = getItem(position)


        item?.let { currentItem ->
            txt_produto.text = currentItem.nome
            txt_qtd.text = currentItem.quantidade.toString()

            val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            txt_valor.text = formatadorMoeda.format(currentItem.valor)

            if (currentItem.imagemUri != null) {
                img_produto.load(
                    Uri.parse(currentItem.imagemUri))
                img_produto.visibility = View.VISIBLE
            } else {

                img_produto.visibility = View.INVISIBLE
            }
        } ?: run {

            txt_produto.text = ""
            txt_qtd.text = ""
            txt_valor.text = ""

            img_produto.visibility = View.GONE
        }

        return v
    }
}