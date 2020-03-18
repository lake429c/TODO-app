package yumemi.todolist

class TodoItem(_name: String) {

    private var done: Boolean = false
    private var name: String = _name

    fun getFlg() : Boolean {
        return this.done
    }

    fun getName() : String {
        return this.name
    }

    fun switchFlg() {
        this.done = !this.done
    }

    fun setName(_name: String) {
        this.name = _name
    }

}
