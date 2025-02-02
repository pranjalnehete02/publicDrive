package cdac.acts.drive.config;

import java.util.*;

import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomDBBasedUserDetailsService  implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // We are providing our own custom logic for verifying user authentication.
        User user = userRepository.findByUsername(username).get();

        // 1. If user is not found.
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 2. If user is legal return the instance of `UserDetails`---------------------------------------------------------------------
        // a. Mapping custom_role_system to spring_security_compatible role_system
        String authorities = user.getRole();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(authorities));

        // b. Returning UserDetails instance having user info and permissions
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                grantedAuthorities);
    }
}
