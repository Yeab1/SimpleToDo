package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // remove item from list
                listOfTasks.removeAt(position)
                // notify adapter of the changes
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        // get tasks from file
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up button and input field. (adding tasks)
        val inputField = findViewById<EditText>(R.id.addTaskField)
        // get reference to the button and add a listener.
        findViewById<Button>(R.id.button).setOnClickListener {
            // get user input text
            val userInputtedTask = inputField.text.toString()

            // add string to list of Tasks
            listOfTasks.add(userInputtedTask)

            // notify adapter of the changes
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // clear text field
            inputField.setText("")

            saveItems()
        }
    }

    // save user input
    // get the file
    fun getDataFile():File{
        return File(filesDir, "data.txt")
    }
    // load items from file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException:IOException){
            ioException.printStackTrace()
        }
    }
    // save items to file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch(ioException:IOException){
            ioException.printStackTrace()
        }
    }
}