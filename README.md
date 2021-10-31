#   APP DID
---
## 10/26
    -   바텀네비게이션 추가(build.gradle(module), activity_main.xml) ,Primary Color 변경,binding 작업
    -   jetPack 설정(build.gradle(project),build.gradle(module)) 

## 10/28
    -   ViewPager2를 이용한 달력, Todo리스트 화면 전환 작업
    -   DrawerLayout을 이용하여 SideBar메뉴 구현 및 확장 리스트 사용(ExpandableListAdapter.kt 생성)
    -   MainActivity 최상위 레이아웃 Relative -> DrawerLayout으로 변경 , 차상위 레이아웃이 ConstraintLayout
    -   menu_child.xml -> 팀원 정보 레이아웃
    -   menu_parent.xml -> 팀 정보 레이아웃

## 10/29
    -   AppBarLayout을 include 태그로 변경 및 app_bar_main에 실제 내용 기입
    -   include로 변경함으로써 MainActivity.kt를 에서 appBar를 binding으로 바로 접근 불가능 , 어쩔 수 없이 findViewById 사용
    -   app_bar 햄버거 메뉴 이벤트 역시 변경
    -   SideBar에 UserProfile header를 만듬 (navigation_header.xml)
    -   com.mikhaellopez.circularimageview.CircularImageView (implement 추가) 및 navigation_header.xml에 사용
    -   profile 구간 아직 완성하지 못함

## 10/30
    -   SideBar Profile CircularImageView ScaleType 지정 (navigation_header.xml)
    -   manifest 외부저장소 접근 권한 명시
    -   SideBar profile 이미지뷰 클릭 이벤트 리스너 등록
    -   위험 퍼미션에 대한 팝업 ted:tedpermission:2.3.3' 등록 (manifest.xml)
    -  퍼미션 체크함수(setPermission)
    - 카메라 or 갤러이에서 데이터를 위한 Launch 생성(MainActivity.kt)
    - 카메라 사용 함수 및 ted 권한 확인 함수
    - 카메라 촬영 및 해당 사진 저장을 위한 작업(manifest(provider) 및 res/xml 에 경로 지정)
    - 촬영함수(takeCapture)
    - 사진 저장함수(SavePhoto)
    - SideBar 모서리 둥근 효과(drawable/navi_border.xml)
    - 프로필 이미지 중앙 정렬된 정사각형으로 변경