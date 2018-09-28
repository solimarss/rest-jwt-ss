package br.com.solimar.rest.jwt.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

@Path("/pessoa")
public class PessoaService {

	// static Logger logger = Logger.getLogger(JwtSecurityExample.class);
	static List<JsonWebKey> jwkList = null;

	static {
		System.out.println("Inside static initializer...");
		jwkList = new LinkedList<>();
		for (int kid = 1; kid <= 3; kid++) {
			JsonWebKey jwk = null;
			try {
				jwk = RsaJwkGenerator.generateJwk(2048);
				System.out
						.println("PUBLIC KEY (" + kid + "): " + jwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY));
			} catch (JoseException e) {
				e.printStackTrace();
			}
			jwk.setKeyId(String.valueOf(kid));
			jwkList.add(jwk);
		}
	}

	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response gelistAll() {
		System.out.println("list");

		List<PessoaVO> list = new ArrayList<>();
		list.add(new PessoaVO("Maria", 80));
		return Response.status(200).entity(list).build();

	}

	@Path("/authenticate")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateCredentials(@HeaderParam("username") String username,
			@HeaderParam("password") String password) throws IOException {

		System.out.println("Authenticating User Credentials...");
		System.out.println("Username: "+username);
		System.out.println("Password: "+password);

		if (username == null) {
			return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity("Username value is missing!!!")
					.build();
		}

		if (password == null) {
			return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity("Password value is missing!!!")
					.build();
		}

		if (!password.equals("123")) {
			return Response.status(Status.UNAUTHORIZED.getStatusCode()).entity("Access Denied for this functionality !!!")
					.build();
		}

		RsaJsonWebKey senderJwk = (RsaJsonWebKey) jwkList.get(0);

		senderJwk.setKeyId("1");
		System.out.println("JWK (1) ===> " + senderJwk.toJson());

		// Create the Claims, which will be the content of the JWT
		JwtClaims claims = new JwtClaims();
		claims.setIssuer("avaldes.com");
		claims.setExpirationTimeMinutesInTheFuture(1);
		claims.setGeneratedJwtId();
		claims.setIssuedAtToNow();
		claims.setNotBeforeMinutesInThePast(2);
		claims.setSubject("User_Name");
		List<String> rolesList = new ArrayList<String>();
		rolesList.add("ADMIN");
		rolesList.add("USER");

		claims.setStringListClaim("roles", rolesList);

		JsonWebSignature jws = new JsonWebSignature();

		jws.setPayload(claims.toJson());

		jws.setKeyIdHeaderValue(senderJwk.getKeyId());
		jws.setKey(senderJwk.getPrivateKey());

		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

		String jwt = null;
		try {
			jwt = jws.getCompactSerialization();
		} catch (JoseException e) {
			e.printStackTrace();
		}

		return Response.status(200).entity(jwt).build();
	}

	// --- Protected resource using JWT Token ---
	@Path("/showallitems")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllItems(@HeaderParam("token") String token) throws IOException {

		System.out.println("Inside showAllItems...");

		if (token == null) {
			return Response.status(Status.UNAUTHORIZED.getStatusCode()).entity("Access Denied for this functionality !!!")
					.build();
		}

		JsonWebKeySet jwks = new JsonWebKeySet(jwkList);
		JsonWebKey jwk = jwks.findJsonWebKey("1", null, null, null);
		System.out.println("JWK (1) ===> " + jwk.toJson());

		// Validate Token's authenticity and check claims
		JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime().setAllowedClockSkewInSeconds(30)
				.setRequireSubject().setExpectedIssuer("avaldes.com").setVerificationKey(jwk.getKey()).build();

		try {
			// Validate the JWT and process it to the Claims
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			System.out.println("JWT validation succeeded! " + jwtClaims);
		} catch (InvalidJwtException e) {

			return Response.status(Status.UNAUTHORIZED.getStatusCode()).entity("Access Denied for this functionality !!!")
					.build();
		}

		List<PessoaVO> list = DatabaseSimulator.getPeople();

		return Response.status(200).entity(list).build();
	}

}
