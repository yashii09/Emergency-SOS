package com.example.myfamily

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class],version = 1, exportSchema = false)
public abstract class MyFamilyDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDao

     companion object{

         @Volatile
         private var INSTANCE: MyFamilyDatabase? = null

         fun getDatabse(context: Context): MyFamilyDatabase{

             INSTANCE?.let {
                 return it
             }

             return synchronized(MyFamilyDatabase::class.java){
                 val instance= Room.databaseBuilder(
                     context.applicationContext,
                     MyFamilyDatabase::class.java,
                     "my_family_data"
                 ).build()

                 INSTANCE = instance
                 instance
             }

        }

    }

}