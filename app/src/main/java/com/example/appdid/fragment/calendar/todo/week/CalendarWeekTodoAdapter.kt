package com.example.appdid.fragment.calendar.todo.week

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.R
import com.example.appdid.databinding.LayoutWeekTodoBinding
import com.example.appdid.fragment.calendar.todo.day.CalendarDayTodoInfo
import java.util.*
import kotlin.collections.ArrayList

class CalendarWeekTodoAdapter () : ListAdapter<CalendarWeekTodoInfo, CalendarWeekTodoAdapter.ViewHolder>(
    CalendarWeekTodoAdapterDiffCallback()
) {
    lateinit var viewHolder: ViewHolder

    class ViewHolder private constructor ( // RecyclerView 사용을 위한 Holder
        val binding: LayoutWeekTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarWeekTodoInfo) { // Todo 바인딩
            binding.calendarWeekTodoInfo = item
            binding.weekTodoTable.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.weekTodoTable.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    setDayTodoList(item)
                }
            })
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup) : ViewHolder { // view 바인딩
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutWeekTodoBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding)
            }
        }

        fun setDayTodoList(item: CalendarWeekTodoInfo) { // Todo List를 겹치지 않게 표기하기 위한 가공
            var todoList: MutableList<CalendarDayTodoInfo> = item.calendarDayTodoInfoWeek
            var gridRowStack = ArrayList<Int>(0)
            for (i in 0 until todoList.size) {

                for (j in 0 until gridRowStack.size) {
                    if (gridRowStack[j] <= todoList[i].beginCalendar.get(Calendar.DAY_OF_WEEK)) {
                        gridRowStack[j] = todoList[i].endCalendar.get(Calendar.DAY_OF_WEEK)+1
                        todoList[i].row = j
                        break
                    }
                }
                if (todoList[i].row == -1) {
                    gridRowStack.add(todoList[i].endCalendar.get(Calendar.DAY_OF_WEEK) + 1)
                    todoList[i].row = gridRowStack.size - 1
                }
            }
            return initDayTodoList(todoList, gridRowStack)
        }

        fun initDayTodoList(todoList: MutableList<CalendarDayTodoInfo>, gridRowStack: ArrayList<Int>) { // 화면에 Todo List 표시
            todoList.sortBy { it.row }

            var tableLayout = binding.weekTodoTable
            for (i in 0 until gridRowStack.size) {
                var tableRow = TableRow(binding.root.context)
                tableLayout.addView(tableRow)
            }

            for (i in 0 until todoList.size) {
                var block: TextView = TextView(binding.root.context)
                block.setText(todoList[i].title)
                block.setBackgroundResource(R.drawable.calendar_todo_round)
                block.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, todoList[i].color))
                block.typeface = Typeface.DEFAULT_BOLD
                block.setPadding(10, 0, 0, 0)

                var tableRow: TableRow
                tableRow = tableLayout.getChildAt(todoList[i].row + 1) as TableRow
                tableRow.addView(block)
                val blockParams = TableRow.LayoutParams(tableRow.layoutParams)
                blockParams.column = todoList[i].beginCalendar.get(Calendar.DAY_OF_WEEK) - 1
                blockParams.span = todoList[i].endCalendar.get(Calendar.DAY_OF_WEEK) -
                        todoList[i].beginCalendar.get(Calendar.DAY_OF_WEEK) + 1
                blockParams.width = 0
                blockParams.height = 50
                blockParams.setMargins(3, 3, 3, 3)
                tableRow.getChildAt(tableRow.childCount - 1).layoutParams = blockParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // 달력 주 생성시 호출
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // position번째 주 바인딩
        val item = getItem(position)
        holder.bind(item)
        viewHolder = holder
    }

    companion object {
        @JvmStatic
        @BindingAdapter("tools:height")
        fun setLayoutHeight(view: View, height: Int) { // 해당 주차 높이 조절
            val layoutParams = view.layoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
            view.invalidate()
        }
    }
}

class CalendarWeekTodoAdapterDiffCallback: DiffUtil.ItemCallback<CalendarWeekTodoInfo>() { // RecyclerView 전환시 두 목록의 차이점을 찾아 업데이트
    override fun areItemsTheSame(
        oldItem: CalendarWeekTodoInfo,
        newItem: CalendarWeekTodoInfo
    ): Boolean {
        return oldItem.week == newItem.week
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: CalendarWeekTodoInfo,
        newItem: CalendarWeekTodoInfo
    ): Boolean {
        return oldItem == newItem
    }

}