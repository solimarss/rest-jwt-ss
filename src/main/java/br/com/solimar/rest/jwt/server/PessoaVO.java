package br.com.solimar.rest.jwt.server;

import java.io.Serializable;

public class PessoaVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nome;
	Integer idade;
	
	
	public PessoaVO() {
	
	}
	public PessoaVO(String nome, Integer idade) {
		this.nome = nome;
		this.idade = idade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getIdade() {
		return idade;
	}
	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	
	

}
