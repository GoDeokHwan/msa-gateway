# vault-agent.hcl
pid_file = "./vault-agent.pid"

auto_auth {
  method "userpass" {
    mount_path = "auth/userpass"
    config = {
      username = "admin"
      password = "admin1234"
    }
  }

  sink "file" {
    config = {
      path = "./vault-token"   # 토큰을 저장할 파일
    }
  }
}

cache {
  use_auto_auth_token = true
}

listener "tcp" {
  address     = "127.0.0.1:8202"  # Spring Boot가 접근할 로컬 포트
  tls_disable = true               # 개발용, 운영에서는 TLS 설정 필요
}

vault {
  address = "https://127.0.0.1:8201"  # Vault 서버 주소
  tls_skip_verify = true               # 개발용, 운영에서는 CA 인증서 필요
}
