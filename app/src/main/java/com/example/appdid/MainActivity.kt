package com.example.appdid

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.appdid.dto.*
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.bottomNavigation.Selected
import com.example.appdid.databinding.ActivityMainBinding
import com.example.appdid.databinding.AppBarMainBinding
import com.example.appdid.databinding.NavigationHeaderBinding
import com.example.appdid.dialog.ProfileDialog
import com.example.appdid.dialog.TeamCreateDialog
import com.example.appdid.dialog.TeamParticiapteDialog
import com.example.appdid.fragment.todo.AddTodoActivity
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mikhaellopez.circularimageview.CircularImageView
import java.io.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.Integer.min

class MainActivity : AppCompatActivity() {

    private val REQUEST_ADD_TODO = 1

    private lateinit var binding: ActivityMainBinding // ????????? R.id.*??? ?????? ?????? ?????? ?????????
    private lateinit var view_pager2: ViewPager2 // ??????, todo????????? ??????????????? ?????? ViewPager2
    private lateinit var naviHeaderBinding:NavigationHeaderBinding
    private lateinit var appBarBinding:AppBarMainBinding
    private lateinit var bottom_navi_view: BottomNavigationView // ???????????? ???????????? ?????? Navigation
    private lateinit var naviProfileImageView:CircularImageView //SideBar ????????? ????????? ???
    private lateinit var takePictureResultLauncher: ActivityResultLauncher<Intent> // ?????? Launch
    private lateinit var loadPictureFromGalleryLauncher: ActivityResultLauncher<Intent> // ?????? Launch
    private lateinit var dialogProfile:ProfileDialog //????????? ?????? ???????????????
    private lateinit var curPhotoPath:String
    private val CODE_TAKE_PICTURE:Int=0
    private val CODE_GALLERY_PICTURE:Int=1
    private lateinit var dialogParticipateTeam:TeamParticiapteDialog
    private lateinit var dialogAddTeam:TeamCreateDialog
    private lateinit var groupId:String
    private lateinit var retrofit:Retrofit
    private lateinit var service: RetrofitService
    private lateinit var bottom_adapter: com.example.appdid.bottomNavigation.PagerAdapter


