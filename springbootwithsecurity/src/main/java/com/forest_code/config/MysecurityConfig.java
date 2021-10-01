package com.forest_code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class MysecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	/*
	 * 
	 * 
	 *
	 * Basic Autho
	 * 
	 * 
	 * http .authorizeRequests().anyRequest() .authenticated() .and() .httpBasic();
	 *
	 * 
	 * 
	 * //No Password encoder protected void configure(AuthenticationManagerBuilder
	 * auth) throws Exception {
	 * 
	 * auth.inMemoryAuthentication().withUser("Honey1").password("web1").roles(
	 * "NORMAL");
	 * auth.inMemoryAuthentication().withUser("Honey2").password("web2").roles(
	 * "ADMIN");
	 * 
	 * }
	 * 
	 * 
	 * @Bean public PasswordEncoder passwordEncoder() { return
	 * NoOpPasswordEncoder.getInstance(); }
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().authorizeRequests()

				.antMatchers("/signin").permitAll().antMatchers("/public/**").hasRole("NORMAL").antMatchers("/users/**")
				.hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/signin")
				.loginProcessingUrl("/dologin").defaultSuccessUrl("/users/");

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

		/*
		 * auth.inMemoryAuthentication().withUser("Honey1").password(this.
		 * passwordEncoder().encode("web1")) .roles("NORMAL");
		 * auth.inMemoryAuthentication().withUser("Honey2").password(this.
		 * passwordEncoder().encode("web2")).roles("ADMIN");
		 */
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

}
