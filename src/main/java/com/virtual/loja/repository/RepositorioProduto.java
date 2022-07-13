package com.virtual.loja.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.virtual.loja.domain.Produto;

public interface RepositorioProduto extends JpaRepository<Produto, Long>{
	
	@Query(value ="select a from Produto a where a.descricao like %?1% ")
	Page<Produto> findByDescricao(String descricao, Pageable page);
	
	@Query(value ="select a from Produto a where a.marca.descricao like %?1% ")
	Page<Produto> findByMarca(String marca, Pageable page);
	
	@Query(value ="select a from Produto a where a.categoria.descricao like %?1% ")
	Page<Produto> findByCategoria(String categoria, Pageable page);
	
	@Query(value ="select p from Produto p where p.categoria.id=?1 ")
	public List<Produto> buscarProdutosCategoria(Long idCategoria);

	@Query(value = "select p from Produto p where p.categoria.descricao like %?1%")
	public List<Produto> burscarProdutoNomeCategoria(String nomeCategoria);
	
	Page<Produto> findAll(Pageable page);
	
	

}
