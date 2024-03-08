# StockSwiftService
<br>

## 1. 프로젝트 소개
<img width="884" alt="스크린샷 2024-03-08 오전 9 24 41" src="https://github.com/StockSwiftService/sss_project/assets/144636087/069dd588-0f87-4d2a-9d72-aabea23ed265">
<br> : "STOCK SWIFT SERVICE"는 현대 비즈니스 환경에서 필수적인 요소인 유통 관련 재고 관리를 손쉽게, 빠르게, 그리고 효율적으로 해결해주는<br>
웹 사이트 프로젝트입니다. 기업 및 조직들이 복잡한 유통 네트워크에서 발생하는 재고 관리 문제에 대해 빠르게 대응할 수 있도록 돕는 것입니다.
<br><br><br>

### - [회원 관리] : 회사 대표자가 사원들을 등록 및 관리
<img width="1440" alt="스크린샷 2024-03-08 오전 9 38 57" src="https://github.com/StockSwiftService/sss_project/assets/144636087/329724d4-371e-4069-9486-73b2facf678a">
<br><br><br>

### - [거래처 관리] : 거래처를 등록 및 관리
![KakaoTalk_Photo_2024-03-08-09-44-50](https://github.com/StockSwiftService/sss_project/assets/144636087/ed8fd5ab-6752-4f52-903a-7e3837a7ee98)
<br><br><br>

### - [재고 관리] : 재고 등록 및 품목(엑셀) 다운로드
![image](https://github.com/StockSwiftService/sss_project/assets/144636087/8260ab85-c0d7-4e86-8ba0-2cbd9d676222)
<br><br><br>

### - [판매 관리] : 판매 재고의 승인 및 관리
![image](https://github.com/StockSwiftService/sss_project/assets/144636087/51f6729a-0e19-4afb-ac27-79fa493375f3)
![image](https://github.com/StockSwiftService/sss_project/assets/144636087/74a8b4f3-2924-437a-b957-21edff39ee06)
<br><br><br>

### - [매출 관리] : 회사의 매출 관리
![image](https://github.com/StockSwiftService/sss_project/assets/144636087/4f364eee-a50d-4881-8ad1-7412076b8259)
![image](https://github.com/StockSwiftService/sss_project/assets/144636087/84122591-5b3f-4e48-ac46-c5d2171403a2)
<br><br><br>

### - [일정 관리] : 회사 일정 관리
![KakaoTalk_Photo_2024-03-08-09-48-19](https://github.com/StockSwiftService/sss_project/assets/144636087/609287c3-95e8-402a-bd85-a3a9262d44af)
<br><br><br>

### - [자주 묻는 질문] 
![image](https://github.com/StockSwiftService/sss_project/assets/144636087/bb9e19fb-106a-4a67-9e68-e25bd0943424)
<br><br><br>


## 🕰️ 프로젝트 기간
2024-02-13 ~ 2024-03-08
<br>
<br>

## 2. 프로젝트 구조
```
   frontapp
    ├── src
    │   ├── components
    │   │   ├── Header.svelte
    │   │   ├── LeftMenu.svelte
    │   │   ├── LeftMenuAdmin.svelte
    │   │   └── LoginUser.svelte
    │   ├── lib
    │   │   └── index.js
    │   └── routes
    │   │   └──admin
    │   │        ├── login
    │   │        └── using
    │   │   ├── excel
    │   │   ├── information_search
    │   │   ├── join
    │   │   ├── using
    │   │        ├── account_manage
    │   │        ├── calender
    │   │        ├── inventory_manage
    │   │        ├── qua
    │   │        ├── sale_manage
    │   │        ├── sell_manage
    │   │        ├── test
    │   │        └── user_manage
    └── static
          └── img
   src
    ├── main
    │   ├── generated
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── stockswiftservice
    │   │               ├── domain
    │   │               │   ├── client
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   ├── company
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   ├── member
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   ├── purchase
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   ├── question
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   ├── salemanagement
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   ├── schedule
    │   │               │   │   ├── controller
    │   │               │   │   ├── entity
    │   │               │   │   ├── repository
    │   │               │   │   └── service
    │   │               │   └─── stock
    │   │               │       ├── controller
    │   │               │       ├── entity
    │   │               │       ├── repository
    │   │               │       └── service
    │   │               └─────── global
    │   │                    ├── baseentity
    │   │                    ├── config
    │   │                    ├── filter
    │   │                    ├── initdata
    │   │                    ├── jwt
    │   │                    ├── mvcconfig
    │   │                    ├── rq
    │   │                    ├── rs
    │   │                    ├── tokenverify
    │   │                    └── utill
    │   └── resources
    │       ├── static
    │       │   └── img
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── stockswiftservice

```
<br>

## 3. 개발환경
<div align="center">
<p style="font-size:20px;">💻 Tech Stack 💻</p>
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white"/>
<img src="https://img.shields.io/badge/Tailwindcss-06B6D4?style=flat&logo=Tailwindcss&logoColor=white"/>
<img src="https://img.shields.io/badge/JavaScript-f7df1e?style=flat&logo=JavaScript&logoColor=white"/>
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"/>
<img src="https://img.shields.io/badge/JSON-000000?style=flat-square&logo=json&logoColor=white"/>
<img src="https://img.shields.io/badge/Svelte-FF3E00?style=flat-square&logo=Svelte&logoColor=white"/>
<br/>
<img src="https://img.shields.io/badge/mariadb-003545?style=flat&logo=mariadb&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479a1?style=flat&logo=mysql&logoColor=white"/> 
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=flat&logo=Springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>
<br/>
<br/>
  
<p style="font-size:20px;">🛠 Tools 🛠</p>
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&181717=white"/>
<img src="https://img.shields.io/badge/intellijidea-000000?style=flat&logo=intellijidea&logoColor=white">
<img src="https://img.shields.io/badge/dbeaver-382923?style=flat&logo=dbeaver&logoColor=white">
</div>
<br>

## 4. ERD 설계
![KakaoTalk_Photo_2024-03-07-16-17-40](https://github.com/StockSwiftService/sss_project/assets/144636087/bb90ae1e-f0ac-468d-962e-f2a53524bb40)
<br>



## 6. 트러블 슈팅
<details>
   <summary>💥 최경현 </summary>
<br/>
   
1. 문제 : 캘린더에서 주간 매출과 월간 매출을 전역변수로 사용해서 불러오는데 다음 달로 넘어가거나 새로고침을
할 때 데이터가 초기화 되지 않고, 누적되거나 없어지는 등 기존 데이터가 유지되지 않는 문제 발생

<img width="488" alt="스크린샷 2024-03-08 오전 9 29 58" src="https://github.com/StockSwiftService/sss_project/assets/144636087/52b3f773-e29c-4c95-b08f-81a8b6bcd7dd">
<br>2. 해결 : 월 매출 = 해당 연도 및 월을 key값으로 이용해 value값을 월 매출값으로 설정 후, 전역변수에 저장 / 주간 매출 = 해당 연도 및 월 그리고 몇 주차인지 비교해 key값을 이용해서 value 값을 주간 매출 값으로 설정 후, 전역변수에 저장
</details>
<details>
   <summary>💥 김경호 </summary>
<br/>
   
1. 문제 : 판매 재고를 여러 개 등록 후 판매 전표에 저장을 시키면 재고 객체만 저장되고 판매되는 수량이 저장되지 않음
   
<img width="473" alt="스크린샷 2024-03-08 오전 9 31 21" src="https://github.com/StockSwiftService/sss_project/assets/144636087/f0cf2a32-5f57-4a68-afb1-f29caf1b4c69">
<br>2. 해결 : List 형식으로 재고 이름이랑 판매 수량을 담은 배열을 추가하는 entity를 새로 생성하여 종속 관계 설정
</details>
<details>
   <summary>💥 고광완 </summary>
<br/>
   
1. 문제 : 품목 중복확인하는 과정에서 두 개의 필드를 검증 과정에서 UnsatisfiedDependencyException이 발생
오류의 근본적인 원인은 StockRepository의 findByItemName 메서드에 대한 쿼리 생성 실패

<img width="470" alt="스크린샷 2024-03-08 오전 9 32 37" src="https://github.com/StockSwiftService/sss_project/assets/144636087/51625cfc-1110-4983-9779-d584910a3e71">
<br>2. 해결 : @Query어노테이션을 사용하여 쿼리를 다음과 같이 명시적으로 정의
</details>
<details>
   <summary>💥 서정헌 </summary>
<br/>
   
1. 문제 : 풀캘린더에서 등록된 일정을 누가 작성했는지 구분하기 위해서는 일정의 아이디 값이 필요한데 아이디 값이 담기지 않는 오류가 발생

<img width="474" alt="스크린샷 2024-03-08 오전 9 33 34" src="https://github.com/StockSwiftService/sss_project/assets/144636087/97fcb19e-30e3-4e66-9ca8-444d0d995ebe">
<br>2. 해결 : 새로고침 없이 이벤트의 아이디 값을 받기 위해서 일정 등록 후 해당 이벤트에 대한 데이터를 업데이트 해줘야 함
-> 즉, 캘린더를 다시 render() 해줘야 하는데 등록 후 바로 render()를 진행하게 되면 캘린더가 새로고침되어 불편함이 발생해 삭제나 수정 버튼을 누를 때 render()를 시켜 데이터를 업데이트 될 수 있도록 함
</details>
<details>
   <summary>💥 백현우 </summary>
<br/>
   
1. 문제 : 토큰을 쿠키에 저장시키면 다른 페이지로 넘어갔을 때 도메인이 달라서 보안 이슈로 브라우저에서 쿠키를 강제로 삭제 시켜 저장된 쿠키가 사라지는 문제가 있었음
   
<img width="480" alt="스크린샷 2024-03-08 오전 9 34 26" src="https://github.com/StockSwiftService/sss_project/assets/144636087/a74af9dc-96e0-4fc3-a5f1-115910c4e852">
<img width="403" alt="스크린샷 2024-03-08 오전 9 34 40" src="https://github.com/StockSwiftService/sss_project/assets/144636087/bdcaec83-3340-4eb5-90ea-dee7f1c0bdb0">
<br>2. 해결 : 수정후 처럼 sameSite("None") 을 설정해주어서 다른 도메인 이어도 쿠키가 삭제되지 않게 설정해주었음
</details>
<details>
   <summary>💥 김조은 </summary>
<br/>
   
1. 문제 : 회원관리 목록에서 특정 사원의 수정 버튼을 클릭 했을 때, 해당 사원의 정보인 'member' 객체의 데이터를 HTML에서 어떻게 JavaScript에 전달할지에 대한 고민을 함
   
<img width="461" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-02-25%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%202 58 05" src="https://github.com/StockSwiftService/sss_project/assets/144636087/217bb841-2f55-4571-8092-2ce783a8b863">
<br>2. 해결 : 1.{#each memberList as member}을 통해 회원 목록을 반복하며 각 회원의 정보를 JSON.stringify(member)를 사용하여 JSON 형식의 문자열로 변환하여 출력
2.수정 버튼을 클릭하면 해당 회원의 ID를 추출하고, 이를 사용하여 회원 정보를 가져와 JavaScript 객체로 변환하고, 변환된 객체를 이용하여 수정이 가능한 modifyData 객체에 각 회원의 정보를 설정해줌

</details>




