package com.example.msagateway.global.config.binder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "web")
public class WebProperty {
    List<String> security;
    List<String> cors;
}
