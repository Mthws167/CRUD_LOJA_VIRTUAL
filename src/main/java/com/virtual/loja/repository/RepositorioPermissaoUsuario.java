package com.virtual.loja.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.virtual.loja.domain.PermissaoUsuario;

public interface RepositorioPermissaoUsuario extends JpaRepository<PermissaoUsuario, Long> {
	@Query(value = "from PermissaoUsuario")
	Page<PermissaoUsuario> findAll(Pageable page);
	
}