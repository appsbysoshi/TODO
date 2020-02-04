package com.soshi.todo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val p = Presenter(this)
        var allItemArrayList = p.getAllItemArrayList()
        val allBtn: Button = findViewById(R.id.all_btn)
        val activeBtn: Button = findViewById(R.id.active_btn)
        val completedBtn: Button = findViewById(R.id.completed_btn)

        //初期状態
        allBtn.setBackgroundResource(R.drawable.btn_selected)
        var filter = 0 //0:All, 1:Active, 2:Completed

        val recycler: RecyclerView = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = Adapter(allItemArrayList, this)

        //Active Item数を表示
        var activeItemCount = p.getActiveItemCount()
        val leftItemsNum: TextView = findViewById(R.id.left_items_num)
        leftItemsNum.text = activeItemCount.toString()

        //Allボタン処理
        allBtn.setOnClickListener{
            allItemArrayList = p.getAllItemArrayList()
            adapter.updateAdapter(allItemArrayList)
            allBtn.setBackgroundResource(R.drawable.btn_selected)
            activeBtn.setBackgroundResource(R.drawable.btn_normal)
            completedBtn.setBackgroundResource(R.drawable.btn_normal)
            filter = 0
        }

        //Activeボタン処理
        activeBtn.setOnClickListener{
            val activeItemArrayList = p.getActiveItemArrayList(allItemArrayList)
            adapter.updateAdapter(activeItemArrayList)
            allBtn.setBackgroundResource(R.drawable.btn_normal)
            activeBtn.setBackgroundResource(R.drawable.btn_selected)
            completedBtn.setBackgroundResource(R.drawable.btn_normal)
            filter = 1
        }

        //Completedボタン処理
        completedBtn.setOnClickListener{
            val completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
            adapter.updateAdapter(completedItemArrayList)
            allBtn.setBackgroundResource(R.drawable.btn_normal)
            activeBtn.setBackgroundResource(R.drawable.btn_normal)
            completedBtn.setBackgroundResource(R.drawable.btn_selected)
            filter = 2
        }

        //ClearCompletedボタン処理
        val clearCompletedBtn: Button = findViewById(R.id.clear_completed_btn)
        clearCompletedBtn.setOnClickListener{
            p.clearCompletedItems()
            allItemArrayList = p.getAllItemArrayList()
            when (filter) {
                0 -> {
                    adapter.updateAdapter(allItemArrayList)
                }
                1 -> {
                    //Viewの更新は不要
                }
                2 -> {
                    val completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
                    adapter.updateAdapter(completedItemArrayList)
                }
            }
        }

        //Item追加処理
        val editText: EditText = findViewById(R.id.edit_text)
        val enterBtn: Button = findViewById(R.id.enter_btn)
        enterBtn.setOnClickListener {
            val content = editText.text.toString()
            if (content != "") {
                p.insertItem(content)
                allItemArrayList = p.getAllItemArrayList()
                when (filter) {
                    0 -> {
                        adapter.updateAdapter(allItemArrayList)
                    }
                    1 -> {
                        val activeItemArrayList = p.getActiveItemArrayList(allItemArrayList)
                        adapter.updateAdapter(activeItemArrayList)
                    }
                    2 -> {
                        val completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
                        adapter.updateAdapter(completedItemArrayList)
                    }
                }
                editText.editableText.clear()
                //Active Item数を更新
                activeItemCount = p.getActiveItemCount()
                leftItemsNum.text = activeItemCount.toString()
            }
        }

        adapter.setOnItemClickListener(object : Adapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, isChecked: Boolean?) {
                val resourceID = view.resources.getResourceEntryName(view.id)
                if (resourceID == "select_btn") {
                    //チェック処理
                    when (filter) {
                        0 -> {
                            val id = allItemArrayList[position].id
                            val isActive = !isChecked!!
                            p.updateItem(id, isActive)
                            allItemArrayList = p.getAllItemArrayList()
                            adapter.updateAdapter(allItemArrayList)
                        }
                        1 -> {
                            var activeItemArrayList = p.getActiveItemArrayList(allItemArrayList)
                            val id = activeItemArrayList[position].id
                            val isActive = !isChecked!!
                            p.updateItem(id, isActive)
                            allItemArrayList = p.getAllItemArrayList()
                            activeItemArrayList = p.getActiveItemArrayList(allItemArrayList)
                            adapter.updateAdapter(activeItemArrayList)
                        }
                        2 -> {
                            var completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
                            val id = completedItemArrayList[position].id
                            val isActive = !isChecked!!
                            p.updateItem(id, isActive)
                            allItemArrayList = p.getAllItemArrayList()
                            completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
                            adapter.updateAdapter(completedItemArrayList)
                        }
                    }
                    //Active Item数を更新
                    activeItemCount = p.getActiveItemCount()
                    leftItemsNum.text = activeItemCount.toString()
                } else if (resourceID == "delete_btn") {
                    //削除ボタン処理
                    when (filter) {
                        0 -> {
                            val id = allItemArrayList[position].id
                            p.deleteItem(id)
                            allItemArrayList = p.getAllItemArrayList()
                            adapter.updateAdapter(allItemArrayList)
                        }
                        1 -> {
                            var activeItemArrayList = p.getActiveItemArrayList(allItemArrayList)
                            val id = activeItemArrayList[position].id
                            p.deleteItem(id)
                            allItemArrayList = p.getAllItemArrayList()
                            activeItemArrayList = p.getActiveItemArrayList(allItemArrayList)
                            adapter.updateAdapter(activeItemArrayList)
                        }
                        2 -> {
                            var completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
                            val id = completedItemArrayList[position].id
                            p.deleteItem(id)
                            allItemArrayList = p.getAllItemArrayList()
                            completedItemArrayList = p.getCompletedItemArrayList(allItemArrayList)
                            adapter.updateAdapter(completedItemArrayList)
                        }
                    }
                    //Active Item数を更新
                    activeItemCount = p.getActiveItemCount()
                    leftItemsNum.text = activeItemCount.toString()
                }
            }
        })
        recycler.adapter = adapter
    }
}