package com.example.appdid

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ExpandableListAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.appdid.bottomNavigation.Selected
import com.example.appdid.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mikhaellopez.circularimageview.CircularImageView
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 일일이 R.id.*를 하지 않기 위한 바인딩
    private lateinit var view_pager2: ViewPager2 // 달력, todo리스트 화면전환을 위한 ViewPager2
    private lateinit var bottom_navi_view: BottomNavigationView // 화면전환 컨트롤을 위한 Navigation
    private lateinit var naviProfileImageView:CircularImageView //SideBar 프로필 이미지 뷰
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent,> // 콜백 Launch
    private lateinit var curPhotoPath:String
    val REQUEST_IMAGE_CAPTURE:Int=1

    override fun onStart() {
        super.onStart()
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode== RESULT_OK)
            {
                Toast.makeText(applicationContext, it.data?.getIntExtra("ret",0).toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appbarView:View=findViewById(R.id.incAppBar) as View //include 태그 View를 가져오기 위함
        val appBar:MaterialToolbar=appbarView.findViewById(R.id.appBar) as MaterialToolbar //include View에서 실제 appBar가져옴
        val navigationHeaderView:View=findViewById(R.id.nav_header)
        naviProfileImageView=findViewById<CircularImageView>(R.id.civProfile)


        setSupportActionBar(appBar) //ActionBar 등록
        viewPager2Init()
        setExpandableList()
        /*
        initialize
        */

        //register Listener
        appBar.setNavigationOnClickListener { //햄버거 메뉴 클릭 이벤트
            if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
            {
                binding.dlContainer.closeDrawer(GravityCompat.START)
            }
            else
            {
                binding.dlContainer.openDrawer(GravityCompat.START)
            }
        }
        naviProfileImageView.setOnClickListener(profileClickListener) //SideBar 프로필 이미지 클릭 시 리스너 등록



    }
    val profileClickListener=object:View.OnClickListener
    {
        override fun onClick(v: View?) {
            setPermission()

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
        val parentsList= mutableListOf<String>("부모1","부모2","부모3")
        val childList = mutableListOf( mutableListOf(), mutableListOf("자식 1", "자식 2"), mutableListOf("자식 1", "자식 2", "자식 3") )
        val expandableListAdapter=com.example.appdid.expandableList.ExpandableListAdapter(this,parentsList,childList)
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

    }


    /**
     * 카메라 권한 설정
     */
    private  fun setPermission()
    {
        val permission=object :PermissionListener{
            override fun onPermissionGranted() { //권한을 설정 허가할 경우 수행되는 곳
                Toast.makeText(applicationContext,"권한이 성공적으로 설정됬습니다..",Toast.LENGTH_SHORT).show()
                takeCapture()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { //권한은 거부할 경우 수행 되는 곳
                Toast.makeText(applicationContext,"권한을 거부 됬습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission) //리스너 등록
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용 해 주세요") //허용 메시지
            .setDeniedMessage("권한을 거부 하셨습니다 [앱 설정] ->[권한] 항목에서 설정 허용해주세요.") // 거부 메시지
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA) //해당 권한 나열
            .check()
    }

    private fun takeCapture()
    {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent -> //it 대신 takePictureIntent로 접근

            takePictureIntent.resolveActivity(packageManager).also { //resolveActivity:앱의 기기가 해당 packageManager를 호출 가능하면 이동 아닐 경우 null 호출
                //만약 startActivity를 사용하면 기긱가 해당 패키지 매니저를 호출 불가하면 앱이 크러쉬된다


                val photoFile:File? = try {
                    createImageFile() //이미지 파일 생성

                } catch (e:IOException)
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
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI) //해당 uri로 이동

                    activityResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timestamp:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File?=getExternalFilesDir(Environment.DIRECTORY_PICTURES)// 저장 디렉토리 설정
        return File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir).apply { // 임시파일명:JPEG_날짜.jpg ,저장 경로(storageDir)
            curPhotoPath=absolutePath
        }
    }

}