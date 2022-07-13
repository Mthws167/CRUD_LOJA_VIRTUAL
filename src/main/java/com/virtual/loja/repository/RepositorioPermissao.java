package com.virtual.loja.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.virtual.loja.domain.Permissao;

public interface RepositorioPermissao extends JpaRepository<Permissao, Long> {
	@Query(value = "from Permissao")
	Page<Permissao> findAll(Pageable page);
	
	@Query(value = "from Permissao p where p.descricao=?1")
	public List<Permissao>buscarPermissaoNome(String descricao);
}