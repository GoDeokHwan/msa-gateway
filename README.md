# Spring Gateway 테스트

### Step1
msa-gateway -> service-a 로 API를 통신하게하는 시스템

테스트 방법
1. msa-gateway , service-a 둘다 boot Run
```shell
./msa-gateway/gradlew bootRun
./service-a/gradlew bootRun
```
2. 통신
```shell
curl --location 'http://localhost:8080/api/service-a/health'
```
### Step2
msa-gateway -> security -> service-a 로 인가된 사용자만 이용하게 하는 시스템

msa-gateway에 AuthenticationFilter.java에서 인가된 URL에 따라 auth-service와 통신하여 인증처리
타서버에는 X-USER-ID라고 해더에 user_id 정보를 추가해서 발송하게 처리 

### Step3 
Exception 처리 공통화 