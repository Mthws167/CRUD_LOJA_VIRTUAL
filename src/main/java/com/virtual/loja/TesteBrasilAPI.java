package com.virtual.loja;

import org.springframework.web.client.RestTemplate;

import com.virtual.loja.domain.Endereco;

public class TesteBrasilAPI {
	public static void main(String[] args) {
		String cep = "87710090";
		String url = "https://brasilapi.com.br/api/cep/v2/"+cep;
		RestTemplate restTemplate = new RestTemplate();
		Endereco ob = restTemplate.getForObject(url, Endereco.class);
	}
}
