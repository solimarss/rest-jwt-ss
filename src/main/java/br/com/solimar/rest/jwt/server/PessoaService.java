package br.com.solimar.rest.jwt.server;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pessoa")
public class PessoaService {

	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response gelistAll() {
		System.out.println("list");

		List<PessoaVO> list = new ArrayList<>();
		list.add(new PessoaVO("Maria", 80));
		return Response.status(200).entity(list).build();

	}

}
