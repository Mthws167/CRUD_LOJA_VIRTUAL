package com.virtual.loja.service;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.virtual.loja.domain.Marca;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioMarca;

@Service
public class ServicoMarca {
	
	@Autowired
	private RepositorioMarca repositorioMarca;
	
	private boolean existsById(Long id) {
		return repositorioMarca.existsById(id);
	}
	
	public Marca findById(Long id) throws ResourceNotFoundException{
		Marca Marca = repositorioMarca.findById(id).orElse(null);
		if(Marca==null) {
			throw new ResourceNotFoundException("Usuário não encontrado com o id: "+id);
		}else return Marca;
	}
	
	public Page<Marca> findAll(Pageable pageable){
		return repositorioMarca.findAll(pageable);
	}
	
	public Page<Marca> findAllByDescricao(String descricao, Pageable page){
		Page<Marca>Marcas = repositorioMarca.findByDescricao(descricao,page);
		return Marcas;
	}
	
	public Marca save(Marca Marca) throws BadResourceException,ResourceAlreadyExistsException{
		if(!StringUtils.isEmpty(Marca.getDescricao())) {
			if(Marca.getId() != null && existsById(Marca.getId())) {
				throw new ResourceAlreadyExistsException("Marca com id: "+ Marca.getId()+"já existe.");
			}
			return repositorioMarca.save(Marca);
		}
		else {
			BadResourceException exc =  new BadResourceException("Erro ao salvar usuário");
			exc.addErrorMessages("Usuário está vazio ou é nulo");
			throw exc;
		}
	}
	
	public void update(Marca Marca) throws BadResourceException, ResourceNotFoundException{
		if(!StringUtils.isEmpty(Marca.getDescricao())) {
			if(!existsById(Marca.getId())) {
				throw new ResourceNotFoundException("Usuário não encontrado com o id: "+Marca.getId());
			}
			repositorioMarca.save(Marca);
		}
		else {
			BadResourceException  exc = new BadResourceException ("Falha ao salvar usuário");
			exc.addErrorMessages("Usuário está nulo ou em branco");
			throw exc;
		}
	}
	public void deleteById(Long id) throws ResourceNotFoundException{
		if(!existsById(id)) {
			throw new  ResourceNotFoundException("Marca não encontrado com o id: "+id);
		}
		else {
			repositorioMarca.deleteById(id);
		}
	}
	
	public Long count(){
		return repositorioMarca.count();
	}
}
