#bin/bash
#docker exec -it vault sh
#export VAULT_ADDR='https://127.0.0.1:8201'
#export VAULT_CACERT='./vault/certs/vault.crt'
#export VAULT_SKIP_VERIFY=true
#
#vault status
#vault login hvs.LejkgpbvLi94QpsWqS0cErzq
#vault auth enable userpass
#vault write auth/userpass/users/admin password=admin1234 policies=default
#
## 권한 Role 추가
#docker exec -it vault mkdir -p /vault/policies
#docker cp ./vault/policies/service-a-policy.hcl vault:/vault/policies/service-a-policy.hcl
#
#
#docker exec -it vault sh
#vault login hvs.LejkgpbvLi94QpsWqS0cErzq
#vault policy write service-a-policy /vault/policies/service-a-policy.hcl
#vault write auth/userpass/users/admin password=admin1234 policies=service-a-policy
#vault login -tls-skip-verify -method=userpass username=admin password=admin1234
#vault kv get secret/service-a
