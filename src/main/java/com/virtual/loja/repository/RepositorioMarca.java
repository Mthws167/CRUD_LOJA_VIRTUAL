package com.virtual.loja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.virtual.loja.domain.Marca;

public interface RepositorioMarca extends JpaRepository<Marca, Long>{
	
	@Query(value ="select a from Produto a where a.descricao like %?1%")
	Page<Marca> findByDescricao(String descricao, Pageable page);
	
	Page<Marca> findAll(Pageable page);

}
