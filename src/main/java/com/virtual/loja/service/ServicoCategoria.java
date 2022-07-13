package com.virtual.loja.service;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.virtual.loja.domain.Categoria;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioCategoria;

@Service
public class ServicoCategoria {
	
	@Autowired
	private RepositorioCategoria repositorioCategoria;
	
	private boolean existsById(Long id) {
		return repositorioCategoria.existsById(id);
	}
	
	public Categoria findById(Long id) throws ResourceNotFoundException{
		Categoria Categoria = repositorioCategoria.findById(id).orElse(null);
		if(Categoria==null) {
			throw new ResourceNotFoundException("Categoria não encontrado com o id: "+id);
		}else return Categoria;
	}
	
	public Page<Categoria> findAll(Pageable pageable){
		return repositorioCategoria.findAll(pageable);
	}
	
	public Page<Categoria> findAllByDescricao(String descricao, Pageable page){
		Page<Categoria>Categorias = repositorioCategoria.findByDescricao(descricao,page);
		return Categorias;
	}
	
	public Categoria save(Categoria categoria) throws BadResourceException,ResourceAlreadyExistsException{
		if(!StringUtils.isEmpty(categoria.getDescricao())) {
			if(categoria.getId() != null && existsById(categoria.getId())) {
				throw new ResourceAlreadyExistsException("Categoria com id: "+ categoria.getId()+"já existe.");
			}
			return repositorioCategoria.save(categoria);
		}
		else {
			BadResourceException exc =  new BadResourceException("Erro ao salvar usuário");
			exc.addErrorMessages("Usuário está vazio ou é nulo");
			throw exc;
		}
	}
	
	public void update(Categoria Categoria) throws BadResourceException, ResourceNotFoundException{
		if(!StringUtils.isEmpty(Categoria.getDescricao())) {
			if(!existsById(Categoria.getId())) {
				throw new ResourceNotFoundException("Categoria não encontrado com o id: "+Categoria.getId());
			}
			repositorioCategoria.save(Categoria);
		}
		else {
			BadResourceException  exc = new BadResourceException ("Falha ao salvar categoria");
			exc.addErrorMessages("Categoria está nulo ou em branco");
			throw exc;
		}
	}
	public void deleteById(Long id) throws ResourceNotFoundException{
		if(!existsById(id)) {
			throw new  ResourceNotFoundException("Categoria não encontrado com o id: "+id);
		}
		else {
			repositorioCategoria.deleteById(id);
		}
	}
	
	public Long count(){
		return repositorioCategoria.count();
	}
}
