package com.memorial.st.mst.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:/authorization.properties"),
        @PropertySource("classpath:/datasource.properties")
})
public class PropertiesConfig {
}
