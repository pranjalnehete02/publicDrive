package cdac.acts.drive.controller;
import cdac.acts.drive.dto.ResetPasswordRequest;
import cdac.acts.drive.service.ResetPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequest request) {
        System.out.println("passowrd"+request.getNewPassword());
        String response = resetPasswordService.resetPassword(token, request.getNewPassword());
        logger.info("resetting password");
        return ResponseEntity.ok(response);
    }

}
