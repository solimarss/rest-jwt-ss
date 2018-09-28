package br.com.solimar.rest.jwt.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.solimar.rest.jwt.server.PessoaVO;

public class ClienteRest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final String SERVER_URI = "http://localhost:8080/rest-jwt-ss/service";

	private static final String ENTRY_POINT = "/pessoa";

	public List<PessoaVO> list() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(SERVER_URI + ENTRY_POINT + "/list");

		Response response = null;
		try {

			response = target.request(MediaType.APPLICATION_JSON).get();

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Status: " + response.getStatus());
		System.out.println("Status Info: " + response.getStatusInfo());
		

		List<PessoaVO> lista = response.readEntity(new GenericType<List<PessoaVO>>() {
		});

		return lista;

	}

	

	public static void main(String[] args) {

		ClienteRest cliente = new ClienteRest();

		List<PessoaVO> cars = cliente.list();
		for (PessoaVO c : cars) {
			System.out.println("Resposta: " + c.getNome() + " - " + c.getIdade());
		}

	}

}
