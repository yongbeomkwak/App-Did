package com.example.appdid.fragment.todo.project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.dto.CalendarDayTodoDTO
import com.example.appdid.databinding.LayoutTodoBinding

class TodoListAdapter () : ListAdapter<CalendarDayTodoDTO, TodoListAdapter.ViewHolder> ( // Todo project 속 todo list recyclerview 어뎁터
    TodoListAdapterDiffCallback()
        ){

    lateinit var viewHolder: ViewHolder

    class ViewHolder private constructor(
        val binding: LayoutTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var checkTodo: CheckBox
        private lateinit var buttonDelete: ImageButton

        fun bind(item: CalendarDayTodoDTO) {
            binding.todoDTO = item
            binding.executePendingBindings()

            checkTodo = binding.checkTodo
            checkTodo.setOnCheckedChangeListener { buttonView, isChecked ->
                item.check = isChecked
            }

            buttonDelete = binding.buttonTodoDelete
            buttonDelete.setOnClickListener{
            }
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutTodoBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        viewHolder = holder
    }

    companion object { // todo체크됐을 때 체크 데이터 저장
        @JvmStatic
        @BindingAdapter("tools:todo_checked")
        fun setChecked(view: View, checked: Boolean) {
            (view as CheckBox).isChecked = checked
            view.invalidate()
        }
    }
}

class TodoListAdapterDiffCallback : DiffUtil.ItemCallback<CalendarDayTodoDTO>() {
    override fun areItemsTheSame(
        oldItem: CalendarDayTodoDTO,
        newItem: CalendarDayTodoDTO
    ): Boolean {
        return oldItem._id == newItem._id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: CalendarDayTodoDTO,
        newItem: CalendarDayTodoDTO
    ): Boolean {
        return oldItem == newItem
    }
}