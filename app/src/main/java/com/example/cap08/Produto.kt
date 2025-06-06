package com.example.cap08

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_produtos")
data class Produto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val quantidade: Int,
    val valor: Double,

    val imagemUri: String? = null){

}