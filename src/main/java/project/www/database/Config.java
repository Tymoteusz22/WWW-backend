package project.www.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${spring.datasource.url}")
    public String url;
}
