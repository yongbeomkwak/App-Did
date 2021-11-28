package com.example.appdid.RetrofitSet

import com.example.appdid.dto.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
        //@종류: GET,POST,PUT,DELETE,HEAD
        //
        @GET("posts/{key}")
        fun getPosts(@Path("key") key: String): Call<TestDto>
        /**
        전체 URI:https://jsonplaceholder.typicode.com/posts/1   .. EndPoint -> posts/1
        @GET("posts/{key}") - 요청메소드 GET, baseUrl에 연결될 EndPoint 'posts/{key}
        fun getPosts Call<PostResult> - Call은 응답이 왔을때 Callback으로 불려질 타입
        TestDto - 요청GET에 대한 응답데이터를 받아서 dto 객체화할 클래스 타입 지정
        메소드명 "getPosts" - 자유롭게 설정, 통신에 영향 x
        매개변수 '@Path("key") String key' - 매개변수 key가 @Path("post")를 보고 @GET 내부 {key}에 대입
         * */
        //매개 변수 (전체 URI에서 URL을 제외한 End Point(URI))


        //https://mp-prj-backend.herokuapp.com/todo/my?id=123412341234"
        /**

         * Query 사용시 역시 마찬가지로 com/까지만 넣어 놓고
         * ?id=1234 .. 부분이 @Query 에노테이션으로 처리가 됨
        */
        @GET("todo/my")
        fun getMyTodoList(@Query("id") id:String) : Call<MyTodoListDTO>


        @GET("profile")
        fun getProfile(@QueryMap querys:Map<String,String>,@Header("Authorization") token:String) : Call<PayloadDTO>

        @POST("group") //group name, userid
        fun postGroup(@QueryMap querys: Map<String, String>, @Header("Authorization") token:String) : Call<CodeMessageDTO>


        @PUT("group/remove/{groupId}")
        fun removeGroup(@Path("groupId") groupId:String, @Query("userId") query: String, @Header("Authorization") token:String) : Call<CodeMessageDTO>


        @GET("group/{groupId}")
        fun getGroup(@Path("groupId") groupId: String,@Header("Authorization") token:String) : Call<GroupPayloadDTO>


        @PUT("group/join/{groupId}")
        fun joinGroup(@Path("groupId") groupId: String,@Query("userId") query:String,@Header("Authorization") token:String):Call<CodeMessageDTO>

        @PUT("profile/{id}")
        fun setProfile(@Path("id") id: String, @Query("avatar") query:String, @Header("Authorization") token:String) : Call<CodeMessageDTO>


        @GET("project")
        fun getProjects(@Query("groupId") id:String,@Header("Authorization") token:String) :Call<ProjectPayloadDTO>

        @POST("todo")
        fun postProject(@QueryMap querys: Map<String, String>, @Header("Authorization") token:String) :Call<CodeMessageDTO>


        @GET("project/todos")
        fun getProjectsAndTodos(@Query("groupId") id:String,@Header("Authorization") token:String) :Call<ProjectsAndTodosPayloadDTO>


        @GET("todo")
        fun getTodos(@QueryMap querys: Map<String, String>, @Header("Authorization") token:String) :Call<TodoPayloadDTO>

        @POST("project")
        fun setProject(@QueryMap querys: Map<String, String>, @Header("Authorization") token:String) :Call<ProjectDTO>

        @PUT("project/{id}")
        fun setProjectTitle(@Path("id") id: String, @Query("projectName") query: String, @Header("Authorization") token:String) :Call<CodeMessageDTO>

        @DELETE("todo/{id}")
        fun deleteTodo(@Path("id") id: String, @Header("Authorization") token:String) :Call<CodeMessageDTO>

        @DELETE("project/{id}")
        fun deleteProject(@Path("id") id: String, @Header("Authorization") token:String) :Call<CodeMessageDTO>

        @PUT("todo/{id}")
        fun checkTodo(@Path("id") id: String, @Query("check") check: Boolean, @Header("Authorization") token:String) :Call<CodeMessageDTO>
}


