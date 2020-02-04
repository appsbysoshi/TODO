package com.soshi.todo

import android.content.Context
import java.util.ArrayList

class Presenter() {

    private var context: Context? = null
    private var sqlite: SQLite? = null

    constructor(context: Context): this() {
        this.context = context
        sqlite = SQLite(context)
    }

    //全ItemのArrayListを取得
    internal fun getAllItemArrayList(): ArrayList<Item> {
        val itemArrayList = ArrayList<Item>()
        val writableDB = sqlite!!.writableDatabase
        writableDB.use { db ->
            val cursor = db.rawQuery("select id, content, is_active from ITEM_TABLE order by id", null)
            var next = cursor.moveToFirst()
            while (next) {
                val item = Item()
                val id = cursor.getInt(0)
                val content = cursor.getString(1)
                val isActive = cursor.getInt(2)
                item.id = id
                item.content = content
                item.isActive = (isActive == 0)
                itemArrayList.add(item)
                next = cursor.moveToNext()
            }
            cursor.close()
        }
        return itemArrayList
    }

    //Item追加
    internal fun insertItem(content: String) {
        val contentEscape = escapeSingleQuote(content)
        val writableDB = sqlite!!.writableDatabase
        writableDB.use { db ->
            db.execSQL("insert into ITEM_TABLE(content, is_active) VALUES('$contentEscape', 0)")
        }
    }

    //Item削除
    internal fun deleteItem(id: Int) {
        val writableDB = sqlite!!.writableDatabase
        writableDB.use { db ->
            db.execSQL("delete from ITEM_TABLE where id = $id")
        }
    }

    //Item更新
    internal fun updateItem(id: Int, isActive: Boolean) {
        val isActiveInt = if (isActive) 0
        else 1
        val writableDB = sqlite!!.writableDatabase
        writableDB.use { db ->
            db.execSQL("update ITEM_TABLE set is_active = $isActiveInt where id = $id")
        }
    }

    //Completed Item削除
    internal fun clearCompletedItems() {
        val writableDB = sqlite!!.writableDatabase
        writableDB.use { db ->
            db.execSQL("delete from ITEM_TABLE where is_active = 1")
        }
    }

    //Active Item数を取得
    internal fun getActiveItemCount(): Int {
        var activeItemCount = 0
        val writableDB = sqlite!!.writableDatabase
        writableDB.use { db ->
            val cursor = db.rawQuery("select count(*) from ITEM_TABLE where is_active = 0", null)
            cursor.moveToFirst()
            activeItemCount = cursor.getInt(0)
            cursor.close()
        }
        return activeItemCount
    }

    //エスケープ処理
    private fun escapeSingleQuote(value: String): String {
        return value.replace("'", "''")
    }

    //Active ItemのArrayListを取得
    internal fun getActiveItemArrayList(itemArrayList: ArrayList<Item>): ArrayList<Item> {
        val activeItemArrayList = ArrayList<Item>()
        for (item in itemArrayList) {
            if (item.isActive) {
                activeItemArrayList.add(item)
            }
        }
        return activeItemArrayList
    }

    //Completed ItemのArrayListを取得
    internal fun getCompletedItemArrayList(itemArrayList: ArrayList<Item>): ArrayList<Item> {
        val completedItemArrayList = ArrayList<Item>()
        for (item in itemArrayList) {
            if (!item.isActive) {
                completedItemArrayList.add(item)
            }
        }
        return completedItemArrayList
    }
}