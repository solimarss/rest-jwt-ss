package br.com.solimar.rest.jwt.server;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSimulator {
	
	private static List<UserVO> users = new ArrayList<UserVO>();
	private static List<PessoaVO> people = new ArrayList<PessoaVO>();
	
	static {
		UserVO user1 = new UserVO();
		user1.setLogin("sol");
		user1.setPassword("123");
		users.add(user1);
		
		UserVO user2 = new UserVO();
		user2.setLogin("jo");
		user2.setPassword("12");
		users.add(user2);
		
		PessoaVO p1 = new PessoaVO();
		p1.setNome("Maria da Silva");
		p1.setIdade(35);
		people.add(p1);
		
		PessoaVO p2 = new PessoaVO();
		p2.setNome("Sim�o Pedro");
		p2.setIdade(45);
		people.add(p2);
		
	}

	public static List<UserVO> getUsers() {
		System.out.println("Database-users");
		return users;
	}

	public static List<PessoaVO> getPeople() {
		System.out.println("Database-people");
		return people;
	}

	
}
