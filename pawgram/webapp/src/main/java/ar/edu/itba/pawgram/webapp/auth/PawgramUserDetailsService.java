package ar.edu.itba.pawgram.webapp.auth;

import java.util.Arrays;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ar.edu.itba.pawgram.interfaces.UserService;
import ar.edu.itba.pawgram.model.User;

@Component
public class PawgramUserDetailsService implements UserDetailsService {
	@Autowired
	private UserService us;
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = us.findByMail(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user by the mail " + username);
		}
		final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
				new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")
		);
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}
}
