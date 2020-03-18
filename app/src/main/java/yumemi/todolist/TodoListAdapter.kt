package yumemi.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.File

class TodoListAdapter(
    _context: Context,
    _todolist: ArrayList<TodoItem>,
    _fileName: String
): BaseAdapter() {

    private val context = _context
    private var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var todolist = _todolist
    private val fileName = _fileName
    private var searchFlag: Boolean? = null

    // リストビューの更新の際の処理
    @SuppressLint("ViewHolder")
    override fun getView(
        _position: Int,
        _convertView: View?,
        _parent: ViewGroup
    ): View {
        val convertView = layoutInflater.inflate(R.layout.todo_item, _parent, false)
        val checkBox = convertView?.findViewById(R.id.checkBox) as CheckBox
        val textView = convertView.findViewById(R.id.textView) as TextView
        val imageButton = convertView.findViewById(R.id.imageButton) as ImageButton
        if(todolist.isNotEmpty()) {
            checkBox.isEnabled = true
            textView.isEnabled = true
            imageButton.isEnabled = true
            textView.text = todolist[_position].getName()
            // 各項目のフラグの状態によって，チェックボックスにチェックしたり，テキストに取り消し線いれたりする
            checkBox.isChecked = todolist[_position].getFlg()
            if (todolist[_position].getFlg()){
                textView.setTextColor(Color.GRAY)
                textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                textView.setTextColor(Color.BLACK)
                textView.paintFlags = Paint.ANTI_ALIAS_FLAG
            }
            // 絞り込みによって濃さを変え,操作を制限する
            if (searchFlag != null && searchFlag != todolist[_position].getFlg()) {
                checkBox.isEnabled = false
                textView.isEnabled = false
                imageButton.isEnabled = false
                convertView.alpha = 0.1F
            }
        }
        // 各項目内のビューにリスナーをセット
        textView.setOnClickListener { view ->
            (_parent as ListView).performItemClick(
                view,
                _position,
                R.id.textView.toLong()
            )
        }
        val itemEditorActionListener = ItemEditorActionListener(context, todolist, _position)
        textView.setOnEditorActionListener(itemEditorActionListener)
        textView.setOnFocusChangeListener{
            v: View?,
            hasFocus: Boolean ->
            // フォーカスが離れた時点での文字列をtodolistに格納
            if(!hasFocus) {
                todolist[_position].setName((v as TextView).text.toString())
            }
        }
        checkBox.setOnClickListener { view ->
            (_parent as ListView).performItemClick(
                view,
                _position,
                R.id.checkBox.toLong()
            )
        }
        imageButton.setOnClickListener { view ->
            (_parent as ListView).performItemClick(
                view,
                _position,
                R.id.imageButton.toLong()
            )
        }
        return convertView
    }

    fun setFlag(_searchFlag: Boolean?) {
        this.searchFlag = _searchFlag
    }

    fun notDone(): Int{
        var cnt = 0
        for (item in todolist) {
            if (!item.getFlg()) cnt += 1
        }
        return cnt
    }

    // リストビュー更新のタイミングでデータを保存する
    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        File(context.filesDir, fileName).writer().use {
            for(item in todolist) {
                it.write(item.getFlg().toString()+"\n")
                it.write(item.getName()+"\n")
            }
        }
    }

    override fun getItem(position: Int): Any {
        return todolist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return todolist.size
    }
}

