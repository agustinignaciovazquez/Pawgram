package ar.edu.itba.pawgram.webapp.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ar.edu.itba.pawgram.webapp.auth.PawgramUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.pawgram.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
@Autowired
private PawgramUserDetailsService userDetailsService;

@Override
protected void configure(final HttpSecurity http) throws Exception {
	http.userDetailsService(userDetailsService)
	.sessionManagement()
	.invalidSessionUrl("/login")
	.and().authorizeRequests()
	.antMatchers("/login","/register").anonymous()
	.antMatchers("/admin/**").hasRole("ADMIN")
	.antMatchers("/**").authenticated()
	.and().formLogin()
	.usernameParameter("j_username")
	.passwordParameter("j_password")
	.defaultSuccessUrl("/", false)
	.loginPage("/login")
	.and().rememberMe()
	.rememberMeParameter("j_rememberme")
	.userDetailsService(userDetailsService)
	.key("mysupersecretketthatnobodyknowsabout")
	.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
	.and().logout()
	.logoutUrl("/logout")
	.logoutSuccessUrl("/login")
	.and().exceptionHandling()
	.accessDeniedPage("/403")
	.and().csrf().disable();
}

@Override
public void configure(final WebSecurity web) throws Exception {
	web.ignoring().antMatchers("/resources/**","/favicon.ico", "/403");
}

}

