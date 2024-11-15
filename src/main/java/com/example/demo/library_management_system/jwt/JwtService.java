package com.example.demo.library_management_system.jwt;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	

	
	public String generateToken(String username) {

		Map<String, Object> claims = new HashMap<>();

		return createToken(claims,username);

	}

	// creates JWT token

	private String createToken(Map<String, Object> claims, String username) {

		return Jwts.builder()

				.setClaims(claims)

				.setSubject(username)

				.setIssuedAt(new Date())

				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 min

				.signWith(getSignInKey(), SignatureAlgorithm.HS256)

				.compact();

	}

	private Key getSignInKey() {

		byte[] keyBytes = Decoders.BASE64.decode(SECRET);

		return Keys.hmacShaKeyFor(keyBytes);

	}
	
	public String extractUsername(String token) {
		//return extractClaim(token,Claims::getSubject);
		 try {
	            // Extract the username from the token
	            Claims claims = Jwts.parser()
	                               .setSigningKey(SECRET)
	                               .parseClaimsJws(token)
	                               .getBody();
	            return claims.getSubject();  // Return the subject (usually the username)
	        } catch (ExpiredJwtException e) {
	            throw new RuntimeException("JWT token is expired");
	        } catch (SignatureException e) {
	            throw new RuntimeException("Invalid JWT signature");
	        } catch (MalformedJwtException e) {
	            throw new RuntimeException("Malformed JWT token");
	        }
	    }
		
	

	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);	
		
	}
	
	
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}
	
	
	public Boolean validateToken(String token,UserDetails userDetails) {
		final String username=extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
