package yumemi.todolist

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast

class TodoListViewListener(
    _context: Context,
    _todolist: ArrayList<TodoItem>,
    _todoListAdapter: TodoListAdapter
): AdapterView.OnItemClickListener {

    private val context = _context
    private var todolist = _todolist
    private val todoListAdapter = _todoListAdapter

    override fun onItemClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        when (view?.id) {
            // タップされたのがチェックボックスのとき
            R.id.checkBox -> {
                todolist[position].switchFlg()
                if (todolist[position].getFlg()) {
                    // 未遂->既遂
                    if(todoListAdapter.notDone() == 0) makeToast(context, "Well done!!")
                }
            }
            // タップされたのがテキストのとき
            R.id.textView -> {
            }
            // タップされたのが削除ボタンのとき
            R.id.imageButton -> {
                todolist.removeAt(position)
            }
        }
        // ソフトキーボードを隠す
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        // リストビューを更新する
        todoListAdapter.notifyDataSetChanged()
    }

    fun makeToast(context: Context, _text: String) {
        val toast = Toast.makeText(context, _text, Toast.LENGTH_LONG)
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 300)
        toast.show()
    }
}
