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
