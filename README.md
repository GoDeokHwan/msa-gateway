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
requestId를 공유하여 전체적으로 흐름을 연결 고리 생성 
1. RequestIdGenerator.generate() 메서드로 유니크한 6자리 requestId생성
2. gateway서버가 WebFlux이다 보니 MDC에 저장되는게 단계를 지나면서 유실되는 현상으로 새로운 스레드에 돌입할 때 이전 값 추가
3. logback.xml에서 traceId를 requestId 로 표현되게 설정

### Step4
Exception 처리 공통화 