package com.example.cap08

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert
    suspend fun inserir(produto: Produto)
    @Query("SELECT * FROM tabela_produtos ORDER BY nome ASC")
    fun getTodos(): Flow<List<Produto>>
    @Delete
    suspend fun deletar(produto: Produto)
}