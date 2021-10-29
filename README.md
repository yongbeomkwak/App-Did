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