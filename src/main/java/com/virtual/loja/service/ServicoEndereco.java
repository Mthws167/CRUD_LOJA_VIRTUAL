package com.virtual.loja.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.virtual.loja.domain.Endereco;

@Service
public class ServicoEndereco {
	
	public Endereco encontrarEnderecoPorCEP(String cep) {
		String url = "https://brasilapi.com.br/api/cep/v2/"+cep;
		RestTemplate rest = new RestTemplate();
		Endereco endereco = rest.getForObject(url, Endereco.class);
		return endereco;
	}
}