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
import org.springframework.stereotype.Component;


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
//TODO THIS DOES NOT WORK SINCE CHANGES IN POM
private String remember_me_key = "SfNr/jlIHpGqcYkJYlOBn0QaICyhA6likgvTe0FA2VySVdYRkFg6e+ssv4p9jCyYICMcR6mlR7CAtHPlluBdRKK9FSBQbXKZmk6EOLzOUon5XI0SlTRQb2xejFZS48ECLHceF1evyzM+KPg6VgJTW5AgQOTobM1Ixzu25zc0YcsBqLG3ay+NQEJTNH99/ElOXS95t8YhaGlxupaCV2zycl7PGkP5Z5Q1xeGNeWpE3llKQAWCuXCx+BoOXwrELBxwegNvSnxz7qfLuNtUPv6pn39ggmcZRxqcv3cHtXSdLqajK+rvqN9c764N0HmVQSs9rXCLgG2FRHMspFW00z1dG8JfJdmR/F1yLGqSEENerdLdkKi5MLcfIND9OB5/KMh6TqM33IgGdEhwFiLAIjXR21IgDYYCkOhFkWnenVJjryoo8kINSXnlmUra/4VWDv7ph8FpT5fLdVEjs8nu/8s59kqbihnkLqpL3DOWHeQIVzK+AwkNR/Uu1VWY7V0OM2/AfzwTcaAw9HRYAcclH5VHj2BABwo2LMfPGfYR9PCnJgINPkRPM/84QVhG0+WU00Z1cRugc2GSxc/jwQQ2P6E7CVcDPnF9z7bB6kuKkeuSJHN7b8mX8ZvM5wx1zlVatUrNDMr7eGrxzFq44WMvGdjKxkaAmbMqduFN1rETdJitmyiC78fc35asdasdj5oXnpge3IHfKx2L6P3lqyp8nZZIx5NFeaztHpqkL8XwvyVQYTRToD/KLnVsWAh0sbxVPxqf815w0H63N/SoxVGR/dtvRwaA7Rrzwjld+UAUNQElbGo15Rx9UFqpAMor5MITm/FW8AMiqJsVtnNVMDehOFNfl1gBrwhL6eXiqvO8xSX+NV2gVPJ7kSe9Pr0Kq1b/qqdTG0idkJwZvSFLV0c9pu/jwx5GsMDUEdA12BBDZSp/9U9MW43AcEVXv8Qx1BsGSPhgPPTNsyXf322X6AB9kjgEmlO+yC+yW3KEmxHEQ/QxYB2brBANtAyAtztsZnFU8Z9ORTB1kcxVV0i1J45g2+SN1TTjXixvPMMXBKyUtPxfcR1kp2MN7+lG9UaJyXzaKhacqdtN6hMcYO6jCtJTt8Ok4nup0TE/Ix/y3U/aXAxnMs6TsG8wjY+7RgMCoDymolerj2a8hoNkFT2QiitkreQF2rTwO7mFjGWFNLHjyceqdGu2v9asefyEYic8rNy00sRMFY/ZpcSyM+p1pnnmsNposHBchzQ0tXIJ5J52TQNYtu7rl11/6XovVIFR1zOlvilnQX4HpPMqRQrUHnaX+O3L0up2YhRICq5mex5otHi6K0tY8EKKnHrolR81sbUf72a8o5FBJEJYsWfIv6qVBbbKfeW6mCitOteh9bCuGlcNezyKAMgIjyldoJ3UG1fR7BtTui4z7wmks6Yk0bDrptn/xED1IRMY283Opmw17PupWKDXjTsCZ09+9E8dYLfiGHZ/eo6Ahdh4nrSdIAVbj7qnVv+bz/HEQpkH0dXDeiWeHBoGyrvz4QBq3oJ2JXQOVKY3mRpExxTdoDiDT4LD5hnerodPwkX8Li1ucj/JdovXeAO0gRpM4oN0Pu0iVw3bEluW";

@Override
protected void configure(final HttpSecurity http) throws Exception {
	http.userDetailsService(userDetailsService).sessionManagement()
			.and()
			.csrf().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
			.and().authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/login").anonymous()
			.antMatchers(HttpMethod.POST, "/api/users/").anonymous()
			.antMatchers(HttpMethod.GET, "/api/users/images/**").anonymous()
			.antMatchers(HttpMethod.GET, "/api/posts/images/**").anonymous()
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

