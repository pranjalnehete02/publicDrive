package cdac.acts.drive.dataentry;



import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
public class InitialAdminSetup {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository) {
        return args -> {
            // Check if any users exist in the database
            if (userRepository.count() == 0) {
                // Create and save an initial admin user
                User admin = new User();
                admin.setUsername("admin"); // Set a default username
                admin.setPassword(passwordEncoder.encode("admin123")); // Set a default password
                admin.setRole("ROLE_ADMIN"); // Set role to ADMIN
                userRepository.save(admin);
                System.out.println("Initial admin user created with username 'admin' and password 'admin123'");
            }
        };
    }
}
