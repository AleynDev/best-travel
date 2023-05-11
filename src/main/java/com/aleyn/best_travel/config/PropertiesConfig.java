package com.aleyn.best_travel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:config/api_currency.properties")
public class PropertiesConfig {
}
