package cdac.acts.drive.service;


import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // custom method : saving user
    public void saveUser(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole()==null){
            user.setRole("ROLE_USER");
        }
        userRepository.save(user);
    }
}

