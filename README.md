# WakeMeUp #
-----------------
## 프로젝트 소개 ##
기존의 알람앱 기능에, 패턴풀기 횟수 초과로 실패시 지정한 도우미에게 문자를 전송하는 알람 어플리케이션입니다. 
다양한 미션 수행 알람 어플리케이션이 있지만 도전 횟수가 무제한 이라는점. 얼떨결에 문제를 해결하고 다시 잠들 수 있다는 문제점을 생각하다가, 미션에 횟수를 지정하고, 지정한 횟수안에 미션을 해결하지 못하면,지정한 도우미에게 문자를 보내어 기상을 돕게하는 어플이 있으면 재미있을 것 같다고 생각했습니다.  
그래서 WakeMeUp을 만들게 되었습니다. 일반적 기상 어플처럼 기상도우미 호출없이 서비스를 사용할 수도있고, 휴대폰 내 contact의 번호들을 자동으로 불러와 기상도우미를 지정가능하게 합니다.
* 사용한 언어/툴 
  * front-end : kotlin/android studio
  * back-end : js, node-js/visual studio code, aws
* 사용 라이브러리 
  * 패턴풀기 : https://github.com/l7naive/pattern-lock.git
  * 클라이언트-서버 통신 : Retrofit2
* 기능 
  * 회원가입, 로그인, 알람 추가, 삭제, 편집, 패턴풀기
* 개발 기간 
  * 2022.12.23 ~ 2023.1.8
-----------------
### 회원가입, 로그인 ###
<p align="center"><img width="269" alt="스크린샷 2023-01-08 오후 5 35 46" src="https://user-images.githubusercontent.com/77314069/211187417-7ed923d7-0f66-40f3-bd57-7b3e23299cd4.png"> <img width="270" alt="스크린샷 2023-01-08 오후 5 33 22" src="https://user-images.githubusercontent.com/77314069/211187333-33992786-3c98-4453-b29e-25decd227f09.png"></p><br> 
- DB접근, 대조<br>   
- sharedpreferences로 아이디값 저장

### 알람 리스트 ###
<p align="center"><img width="259" alt="스크린샷 2023-01-08 오후 5 45 11" src="https://user-images.githubusercontent.com/77314069/211187773-42c61902-5bf6-4f44-8602-533b4618c05b.png"></p><br>  
- 로그인을 하면, 자동으로 해당 사용자의 리스트를 가져옵니다. <br>
- DB접근, 업데이트<br>  
- alarmListAdapter에서 notifcation 등록<br><br>  

### 알람 추가 ###
<p align="center"><img width="266" alt="스크린샷 2023-01-08 오후 5 41 10" src="https://user-images.githubusercontent.com/77314069/211187614-f1ede7b0-599e-4b5c-98ac-de281838765f.png"><img width="278" alt="스크린샷 2023-01-08 오후 6 08 54" src="https://user-images.githubusercontent.com/77314069/211188492-dd110e62-d90a-4a26-9c1c-760c46940ed5.png">
<img width="276" alt="스크린샷 2023-01-08 오후 6 09 39" src="https://user-images.githubusercontent.com/77314069/211188472-fbff73a8-4d0d-4b07-bf27-6d9e5b7facca.png">
</p> 
- DB접근, 추가<br> 
- 안드로이드 내장 벨소리(RingTone) 사용<br> 
- 기상도우미 설정 시 안드로이드 저장된 contacts read 가능<br> 
- alarmListAdapter에서 notifcation 등록<br><br> 

### 알람 삭제 ### 
<p align="center"><img width="259" alt="스크린샷 2023-01-08 오후 5 45 31" src="https://user-images.githubusercontent.com/77314069/211187787-31f321ab-c202-4ba3-96e2-89a9bf8ee1aa.png"></p> 
- DB접근, 삭제<br>  
- alarmListAdapter에서 notifcation 등록<br><br>
 
### 패턴 풀기 미션 ###  
<p align="center"><img width="266" alt="스크린샷 2023-01-08 오후 5 43 37" src="https://user-images.githubusercontent.com/77314069/211187714-545bb4bf-047f-4ac5-92ac-88628d225a79.png"></p><br>  
 

