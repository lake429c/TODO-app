package yumemi.todolist

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

class AddButtonListener(
    _context: Context,
    _inputForm: TextView,
    _todolist: ArrayList<TodoItem>,
    _todoListAdapter: TodoListAdapter
): View.OnClickListener{

    private val context = _context
    private val inputForm = _inputForm
    private var todolist = _todolist
    private val todoListAdapter = _todoListAdapter

    override fun onClick(v: View?) {
        if(!inputForm.text.isBlank()){
            // リストに項目を追加
            todolist.add(TodoItem(inputForm.text.toString()))
            todoListAdapter.notifyDataSetChanged()
            // テキストボックスを空に
            inputForm.text = ""
        }else{
            // フォームが空の場合はソフトキーボードを出してフォーカスを当てる
            inputForm.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(inputForm, 0)
        }
    }
}
