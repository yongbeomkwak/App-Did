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
    - 프로필 이미지 중앙 정렬된 정사각형으로 변
    - 프로필 이미지 외부저장소 저장 화질 개선

## 10/31
    -   LoadPicture(갤러리 접근 함수)
    -   OptionMenu 선택 이벤트 구간 만들기 구현은 나중
    -   팝업 다이얼로그 생성 및 해당 리스너별 동작 배정

## 11/3
-   [구글로그인 및 Firebase 적용](https://ghs-dev.tistory.com/14)
- SharedPreference 세팅
-   [Retrofit](https://jaejong.tistory.com/33)
-   [레트로핏 코틀린](https://kumgo1d.tistory.com/57)
    -  DTO , Interface(Service) , Builder 정의 
    
## 11/5
-   [달력 제작 참고 블로그](https://shwoghk14.blogspot.com/2020/10/android-custom-calendar-with.html)
-   res/color에 달력에 사용할 색상 추가
-   달력 일 칸 구분 줄 (drawable/day_border.xml)
-   달력 등록 (fragment_calendar.xml)
-   달력 달 이동 어뎁터 추가 (CalendarMonthPagerAdapter.kt)
-   달력 달 프레그먼트 추가 (CalendarMonthFragment.kt)
-   달력 프레그먼트에 달력 추가 (CalendarFragment.kt)
-   달력 일 어뎁터 추가 (CalendarDayAdapter.kt)
-   달력 데이터 바인딩 클래스 추가 (CalendarInfo.kt)

## 11/8
-   BottomSheetDialog  설정
-   잔체적인 리바인딩 작업
-   [animation(속성)](https://seosh817.tistory.com/18)
-   [애니메이션 순서](https://greedy0110.tistory.com/52)
-   interpolator:보간
-   overridePendingTransition(R.anim.none, R.anim.horizon_exit) :param1:새로 나타는 화면에,param2:현재 화면에

## 11/9
-   달력 위쪽 글자 클릭시 원하는 달로 한번에 이동할 수 있는 DatePicker 구현 (CalendarFragment.kt)
-   달력 날짜 높이 조절을 위한 데이터바인딩 함수 (CalendarDayAdapter.kt) [dewinjm/monthyear-picker](https://github.com/dewinjm/monthyear-picker)
-   달력 크기를 화면 크기에 맞춤 (CalendarMonthFragment.kt)


## 11/12
-   TeamSettingActivity 규격 잡기
-   레이아웃 초본 완성
-   RecyclerView 각 아이템 별로 데코레이션(높이) 설정
-   TeamMemberDTO 생성 및 서버로 부터 받아올 준비
-   초대코드 복사 작업완료



