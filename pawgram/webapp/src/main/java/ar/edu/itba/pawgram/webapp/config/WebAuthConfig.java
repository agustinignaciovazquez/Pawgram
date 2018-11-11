package ar.edu.itba.pawgram.webapp.config;

import java.io.File;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.pawgram.webapp.auth.RefererLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ar.edu.itba.pawgram.webapp.auth.PawgramUserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.pawgram.webapp.auth"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
@Autowired
private PawgramUserDetailsService userDetailsService;
private String remember_me_key = "l6uhSy6zyoj9YCCDr1XSR3rtsEKEYCmc";

@Override
protected void configure(final HttpSecurity http) throws Exception {

	http.userDetailsService(userDetailsService)
		.sessionManagement()
		.invalidSessionUrl("/login")
	.and().authorizeRequests()
		.antMatchers("/login","/login/**","/register","/register/**").anonymous()
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/**").authenticated()
	.and().formLogin()
		.usernameParameter("j_username").passwordParameter("j_password")
		.successHandler(successHandler())
		.loginPage("/login")
		.failureUrl("/login?error=1")
	.and().rememberMe()
		.rememberMeParameter("j_rememberme")
		.userDetailsService(userDetailsService)
		.key(remember_me_key)
		.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
	.and().logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/login")
	.and().exceptionHandling()
		.accessDeniedPage("/errors/403")
	.and().csrf().disable();
}

@Override
public void configure(final WebSecurity web) throws Exception {
	web.ignoring().antMatchers("/resources/**","/favicon.ico", "/errors/**");
}

@Bean
public BCryptPasswordEncoder bCryptPasswordEncoder(){
	return new BCryptPasswordEncoder();
}

@Bean
public AuthenticationSuccessHandler successHandler() {
	return new RefererLoginSuccessHandler("/");
}

@Autowired
public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
} 

}

