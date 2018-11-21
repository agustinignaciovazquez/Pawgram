package ar.edu.itba.pawgram.webapp.config;

import java.io.File;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.pawgram.webapp.auth.StatelessAuthenticationFilter;
import ar.edu.itba.pawgram.webapp.auth.StatelessLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ar.edu.itba.pawgram.webapp.auth.PawgramUserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@PropertySource(value="classpath:config.properties")
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.pawgram.webapp.auth"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
@Autowired
private PawgramUserDetailsService userDetailsService;

@Autowired
private StatelessLoginSuccessHandler statelessLoginSuccessHandler;

@Autowired
private StatelessAuthenticationFilter statelessAuthenticationFilter;

@Autowired
private AuthenticationEntryPoint restAuthenticationEntryPoint;

@Value(value = "${server.remember_me.key}")
private String remember_me_key;

@Override
protected void configure(final HttpSecurity http) throws Exception {
	http.userDetailsService(userDetailsService).sessionManagement()
			.and()
			.csrf().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
			.and().authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/login").anonymous()
			.antMatchers(HttpMethod.POST, "/api/users/").anonymous()
			.antMatchers(HttpMethod.POST).authenticated()
			.antMatchers(HttpMethod.DELETE).authenticated()
			.antMatchers(HttpMethod.PUT).authenticated()
			.antMatchers("/api/**").authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().usernameParameter("j_username").passwordParameter("j_password").loginProcessingUrl("/api/login")
			.successHandler(statelessLoginSuccessHandler)
			.failureHandler(new SimpleUrlAuthenticationFailureHandler())
			.and()
			.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
}

@Override
public void configure(final WebSecurity web) throws Exception {
	web.ignoring().antMatchers("/resources/**","/favicon.ico", "/errors/**");
}

@Bean
public BCryptPasswordEncoder bCryptPasswordEncoder(){
	return new BCryptPasswordEncoder();
}

//To resolve ${} in @Value
@Bean
public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
	return new PropertySourcesPlaceholderConfigurer();
}

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.authenticationProvider(authProvider());
}

@Bean
public DaoAuthenticationProvider authProvider() {
	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	authProvider.setUserDetailsService(userDetailsService);
	authProvider.setPasswordEncoder(bCryptPasswordEncoder());
	return authProvider;
}

@Bean
public String tokenSigningKey() {
	return Base64.getEncoder().encodeToString(remember_me_key.getBytes());
}
}

