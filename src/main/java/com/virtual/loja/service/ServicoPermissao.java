package com.virtual.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.virtual.loja.domain.Permissao;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioPermissao;

@Service
public class ServicoPermissao {
	@Autowired
	private RepositorioPermissao repositorioPermissao;
	
	private boolean existsbyId(Long id) {
		return repositorioPermissao.existsById(id);
	}
	
	public Permissao findById(Long id) throws ResourceNotFoundException {
		Permissao permissao = repositorioPermissao.findById(id).orElse(null);
		if(permissao == null) {
			throw new ResourceNotFoundException("Permissão não foi encontrado com o id: "+id);
		}else {
			return permissao;
		}
	}
	
	public Page<Permissao> findAll(Pageable pageable){
		return repositorioPermissao.findAll(pageable);
	}
	
	public Permissao save(Permissao permissao) throws BadResourceException, ResourceAlreadyExistsException{
		if(!StringUtils.isEmpty(permissao.getDescricao())) {
			if(permissao.getId() != 0 && existsbyId(permissao.getId())) {
				throw new ResourceAlreadyExistsException("Permissão com o id: "+permissao.getId()+"\n já existe");
			}
			return repositorioPermissao.save(permissao);
		}else {
			BadResourceException exc = new BadResourceException("Erro ao salvar Permissão");
			exc.addErrorMessages("Permissão está vazio ou é nulo");
			throw exc;
		}
	}
	
	public void update(Permissao permissao) throws BadResourceException, ResourceNotFoundException{
		if(!StringUtils.isEmpty(permissao.getDescricao())) {
			if(!existsbyId(permissao.getId())) {
				throw new ResourceNotFoundException("Permissão não encontrado com o id: "+permissao.getId());
			}
			repositorioPermissao.save(permissao);
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException{
		if(!existsbyId(id)) {
			throw new ResourceNotFoundException("Permissão não encontrado com o id: "+id);
		}else {
			repositorioPermissao.deleteById(id);
		}
	}
	
	public Long count() {
		return repositorioPermissao.count();
	}

}