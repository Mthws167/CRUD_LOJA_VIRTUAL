package com.virtual.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtual.loja.domain.Endereco;
import com.virtual.loja.service.ServicoEndereco;

@RestController
@RequestMapping("/api")
public class ControleEndereco {
	
	@Autowired
	private ServicoEndereco brasilAPIService;
	
	@GetMapping(value="/cep/{cep}")
	public Endereco findLocationForCEP(@PathVariable String cep) {
		Endereco endereco = brasilAPIService.encontrarEnderecoPorCEP(cep);
		return endereco;
	}
}