package com.glemser.invoice.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FirebaseAuthFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String idToken = authHeader.substring("Bearer ".length()).trim();
		
		try {
			FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(idToken);
			String uid = decoded.getUid();
			
			var auth = new UsernamePasswordAuthenticationToken(uid, null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			
		}catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\":\"Invalid or expired Firebase token\"}");
			return;
		}
		
		filterChain.doFilter(request, response);
		
	}

}
