API_KEY={{ with secret "secret/myproject" }}{{ .Data.data.api_key }}{{ end }}
PASSWORD={{ with secret "secret/myproject" }}{{ .Data.data.password }}{{ end }}
