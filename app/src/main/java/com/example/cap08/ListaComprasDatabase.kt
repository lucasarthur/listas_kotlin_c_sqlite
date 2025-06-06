package com.example.cap08

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Produto::class], version = 1, exportSchema = false)
abstract class ListaComprasDatabase : RoomDatabase(){
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private var INSTANCE: ListaComprasDatabase? = null

        fun getDatabase(context: Context): ListaComprasDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, ListaComprasDatabase::class.java,
                    "lista_compras.db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}