package pl.edu.agh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import pl.edu.agh.filters.RequestLogFilter;
import pl.edu.agh.logger.TLMLogger;

/**
 * Created by przemek on 01.11.16.
 */
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayService {
    private static TLMLogger logger = TLMLogger.getLogger(GatewayService.class.getName());

    public static void main(String[] args) {
        logger.info("Gateway service is running.", null);
        SpringApplication.run(GatewayService.class, args);
    }

    @Bean
    public RequestLogFilter simpleFilter() {
        return new RequestLogFilter();
    }

}