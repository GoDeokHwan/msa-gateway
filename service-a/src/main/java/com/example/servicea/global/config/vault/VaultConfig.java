package com.example.servicea.global.config.vault;

import com.example.servicea.global.binder.VaultProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.util.Map;

@Configuration
public class VaultConfig {

    private final VaultTemplate vaultTemplate;
    private final ObjectMapper objectMapper;

    public VaultConfig(VaultTemplate vaultTemplate, ObjectMapper objectMapper) {
        this.vaultTemplate = vaultTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Bean
    public VaultProperties vaultProperties() {
        // 1. KV v2 읽기
        Map<String, Object> response = vaultTemplate.read("secret/data/service-a", Map.class).getData();

        // 2. 실제 값은 data.data 안에 있음
        Map<String, Object> secretData = (Map<String, Object>) response.get("data");

        // 3. ObjectMapper를 통해 자동으로 POJO에 매핑
        return objectMapper.convertValue(secretData, VaultProperties.class);
    }
}