    override fun onStart() {
        super.onStart()

        takePictureResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { // ?????? ??????
            if(it.resultCode== RESULT_OK)
            {
                setProfileImage()
            }
        }
        loadPictureFromGalleryLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { //?????? ????????????
            if(it.resultCode== RESULT_OK)
            {
                try
                {
                    val fileUri: Uri? =it.data?.data
                    val instream:InputStream= contentResolver.openInputStream(fileUri!!)!!
                    val bitmap:Bitmap=BitmapFactory.decodeStream(instream)
                    naviProfileImageView.setImageBitmap(bitmap)
                    instream.close()
//                    savePhoto(bitmap)
                    saveProfileImageFirebase(bitmap)
                }
                catch (e:Exception)
                {
                    println("Load Error")
                }


            }
        }

        dialogProfile= ProfileDialog(this)



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        retrofit=RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
        service=retrofit.create(RetrofitService::class.java)//???????????????
        naviHeaderBinding=binding.navHeader // include ?????????
        appBarBinding= binding.incAppBar // include ?????????
        //val appbarView:View=findViewById(R.id.incAppBar) as View //include ?????? View??? ???????????? ??????
        //val appBar:MaterialToolbar=appbarView.findViewById(R.id.appBar) as MaterialToolbar //include View?????? ?????? appBar?????????
        //val navigationHeaderView:View=findViewById(R.id.nav_header) //????????? ?????? appBar
        //val TvUserEmail:TextView =navigationHeaderView.findViewById(R.id.tvUserEmail)
        //val TvUserName:TextView =navigationHeaderView.findViewById(R.id.tvUserName)
        naviProfileImageView=naviHeaderBinding.civProfile




//        setSupportActionBar(appBar) //ActionBar ?????? (???????????? ????????? ?????? ????????????)
        naviHeaderBinding.tvUserEmail.text=MyApplication.prefs.getString("email")
        naviHeaderBinding.tvUserName.text=MyApplication.prefs.getString("name")
        viewPager2Init()
        loadProfilePhoto()


        /*
        initialize
        */

        //register Listener
        appBarBinding.appBar.setNavigationOnClickListener { //????????? ?????? ?????? ?????????
            if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
            {
                binding.dlContainer.closeDrawer(GravityCompat.START)
            }
            else
            {
                reFreshTeamList(false)
                binding.dlContainer.openDrawer(GravityCompat.START)
            }
        }

        appBarBinding.appBar.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.menu_AddTodo -> {
                    val intent:Intent=Intent(applicationContext,AddTodoActivity::class.java)
                    intent.putExtra("groupId",groupId)
                    startActivityForResult(intent, REQUEST_ADD_TODO)
                }
            }
            false
        }

        naviProfileImageView.setOnClickListener(profileClickListener) //SideBar ????????? ????????? ?????? ??? ????????? ??????
        dialogAddTeam= TeamCreateDialog(this)
        dialogParticipateTeam= TeamParticiapteDialog(this)
        binding.llTeamParticipaate.setOnClickListener(teamParticipateListener)
        binding.llTeamAdd.setOnClickListener(teamAddListener)
        reFreshTeamList(true)

        setContentView(binding.root)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_TODO && resultCode == RESULT_OK) {
            loadNewAdapter()
        }
    }

    val profileClickListener=object:View.OnClickListener
    {
        override fun onClick(v: View?) {
            dialogProfile.callDialog()

            val llTakePicture:LinearLayout=dialogProfile.dialog.findViewById<LinearLayout>(R.id.llTakePicture)
            val llGalleryPicture:LinearLayout=dialogProfile.dialog.findViewById(R.id.llGallery)
            llTakePicture.setOnClickListener {
               setPermission(CODE_TAKE_PICTURE)
            }

            llGalleryPicture.setOnClickListener {
                setPermission(CODE_GALLERY_PICTURE)
            }
        }
    }

    val teamParticipateListener=object :View.OnClickListener
    {
        override fun onClick(v: View?) {
            if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
            {
                binding.dlContainer.closeDrawer(GravityCompat.START)
            }

            dialogParticipateTeam.setDialog()

        }
    }

    val teamAddListener=object :View.OnClickListener {
        override fun onClick(v: View?) {
            if (binding.dlContainer.isDrawerOpen(GravityCompat.START)) {
                binding.dlContainer.closeDrawer(GravityCompat.START)
            }
            dialogAddTeam.setDialog()


        }
    }

    override fun onBackPressed() { //SideBar ????????? ??? ???????????? ?????? ?????? ??? ??????
        if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
        {
            binding.dlContainer.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }


    }





    fun viewPager2Init() {   // ViewPager2 ??????????????????
        view_pager2 = binding.viewPager
        bottom_adapter = com.example.appdid.bottomNavigation.PagerAdapter(supportFragmentManager, lifecycle)
        view_pager2.adapter = bottom_adapter

        bottomNaviInit()
        val page_listener = Selected(view_pager2, bottom_navi_view)
        view_pager2.setUserInputEnabled(false)
    }

    fun bottomNaviInit() {  // Bottom navigation view ??????????????????
        bottom_navi_view = binding.bottomNaviView
        bottom_navi_view.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_calendar -> {
                        if (MyApplication.prefs.getString("update").equals("update")) {
                            MyApplication.prefs.setString("update", "done")
                            loadNewAdapter()
                        }
                        view_pager2.currentItem = 0

                    }
                    R.id.item_todo -> {
                        view_pager2.currentItem = 1
                    }
                }
                true
            }
        }
    }


    /**
     * ????????? ?????? ??????
     */
    private  fun setPermission(accessCode:Int)
    {
        val permission=object :PermissionListener{
            override fun onPermissionGranted() { //????????? ?????? ????????? ?????? ???????????? ???
                //?????? ????????? ?????? ??????
                if(accessCode==CODE_GALLERY_PICTURE)
                {
                    loadPhoto() //????????? ??????
                }
                else
                {
                    takeCapture() //?????? ??????
                }
                dialogProfile.dialog.dismiss()

                Toast.makeText(applicationContext, "????????? ??????????????? ??????????????????..", Toast.LENGTH_SHORT).show()

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { //????????? ????????? ?????? ?????? ?????? ???
                Toast.makeText(applicationContext, "????????? ?????? ????????????.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission) //????????? ??????
            .setRationaleMessage("????????? ?????? ?????????????????? ????????? ?????? ??? ?????????") //?????? ?????????
            .setDeniedMessage("????????? ?????? ??????????????? [??? ??????] ->[??????] ???????????? ?????? ??????????????????.") // ?????? ?????????
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,

            ) //?????? ?????? ??????
            .check()
    }

    private fun takeCapture()
    {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent -> //it ?????? takePictureIntent??? ??????

            takePictureIntent.resolveActivity(packageManager).also { //resolveActivity:?????? ????????? ?????? packageManager??? ?????? ???????????? ?????? ?????? ?????? null ??????
                //?????? startActivity??? ???????????? ????????? ?????? ????????? ???????????? ?????? ???????????? ?????? ???????????????


                val photoFile:File? = try {
                    createImageFile() //????????? ?????? ??????

                } catch (e: IOException)
                {
                    println("Error ??????")
                    null
                }
                photoFile.also {
                    val photoURI:Uri=FileProvider.getUriForFile(
                        this, //context
                        "com.example.appdid.fileprovider", // "packagename.fileprovider"
                        it!! //file
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) //?????? uri??? ??????

                    takePictureResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun createImageFile(): File {
        //val timestamp:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File?=getExternalFilesDir(Environment.DIRECTORY_PICTURES)// ?????? ???????????? ??????
        return File.createTempFile("JPEG_Profie_", ".jpg", storageDir).apply { // ???????????????:JPEG_??????.jpg ,?????? ??????(storageDir)
            curPhotoPath=absolutePath //?????? ?????? ??????
        }
    }

    private fun setBitmapSquare(bitmap: Bitmap) : Bitmap{ // Bitmap???????????? ?????? ?????? ???????????? ???????????? ???????????? ??????
        var w = bitmap.width
        var h = bitmap.height

        var x = w / 2
        var y = h / 2

        w = min(w, h)
        h = min(w, h)

        x -= w / 2
        y -= h / 2

        return Bitmap.createBitmap(bitmap, x, y, w, h)
    }

    private fun setProfileImage(){
        val bitmap:Bitmap
        val file=File(curPhotoPath)

        if(Build.VERSION.SDK_INT<28)
        {
            bitmap=MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
        }
        else {
            val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
            )
            bitmap = ImageDecoder.decodeBitmap(decode)
        }
        /**
         *  Resize ??????
         * */
            var squareBitmap = setBitmapSquare(bitmap)
            var options: BitmapFactory.Options = BitmapFactory.Options()
            options.inSampleSize = 2 //1/N?????? ????????? ??????????????? ???????????? 1?????? 2??? ??????????????? ????????? ?????? ????????? ????????????.
            val width: Int = 170
            val height: Int = 170
            var bmpWidth: Float = squareBitmap.width.toFloat()
            var bmpHeight: Float = squareBitmap.height.toFloat()

            if (bmpWidth > width) {
                val mWidth = (bmpWidth / 100)
                val scale = width / mWidth
                bmpWidth *= scale / 100
                bmpHeight *= scale / 100
            } else if (bmpHeight > height) {
                // ????????? ???????????? ??? ????????? ??????
                val mHeight = bmpHeight / 100;
                val scale = height / mHeight;
                bmpWidth *= (scale / 100);
                bmpHeight *= (scale / 100);
            }

        val resizedBitmap:Bitmap=Bitmap.createScaledBitmap(squareBitmap,bmpWidth.toInt(),bmpWidth.toInt(),true) //Resize
        naviProfileImageView.setImageBitmap(resizedBitmap) // ????????? ?????? ??????
//        savePhoto(resizedBitmap) //??????
        saveProfileImageFirebase(resizedBitmap) // ??????
    }
    private fun savePhoto(bitmap: Bitmap)
    {
        val absolutePath = "/storage/emulated/0/"
        val folderPath = "$absolutePath/Pictures/"
        val fileName="Profile.jpeg"
        val folder=File(folderPath)
        if(!folder.isDirectory) //?????? ?????? ????????? ????????? ?????????
        {
            folder.mkdirs() //????????? ?????????
        }
        if(File(folderPath+fileName).exists()) //?????? ?????? ???
        {
            File(folderPath+fileName).delete() //??????

        }

        val out =FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }

    private fun loadPhoto()
    {
        Intent(MediaStore.Images.Media.CONTENT_TYPE).also {
            it.setType("image/*");
            it.setAction(Intent.ACTION_GET_CONTENT);
            loadPictureFromGalleryLauncher.launch(it)
        }


    }

    fun saveProfileImageFirebase(bitmap: Bitmap) { // ????????? ????????? ????????????????????? ??????

        val imageUri = bitmapToUri("profile", bitmap)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val riversRef = storageRef.child("ProfileImages/" + imageUri.lastPathSegment)
        val uploadTask = riversRef.putFile(imageUri)

        val urlTask = uploadTask.continueWithTask { task ->
            if(!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            riversRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                val retrofit:Retrofit=RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
                val service: RetrofitService =retrofit.create(RetrofitService::class.java)//???????????????
                val call:Call<CodeMessageDTO> = service.setProfile(
                        MyApplication.prefs.getString("id", ""),
                        downloadUri,
                        MyApplication.prefs.getString("token")
                )

                call.enqueue(object : Callback<CodeMessageDTO> {
                    override fun onResponse(
                            call: Call<CodeMessageDTO>,
                            response: Response<CodeMessageDTO>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                        Log.e("Response", "Error")
                    }
                })
            }
            else{
                Log.e("Value", task.exception.toString())
            }
        }

        contentResolver.delete(imageUri, null, null)
    }

    fun bitmapToUri(filename: String, bitmap: Bitmap) : Uri { // Bitmap??? Uri??? ??????
        var bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(applicationContext.contentResolver, bitmap, filename, null)
        return Uri.parse(path)
    }

    override fun finish() {

        super.finish()
        overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_left_exit) //finish()?????? ??? ??????????????? ??????,?????? ??????????????? ??????
    }

    fun reFreshTeamList(setGroup: Boolean)
    {

        val call: Call<PayloadDTO> =service.getProfile(mapOf(
                "id" to MyApplication.prefs.getString("id",""),
        ),MyApplication.prefs.getString("token"))

        call.enqueue(object : Callback<PayloadDTO> {
            override fun onResponse(call: Call<PayloadDTO>, response: Response<PayloadDTO>) {
                Log.e("RESS",response.toString())
                if(response.isSuccessful)
                {
                    val payload: PayloadDTO =response.body()!!
                    val userInfo: UserInfoDTO = payload.payloads[0]
                    Log.d("Response",userInfo.toString())
                    MyApplication.TeamInfo=payload.payloads[0].userGroupDTOS
                    if(setGroup) {
                        groupId = payload.payloads[0].userGroupDTOS[0]._id //?????? ???????????? ??????
                        MyApplication.prefs.setString("groupId", groupId)
                        binding.incAppBar.appBar.title =
                            payload.payloads[0].userGroupDTOS[0].groupName
                    }

                }
            }

            override fun onFailure(call: Call<PayloadDTO>, t: Throwable) {
                Log.e("Response","Error")
            }
        })

        val parentsList= MyApplication.TeamInfo
        //TODO parentsList?????? 2 ?????? ????????? ??????
        val childList:MutableList<MutableList<String>> = MutableList(parentsList.size+2,{index -> mutableListOf()})

        val expandableListAdapter=com.example.appdid.adapter.ExpandableListAdapter(
                this,
                supportFragmentManager,
                parentsList,
                childList
        )
        binding.elMenu.setAdapter(expandableListAdapter)

        binding.elMenu.setOnGroupClickListener { parent, v, groupPosition, id ->
            groupId=expandableListAdapter.getGroup(groupPosition)._id
            MyApplication.prefs.setString("groupId", groupId)
            Log.e("WOW", groupId.toString() + " lsdfahjasdfslgj")
            binding.incAppBar.appBar.title = expandableListAdapter.getGroup(groupPosition).groupName
            closeDrawer()


            //TODO ?????? ?????????
            loadNewAdapter()
            false
        }
        binding.elMenu.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //TODO
            println("Click child")
            false
        }

    }

    fun loadNewAdapter() {
        bottom_adapter = com.example.appdid.bottomNavigation.PagerAdapter(supportFragmentManager, lifecycle)
        view_pager2.adapter = bottom_adapter
        bottom_navi_view.selectedItemId = R.id.item_calendar
    }
    fun closeDrawer()
    {
        if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
        {
            binding.dlContainer.closeDrawer(GravityCompat.START)
        }
    }

    fun loadProfilePhoto()
    {
        Log.e("Profile",MyApplication.prefs.getString("profilePhoto"))
        if(MyApplication.prefs.getString("profilePhoto")!="null")
        {
            Glide.with(this@MainActivity, ).load(Uri.parse(MyApplication.prefs.getString("profilePhoto"))).into(binding.navHeader.civProfile)
        }



    }
}