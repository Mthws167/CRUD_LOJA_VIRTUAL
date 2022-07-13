package com.virtual.loja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.virtual.loja.domain.Categoria;

public interface RepositorioCategoria extends JpaRepository<Categoria, Long>{
	
	@Query(value ="select a from Produto a where a.categoria.descricao like %?1% ")
	Page<Categoria> findByDescricao(String descricao, Pageable page);
	
	Page<Categoria> findAll(Pageable page);

}
