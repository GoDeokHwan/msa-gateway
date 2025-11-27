# Allow admin to view policies
path "sys/policies/acl/*" {
  capabilities = ["read", "list"]
}

# Allow admin to view mounts
path "sys/mounts" {
  capabilities = ["read", "list"]
}
