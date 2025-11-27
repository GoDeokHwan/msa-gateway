path "secret/data/service-a" {
  capabilities = ["read", "list"]
}

path "secret/data/service-a/*" {
  capabilities = ["read", "create", "update", "delete", "list"]
}

path "secret/metadata/service-a" {
  capabilities = ["read", "list"]
}

path "secret/metadata/service-a/*" {
  capabilities = ["read", "list"]
}
