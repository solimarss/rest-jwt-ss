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

	public List<PessoaVO> showallitems(String token) {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(SERVER_URI + ENTRY_POINT + "/showallitems");

		Response response = null;
		try {

			response = target.request(MediaType.APPLICATION_JSON).header("token", token).get();

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Status: " + response.getStatus());
		System.out.println("Status Info: " + response.getStatusInfo());
		

		List<PessoaVO> lista = new ArrayList<>();
		if (response.getStatus() == 200) {
			lista = response.readEntity(new GenericType<List<PessoaVO>>() {
			});

		}else{
			System.out.println("readEntity: " + response.readEntity(String.class));
		}

		return lista;

	}

	public String authenticate() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(SERVER_URI + ENTRY_POINT + "/authenticate");

		Response response = null;
		try {

			response = target.request(MediaType.APPLICATION_JSON).header("username", "user").header("password", "123")
					.get();

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Status: " + response.getStatus());
		System.out.println("Status Info: " + response.getStatusInfo());
		
		
		if(response.getStatus() == 200){
			return response.readEntity(String.class);
		}
		else{
			System.out.println("readEntity: " + response.readEntity(String.class));
			return null;
		}
		

	}

	public static void main(String[] args) {

		ClienteRest cliente = new ClienteRest();

		System.out.println("******** Authenticate **********************");
		String token = cliente.authenticate();
		
		/*try {
			Thread.sleep(120*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		System.out.println("******** List **********************");
		List<PessoaVO> cars = cliente.showallitems(token);
		for (PessoaVO c : cars) {
			System.out.println("Resposta: " + c.getNome() + " - " + c.getIdade());
		}

	}

}
