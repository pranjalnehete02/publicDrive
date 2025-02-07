package cdac.acts.drive.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HomeController {

    @GetMapping("/public/info")
    public String publicEndpoint() {
        return "This is a public endpoint, accessible to everyone.";
    }

    @GetMapping("/auth/info")
    public String authEndpoint() {
        return "This endpoint requires authentication.";
    }

    @GetMapping("/admin/info")
    public String adminEndpoint() {
        return "This endpoint is restricted to users with ADMIN role.";
    }

    @GetMapping("/user/info")
    public String userEndpoint() {
        return "This endpoint is accessible to users with USER or ADMIN roles.";
    }

}
