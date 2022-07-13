package com.virtual.loja.service;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.virtual.loja.domain.Produto;
import com.virtual.loja.domain.HistoricoPreco;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioHistoricoPreco;


@Service
public class ServicoHistoricoPreco {
	
	@Autowired
	private RepositorioHistoricoPreco repositorioHistoricoPreco;
	
	public HistoricoPreco save(HistoricoPreco historicoPreco) throws BadResourceException,ResourceAlreadyExistsException{
		if(historicoPreco.getId()!=null) {
			
				return repositorioHistoricoPreco.save(historicoPreco);
		
		}
		else {
			BadResourceException exc =  new BadResourceException("Erro ao salvar usuário");
			exc.addErrorMessages("Usuário está vazio ou é nulo");
			throw exc;
		}
	}
	
	public Long count(){
		return repositorioHistoricoPreco.count();
	}
}
