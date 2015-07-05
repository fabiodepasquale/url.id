package api;

import core.SystemConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class API_InfoController {

    @RequestMapping("/api")
    public API_Info getApiInfo() {
        return new API_Info("1.0 beta", SystemConfig.domain + "api/v1/", SystemConfig.domain + "getting_started.html");
    }
}
