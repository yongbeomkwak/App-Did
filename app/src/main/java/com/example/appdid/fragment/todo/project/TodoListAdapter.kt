package com.example.appdid.fragment.todo.project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.dto.CalendarDayTodoDTO
import com.example.appdid.databinding.LayoutTodoBinding
import com.example.appdid.dto.CodeMessageDTO
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TodoListAdapter () : ListAdapter<CalendarDayTodoDTO, TodoListAdapter.ViewHolder> ( // Todo project 속 todo list recyclerview 어뎁터
    TodoListAdapterDiffCallback()
        ){

    lateinit var viewHolder: ViewHolder

    class ViewHolder private constructor(
        val binding: LayoutTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var checkboxTodo: CheckBox
        private lateinit var buttonDelete: ImageButton
        private lateinit var textTitle: TextView

        private lateinit var todoRelative: RelativeLayout

        fun bind(item: CalendarDayTodoDTO) {
            binding.todoDTO = item
            binding.executePendingBindings()

            todoRelative = binding.todoRelative

            textTitle = binding.textTodoTitle

            checkboxTodo = binding.checkTodo
            checkboxTodo.setOnCheckedChangeListener { buttonView, isChecked ->
                item.check = isChecked
                checkTodo(item._id, isChecked)
            }

            buttonDelete = binding.buttonTodoDelete
            buttonDelete.setOnClickListener{
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Todo 삭제")
                builder.setMessage(textTitle.text.toString() + " Todo를 삭제합니다.")
                builder.setPositiveButton("예"){ dialog, id ->
                    deleteTodo(item._id)
                }
                builder.setNegativeButton("아니오", null)
                builder.setNeutralButton("취소", null)
                builder.create().show()
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

        fun checkTodo(id: String ,checked: Boolean) { // Todo 체크시 서버에 전송
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
            val service: RetrofitService =retrofit.create(RetrofitService::class.java)
            val call: Call<CodeMessageDTO> = service.checkTodo(
                id,
                checked,
                MyApplication.prefs.getString("token")
            )

            call.enqueue(object : Callback<CodeMessageDTO> {
                override fun onResponse(
                    call: Call<CodeMessageDTO>,
                    response: Response<CodeMessageDTO>
                ) {
                    if (response.isSuccessful) {
                        MyApplication.prefs.setString("update", "update")
                    }
                }

                override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                }
            })
        }

        fun deleteTodo(id: String) { // Todo 삭제
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
            val service: RetrofitService =retrofit.create(RetrofitService::class.java)
            val call: Call<CodeMessageDTO> = service.deleteTodo(
                id,
                MyApplication.prefs.getString("token")
            )

            call.enqueue(object : Callback<CodeMessageDTO> {
                override fun onResponse(
                    call: Call<CodeMessageDTO>,
                    response: Response<CodeMessageDTO>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(binding.root.context, "Todo를 삭제했습니다.", Toast.LENGTH_LONG).show()
                        var layoutParams = todoRelative.layoutParams
                        layoutParams.height = 0
                        todoRelative.layoutParams = layoutParams
                        todoRelative.setPadding(0, 0, 0, 0)
                        todoRelative.visibility = View.GONE
                        MyApplication.prefs.setString("update", "update")
//                        binding.todoProjectLinear.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                    Toast.makeText(binding.root.context, "Todo 삭제를 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            })
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