package com.example.cap08

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

class CadastroActivity : AppCompatActivity() {

    var imageBitmap: Bitmap? = null
    val COD_IMAGE = 101
    private var selectedImageUri: Uri? = null
    private lateinit var img_foto_produto: ImageView

    fun abrirGaleria(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.type = "image/*"

        startActivityForResult(Intent.createChooser(intent, "Selecione a imagem"), COD_IMAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        img_foto_produto = findViewById(R.id.img_foto_produto)

        img_foto_produto.setOnClickListener	{
            abrirGaleria()
        }

        val btn_inserir = findViewById<Button>(R.id.btn_inserir)
        val txt_produto = findViewById<EditText>(R.id.txt_produto)
        val txt_qtd = findViewById<EditText>(R.id.txt_qtd)
        val txt_valor = findViewById<EditText>(R.id.txt_valor)

        btn_inserir.setOnClickListener {
            val produtoNome = txt_produto.text.toString()
            val qtd = txt_qtd.text.toString()
            val valor = txt_valor.text.toString()

            if(produtoNome.isNotEmpty() && qtd.isNotEmpty() && valor.isNotEmpty()) {
                val prod = Produto(
                    nome = produtoNome,
                    quantidade = qtd.toInt(),
                    valor = valor.toDouble(),
                    imagemUri = selectedImageUri?.toString()
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    val db = ListaComprasDatabase.getDatabase(applicationContext)
                    db.produtoDao().inserir(prod)
                }

                finish()

            }else{
                txt_produto.error = if (txt_produto.text.isEmpty()) "Preencha o nome do produto" else null
                txt_qtd.error = if (txt_qtd.text.isEmpty()) "Preencha a quantidade" else null
                txt_valor.error = if (txt_valor.text.isEmpty()) "Preencha o valor" else null
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == COD_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data?.data != null) {

                selectedImageUri = data.data
                var inputStream: InputStream? = null

                try {
                    inputStream = contentResolver.openInputStream(selectedImageUri!!)
                    imageBitmap = BitmapFactory.decodeStream(inputStream)
                    img_foto_produto.setImageBitmap(imageBitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    inputStream?.close()
                }
            }
        }
    }
}