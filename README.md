### WakeMeUp ###
-----------------
# 프로젝트 소개 #
기존의 알람앱 기능에, 패턴풀기 횟수 초과로 실패시 지정한 도우미에게 문자를 전송하는 알람 어플리케이션 
* 사용한 언어/툴 
  * front-end : kotlin/android studio
  * back-end : js, node-js/visual studio code, aws
* 사용 라이브러리 
  * 패턴풀기 : https://github.com/l7naive/pattern-lock.git
  * 클라이언트-서버 통신 : Retrofit2
* 기능 
  * 회원가입, 로그인, 알람 추가, 삭제, 편집
* 개발 기간 
  * 2022.12.23 ~ 2023.1.8
-----------------
# 기능 상세 # 
1. 회원가입, 로그인  
<p align="center"><img width="269" alt="스크린샷 2023-01-08 오후 5 35 46" src="https://user-images.githubusercontent.com/77314069/211187417-7ed923d7-0f66-40f3-bd57-7b3e23299cd4.png"> <img width="270" alt="스크린샷 2023-01-08 오후 5 33 22" src="https://user-images.githubusercontent.com/77314069/211187333-33992786-3c98-4453-b29e-25decd227f09.png"></p>  
* DB접근, 대조   
* sharedpreferences로 아이디값 저장  
2. 알람 리스트 (클릭시 알람편집)  
<p align="center"><img width="259" alt="스크린샷 2023-01-08 오후 5 45 11" src="https://user-images.githubusercontent.com/77314069/211187773-42c61902-5bf6-4f44-8602-533b4618c05b.png"></p>  
* DB접근, 업데이트  
* alarmListAdapter에서 notifcation 등록  
3. 알람 추가 
<p align="center"><img width="266" alt="스크린샷 2023-01-08 오후 5 41 10" src="https://user-images.githubusercontent.com/77314069/211187614-f1ede7b0-599e-4b5c-98ac-de281838765f.png"></p>. 
* DB접근, 추가 
* alarmListAdapter에서 notifcation 등록 
4. 알람 삭제 
<p align="center"><img width="259" alt="스크린샷 2023-01-08 오후 5 45 31" src="https://user-images.githubusercontent.com/77314069/211187787-31f321ab-c202-4ba3-96e2-89a9bf8ee1aa.png"></p>. 
* DB접근, 삭제  
* alarmListAdapter에서 notifcation 등록 
5. 패턴 풀기 미션  
<p align="center"><img width="266" alt="스크린샷 2023-01-08 오후 5 43 37" src="https://user-images.githubusercontent.com/77314069/211187714-545bb4bf-047f-4ac5-92ac-88628d225a79.png"></p>. 
#* mediaPlayer

