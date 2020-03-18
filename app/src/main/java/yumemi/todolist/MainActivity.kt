package yumemi.todolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private val fileName = "todoitems.txt"

    private lateinit var inputForm: EditText
    private lateinit var todoListView: ListView
    private lateinit var addButton: ImageButton

    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var todoListViewListener: TodoListViewListener
    private lateinit var addButtonListener: AddButtonListener

    private var todolist: ArrayList<TodoItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODOリストのファイルが存在するなら読み込む
        if(File(applicationContext.filesDir, fileName).exists()) {
            val fis: FileInputStream = openFileInput(fileName)
            val isr = InputStreamReader(fis)
            val reader = BufferedReader(isr)

            while (true) {
                val done = reader.readLine() ?: break
                val name = reader.readLine()
                val item = TodoItem(name)
                if (done.toBoolean()) item.switchFlg()
                todolist.add(item)
            }
            fis.close()
        }

        inputForm = findViewById(R.id.inputForm)
        todoListView = findViewById(R.id.todoListView)
        addButton = findViewById(R.id.addButton)

        todoListAdapter = TodoListAdapter(this, todolist, fileName)
        todoListViewListener = TodoListViewListener(this, todolist, todoListAdapter)
        addButtonListener = AddButtonListener(this, inputForm, todolist, todoListAdapter)

        todoListView.adapter = todoListAdapter
        todoListView.onItemClickListener = todoListViewListener
        addButton.setOnClickListener(addButtonListener)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all -> todoListAdapter.setFlag(null)
            R.id.active -> todoListAdapter.setFlag(false)
            R.id.completed -> todoListAdapter.setFlag(true)
        }
        // ソフトキーボードを隠す
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputForm.windowToken, 0)
        // リストを更新
        todoListAdapter.notifyDataSetChanged()
        return true
    }

}
