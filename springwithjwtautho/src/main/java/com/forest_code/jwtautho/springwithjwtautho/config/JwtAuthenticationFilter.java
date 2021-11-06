package com.forest_code.jwtautho.springwithjwtautho.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.forest_code.jwtautho.springwithjwtautho.helper.JwtUtilHelper;
import com.forest_code.jwtautho.springwithjwtautho.services.CustomUserDetailsService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtilHelper jwtUtilHelper;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String reqtokenheader = request.getHeader("Authorization");
		String username = null;
		String JwtToken = null;

		if (reqtokenheader != null && reqtokenheader.startsWith("Bearer ")) {
			JwtToken = reqtokenheader.substring(7);

			try {
				username = this.jwtUtilHelper.extractUsername(JwtToken);
			} catch (Exception e) {
				e.printStackTrace();
			}

			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				System.out.println("Token Not valid");
			}
		}
		filterChain.doFilter(request, response);
	}
}
