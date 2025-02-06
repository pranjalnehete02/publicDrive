package cdac.acts.drive.controller;

import cdac.acts.drive.service.ForgotPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    // Endpoint to generate password reset link
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        System.out.println(email);
        String response = forgotPasswordService.generateResetToken(email);
        logger.info("forgot password for email : {}",email);
        return ResponseEntity.ok(response);
    }
}

