package pvt.web.security.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (!Objects.equals("baiyu", s)) {
            throw new UsernameNotFoundException(s);
        }
        return new CustomUserDetails(s, "123456");
    }
}
