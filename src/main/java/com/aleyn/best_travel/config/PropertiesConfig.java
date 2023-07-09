package com.aleyn.best_travel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:config/api_currency.properties"),
        @PropertySource(value = "classpath:config/redis.properties"),
        @PropertySource(value = "classpath:config/client_security.properties")
})
public class PropertiesConfig {
}
