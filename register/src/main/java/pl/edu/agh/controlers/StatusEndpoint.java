package pl.edu.agh.controlers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Przemek on 23.10.2016.
 */
@RestController
public class StatusEndpoint {
    @RequestMapping("/status")
    public String home() {
        return "{status: \"ok\"}\n";
    }
}
