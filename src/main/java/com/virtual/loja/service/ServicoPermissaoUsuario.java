package com.virtual.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.virtual.loja.domain.PermissaoUsuario;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioPermissaoUsuario;


@Service
public class ServicoPermissaoUsuario {
	
	@Autowired
	private RepositorioPermissaoUsuario repositorioPermissaoUsuario;
	
	private boolean existsbyId(Long id) {
		return repositorioPermissaoUsuario.existsById(id);
	}
	
	public PermissaoUsuario findById(Long id) throws ResourceNotFoundException {
		PermissaoUsuario permissaoUsuario = repositorioPermissaoUsuario.findById(id).orElse(null);
		if(permissaoUsuario == null) {
			throw new ResourceNotFoundException("Produto não foi encontrado com o id: "+id);
		}else {
			return permissaoUsuario;
		}
	}
	
	public Page<PermissaoUsuario> findAll(Pageable pageable){
		return repositorioPermissaoUsuario.findAll(pageable);
	}
	
	public PermissaoUsuario save(PermissaoUsuario permissaoUsuario) throws BadResourceException, ResourceAlreadyExistsException{
		if(permissaoUsuario.getId() != null) {
			if(existsbyId(permissaoUsuario.getId())) {
				throw new ResourceAlreadyExistsException("Permissao Usuario com o id: "+permissaoUsuario.getId()+"\n já existe");
			}
			return repositorioPermissaoUsuario.save(permissaoUsuario);			
		}else {
			BadResourceException exc = new BadResourceException("Erro ao salvar Permissao Usuario");
			exc.addErrorMessages("Permissao Usuario está vazio ou é nulo");
			throw exc;
		}
	}
	
	public void update(PermissaoUsuario permissaoUsuario) throws BadResourceException, ResourceNotFoundException{
		if(permissaoUsuario.getId() != null) {
			if(!existsbyId(permissaoUsuario.getId())) {
				throw new ResourceNotFoundException("Produto não encontrado com o id: "+permissaoUsuario.getId());
			}
			repositorioPermissaoUsuario.save(permissaoUsuario);
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException{
		if(!existsbyId(id)) {
			throw new ResourceNotFoundException("Produto não encontrado com o id: "+id);
		}else {
			repositorioPermissaoUsuario.deleteById(id);
		}
	}
	
	public Long count() {
		return repositorioPermissaoUsuario.count();
	}
}