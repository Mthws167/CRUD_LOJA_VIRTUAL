package com.virtual.loja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.virtual.loja.domain.HistoricoPreco;
import com.virtual.loja.domain.Produto;

public interface RepositorioHistoricoPreco extends JpaRepository<HistoricoPreco, Long>{
	
	@Query(value ="select a from Produto a where a.id like %?1%")
	
	Page<HistoricoPreco> findAll(Pageable page);

}
