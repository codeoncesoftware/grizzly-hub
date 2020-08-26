package fr.codeonce.grizzlyhub.auth.service.auth;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;

@Service
public class MongoUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<fr.codeonce.grizzlyhub.auth.domain.user.User> user = repository.findByEmail(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User not found");
		} else {
			return new User(user.get().getEmail(), new BCryptPasswordEncoder().encode(user.get().getPassword()),
					Arrays.asList(new SimpleGrantedAuthority("user")));
		}

	}

}
