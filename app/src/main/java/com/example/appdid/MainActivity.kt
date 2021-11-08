package com.example.appdid

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.appdid.bottomNavigation.Selected
import com.example.appdid.databinding.ActivityMainBinding
import com.example.appdid.databinding.AppBarMainBinding
import com.example.appdid.databinding.NavigationHeaderBinding
import com.example.appdid.dialog.ProfileDialog
import com.example.appdid.utility.MyApplication
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mikhaellopez.circularimageview.CircularImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.Integer.min
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 일일이 R.id.*를 하지 않기 위한 바인딩
    private lateinit var view_pager2: ViewPager2 // 달력, todo리스트 화면전환을 위한 ViewPager2
    private lateinit var naviHeaderBinding:NavigationHeaderBinding
    private lateinit var appBarBinding:AppBarMainBinding
    private lateinit var bottom_navi_view: BottomNavigationView // 화면전환 컨트롤을 위한 Navigation
    private lateinit var naviProfileImageView:CircularImageView //SideBar 프로필 이미지 뷰
    private lateinit var takePictureResultLauncher: ActivityResultLauncher<Intent> // 콜백 Launch
    private lateinit var loadPictureFromGalleryLauncher: ActivityResultLauncher<Intent> // 콜백 Launch
    private lateinit var dialogProfile:ProfileDialog //프로필 사진 다이얼로그
    private lateinit var curPhotoPath:String
    private val CODE_TAKE_PICTURE:Int=0
    private val CODE_GALLERY_PICTURE:Int=1


    override fun onStart() {
        super.onStart()
        takePictureResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { // 사진 찍기
            if(it.resultCode== RESULT_OK)
            {
                setProfileImage()
            }
        }
        loadPictureFromGalleryLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { //사진 가져오기
            if(it.resultCode== RESULT_OK)
            {
                try
                {
                    val fileUri: Uri? =it.data?.data
                    val instream:InputStream= contentResolver.openInputStream(fileUri!!)!!
                    val bitmap:Bitmap=BitmapFactory.decodeStream(instream)
                    naviProfileImageView.setImageBitmap(bitmap)
                    instream.close()
                    savePhoto(bitmap)
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

        naviHeaderBinding=binding.navHeader // include 바인딩
        appBarBinding= binding.incAppBar // include 바인딩
        //val appbarView:View=findViewById(R.id.incAppBar) as View //include 태그 View를 가져오기 위함
        //val appBar:MaterialToolbar=appbarView.findViewById(R.id.appBar) as MaterialToolbar //include View에서 실제 appBar가져옴
        //val navigationHeaderView:View=findViewById(R.id.nav_header) //사이드 메뉴 appBar
        //val TvUserEmail:TextView =navigationHeaderView.findViewById(R.id.tvUserEmail)
        //val TvUserName:TextView =navigationHeaderView.findViewById(R.id.tvUserName)
        naviProfileImageView=naviHeaderBinding.civProfile




//        setSupportActionBar(appBar) //ActionBar 등록 (안보이는 오류로 인해 주석처리)
        naviHeaderBinding.tvUserEmail.text=MyApplication.prefs.getString("email")
        naviHeaderBinding.tvUserName.text=MyApplication.prefs.getString("name")
        viewPager2Init()
        setExpandableList()

        /*
        initialize
        */

        //register Listener
        appBarBinding.appBar.setNavigationOnClickListener { //햄버거 메뉴 클릭 이벤트
            if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
            {
                binding.dlContainer.closeDrawer(GravityCompat.START)
            }
            else
            {
                binding.dlContainer.openDrawer(GravityCompat.START)
            }
        }

        appBarBinding.appBar.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.app_bar_more -> {
                    println("Hello") //TODO 캘린더 옵션 이벤트
                }
            }
            false
        }

        naviProfileImageView.setOnClickListener(profileClickListener) //SideBar 프로필 이미지 클릭 시 리스너 등록

        setContentView(binding.root)

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

    override fun onBackPressed() { //SideBar 열렸을 때 뒤로가기 버튼 누를 시 닫힘
        if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
        {
            binding.dlContainer.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }

        binding.dlContainer
    }



    fun viewPager2Init() {   // ViewPager2 이니셜라이저
        view_pager2 = binding.viewPager
        view_pager2.adapter =
            com.example.appdid.bottomNavigation.PagerAdapter(supportFragmentManager, lifecycle)

        bottomNaviInit()
        val page_listener = Selected(view_pager2, bottom_navi_view)

        view_pager2.registerOnPageChangeCallback(page_listener.PageChangeCallback())
        view_pager2.setUserInputEnabled(false)
    }

    fun bottomNaviInit() {  // Bottom navigation view 이니셜라이저
        bottom_navi_view = binding.bottomNaviView
        bottom_navi_view.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_calendar -> {
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

    private fun  setExpandableList() //리스트 이니셜라이저
    {
        val parentsList= mutableListOf<String>("부모1", "부모2", "부모3")
        val childList = mutableListOf(
            mutableListOf(), mutableListOf("자식 1", "자식 2"), mutableListOf(
                "자식 1",
                "자식 2",
                "자식 3"
            )
        )
        val expandableListAdapter=com.example.appdid.expandableList.ExpandableListAdapter(
            this,
                supportFragmentManager,
            parentsList,
            childList
        )

        binding.elMenu.setAdapter(expandableListAdapter)

        binding.elMenu.setOnGroupClickListener { parent, v, groupPosition, id ->
            //TODO
            println("Click group")
            false
        }
        binding.elMenu.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //TODO

            println("Click child")
            false
        }
        println("SetEx")

    }


    /**
     * 카메라 권한 설정
     */
    private  fun setPermission(accessCode:Int)
    {
        val permission=object :PermissionListener{
            override fun onPermissionGranted() { //권한을 설정 허가할 경우 수행되는 곳
                //해당 코드에 맞는 작업
                if(accessCode==CODE_GALLERY_PICTURE)
                {
                    loadPhoto() //겔러리 접근
                }
                else
                {
                    takeCapture() //사진 촬영
                }
                dialogProfile.dialog.dismiss()

                Toast.makeText(applicationContext, "권한이 성공적으로 설정됬습니다..", Toast.LENGTH_SHORT).show()

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { //권한은 거부할 경우 수행 되는 곳
                Toast.makeText(applicationContext, "권한을 거부 됬습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission) //리스너 등록
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용 해 주세요") //허용 메시지
            .setDeniedMessage("권한을 거부 하셨습니다 [앱 설정] ->[권한] 항목에서 설정 허용해주세요.") // 거부 메시지
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,

            ) //해당 권한 나열
            .check()
    }

    private fun takeCapture()
    {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent -> //it 대신 takePictureIntent로 접근

            takePictureIntent.resolveActivity(packageManager).also { //resolveActivity:앱의 기기가 해당 packageManager를 호출 가능하면 이동 아닐 경우 null 호출
                //만약 startActivity를 사용하면 기긱가 해당 패키지 매니저를 호출 불가하면 앱이 크러쉬된다


                val photoFile:File? = try {
                    createImageFile() //이미지 파일 생성

                } catch (e: IOException)
                {
                    println("Error 발생")
                    null
                }
                photoFile.also {
                    val photoURI:Uri=FileProvider.getUriForFile(
                        this, //context
                        "com.example.appdid.fileprovider", // "packagename.fileprovider"
                        it!! //file
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) //해당 uri로 이동

                    takePictureResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun createImageFile(): File {
        //val timestamp:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File?=getExternalFilesDir(Environment.DIRECTORY_PICTURES)// 저장 디렉토리 설정
        return File.createTempFile("JPEG_Profie_", ".jpg", storageDir).apply { // 임시파일명:JPEG_날짜.jpg ,저장 경로(storageDir)
            curPhotoPath=absolutePath //경로 저장 하기
        }
    }

    private fun setBitmapSquare(bitmap: Bitmap) : Bitmap{ // Bitmap이미지를 중앙 정렬 정사각형 이미지로 변경하는 함수
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
         *  Resize 구간
         * */
            var squareBitmap = setBitmapSquare(bitmap)
            var options: BitmapFactory.Options = BitmapFactory.Options()
            options.inSampleSize = 2 //1/N배로 크기를 줄여주는데 최솟값은 1이며 2의 거듭제곱을 값으로 주면 속도가 향상된다.
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
                // 원하는 높이보다 클 경우의 설정
                val mHeight = bmpHeight / 100;
                val scale = height / mHeight;
                bmpWidth *= (scale / 100);
                bmpHeight *= (scale / 100);
            }

        val resizedBitmap:Bitmap=Bitmap.createScaledBitmap(squareBitmap,bmpWidth.toInt(),bmpWidth.toInt(),true) //Resize
        naviProfileImageView.setImageBitmap(resizedBitmap) // 이미지 뷰에 설정
        savePhoto(resizedBitmap) //저장
    }
    private fun savePhoto(bitmap: Bitmap)
    {
        val absolutePath = "/storage/emulated/0/"
        val folderPath = "$absolutePath/pictures/"
        val fileName="Profile.jpeg"
        val folder=File(folderPath)
        if(!folder.isDirectory) //현재 해당 경로에 폴더가 없다면
        {
            folder.mkdirs() //폴더를 만든다
        }
        if(File(folderPath+fileName).exists()) //파일 존재 시
        {
            File(folderPath+fileName).delete() //삭제


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
}