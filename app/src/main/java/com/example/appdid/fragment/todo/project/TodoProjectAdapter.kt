package com.example.appdid.fragment.todo.project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.R
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.databinding.LayoutTodoProjectBinding
import com.example.appdid.dto.CodeMessageDTO
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

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

        private lateinit var settingButton: ImageButton
        private lateinit var deleteButton: ImageButton
        private lateinit var editTitle: EditText
        private lateinit var textTitle: TextView

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

            editTitle = binding.editProjectTitle
            textTitle = binding.textProjectTitle

            editTitle.setOnEditorActionListener{ textView, action, event ->
                var handled = false

                if (action == EditorInfo.IME_ACTION_DONE) {
                    val inputMethodManager = binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(editTitle.windowToken, 0)
                    handled = true
                }

                handled
            }

            deleteButton = binding.buttonProjectDelete
            deleteButton.setOnClickListener({
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("프로젝트 삭제")
                builder.setMessage(textTitle.text.toString() + " 프로젝트를 삭제합니다.")
                builder.setPositiveButton("예"){ dialog, id ->
                    deleteProject(item._id)
                }
                builder.setNegativeButton("아니오", null)
                builder.setNeutralButton("취소", null)
                builder.create().show()
            })

            settingButton = binding.buttonTodoSetting
            settingButton.setOnClickListener({
                if (textTitle.visibility == View.VISIBLE) {
                    settingButton.setImageResource(R.drawable.ic_baseline_check_24)
                    editTitle.visibility = View.VISIBLE
                    textTitle.visibility = View.GONE
                    deleteButton.visibility = View.VISIBLE
                } else {
                    settingButton.setImageResource(R.drawable.white_setting)
                    val inputMethodManager = binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(editTitle.windowToken, 0)
                    textTitle.visibility = View.VISIBLE
                    if (editTitle.text.length == 0) {
                        editTitle.setText("title")
                    }
                    setProjectTitle(item._id, editTitle.text.toString())
                    textTitle.text = editTitle.text

                    editTitle.visibility = View.GONE
                    deleteButton.visibility = View.GONE
                }
            })

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

        fun setProjectTitle(projectId: String, title: String) { // 프로젝트 이름 변경
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
            val service: RetrofitService =retrofit.create(RetrofitService::class.java)
            val call: Call<CodeMessageDTO> = service.setProjectTitle(
                projectId,
                title,
                MyApplication.prefs.getString("token")
            )

            call.enqueue(object : Callback<CodeMessageDTO> {
                override fun onResponse(
                    call: Call<CodeMessageDTO>,
                    response: Response<CodeMessageDTO>
                ) {
                    if (response.isSuccessful) {
                    }
                }

                override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                    Log.e("Response", "Error")
                }
            })
        }

        fun deleteProject(projectId: String) { // 프로젝트 삭제
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
            val service: RetrofitService =retrofit.create(RetrofitService::class.java)
            val call: Call<CodeMessageDTO> = service.deleteProject(
                projectId,
                MyApplication.prefs.getString("token")
            )

            call.enqueue(object : Callback<CodeMessageDTO> {
                override fun onResponse(
                    call: Call<CodeMessageDTO>,
                    response: Response<CodeMessageDTO>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(binding.root.context, "프로젝트를 삭제했습니다.", Toast.LENGTH_LONG).show()
                        var layoutParams = linearLayout.layoutParams
                        layoutParams.height = 0
                        linearLayout.layoutParams = layoutParams
                        linearLayout.setPadding(0, 0, 0, 0)
                        linearLayout.visibility = View.GONE
                        MyApplication.prefs.setString("update", "update")
//                        binding.todoProjectLinear.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                    Toast.makeText(binding.root.context, "프로젝트 삭제를 실패했습니다.", Toast.LENGTH_LONG).show()
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