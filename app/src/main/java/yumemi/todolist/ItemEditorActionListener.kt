package yumemi.todolist

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

class ItemEditorActionListener(
    _context: Context,
    _todolist: ArrayList<TodoItem>,
    _position: Int
): TextView.OnEditorActionListener {

    private val context = _context
    private val todolist = _todolist
    private var position = _position

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if(v.text.isNotBlank()){
            // ソフトキーボードの決定が押されたら内容更新
            todolist[position].setName(v.text.toString())
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        return true
    }
}
