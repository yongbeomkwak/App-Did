package com.example.appdid.fragment.todo.project

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.R
import com.example.appdid.databinding.LayoutTodoProjectBinding

class TodoProjectAdapter () : ListAdapter<TodoProjectInfo, TodoProjectAdapter.ViewHolder>( // Todo Project 어뎁터
    TodoProjectAdapterDiffCallback()
) {
    lateinit var viewHolder: ViewHolder

    class ViewHolder private constructor(
        val binding: LayoutTodoProjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var linearLayout: LinearLayout
        private lateinit var recyclerView: RecyclerView
        private lateinit var todoListAdapter: TodoListAdapter

        fun bind(item: TodoProjectInfo) {
            binding.todoProjectInfo = item
            binding.executePendingBindings()

            recyclerView = binding.todoListRecycler
            linearLayout = binding.todoProjectLinear
            linearLayout.setOnClickListener { // 뷰 클릭시 하단에 Todo list 보여줌
                when (recyclerView.visibility) {
                    View.GONE -> recyclerView.visibility = View.VISIBLE
                    View.VISIBLE -> recyclerView.visibility = View.GONE
                    else -> {}
                }
            }

            todoListAdapter = TodoListAdapter()
            recyclerView.adapter = todoListAdapter
            todoListAdapter.submitList(item.todos)
        }

        companion object {
            fun from (parent: ViewGroup) : ViewHolder {
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutTodoProjectBinding.inflate(
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

    companion object {
        @JvmStatic
        @BindingAdapter("tools:project_background_tint")
        fun setLayoutBackgroundTint(view: View, color: String) { // Todo Project 배경 색 설정
            view.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(color)
            )
            view.invalidate()
        }

        @JvmStatic
        @BindingAdapter("tools:progresswidth")
        fun setLayoutWidth(view: View, progress: Int) { // 프로그래스바 설정
            view.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    var layoutParams = view.layoutParams
                    layoutParams.width = (view.width.toDouble() / 100 * progress).toInt()
                    view.layoutParams = layoutParams
                    view.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(view.context, R.color.white))
                    view.invalidate()
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

        }
    }
}

class TodoProjectAdapterDiffCallback: DiffUtil.ItemCallback<TodoProjectInfo>() {
    override fun areItemsTheSame(oldItem: TodoProjectInfo, newItem: TodoProjectInfo): Boolean {
        return oldItem._id == newItem._id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TodoProjectInfo, newItem: TodoProjectInfo): Boolean {
        return oldItem == newItem
    }
}