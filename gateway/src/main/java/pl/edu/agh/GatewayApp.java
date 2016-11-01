package pl.edu.agh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import pl.edu.agh.filters.RequestLogFilter;

/**
 * Created by przemek on 01.11.16.
 */
@EnableZuulProxy
@SpringBootApplication
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

    @Bean
    public RequestLogFilter simpleFilter() {
        return new RequestLogFilter();
    }

}