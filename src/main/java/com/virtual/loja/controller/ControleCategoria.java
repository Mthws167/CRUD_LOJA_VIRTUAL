package com.virtual.loja.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.virtual.loja.domain.Categoria;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.service.ServicoCategoria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name="Categoria", description="Controle Categoria")
public class ControleCategoria {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ServicoCategoria servicoCategoria;

	@GetMapping(value = "/categoria", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Categoria>> findAll(
			@Parameter(description="Nome para pesquisa...",allowEmptyValue=true)
			@RequestBody(required = false) String descricao, 
			@Parameter(description ="Paginação",example = "{\"page\":0,\"size\":1}", allowEmptyValue = true)
			Pageable pageable) {
		if (StringUtils.isEmpty(descricao)) {
			return ResponseEntity.ok(servicoCategoria.findAll(pageable));
		} else {
			return ResponseEntity.ok(servicoCategoria.findAllByDescricao(descricao, pageable));
		}
	}
	
	@Operation(summary="Buscar ID", description="Buscar Marca pelo ID", tags={"Marca"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Categoria encontrado!"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrado!")})
	@GetMapping(value = "/categoria/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Categoria> findMarcaById(@PathVariable long id) {
		try {
			Categoria categoria = servicoCategoria.findById(id);
			return ResponseEntity.ok(categoria);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@PostMapping(value = "/categoria")
	public ResponseEntity<Categoria> addMarca(@RequestBody Categoria categoria) throws URISyntaxException {
		try {
			Categoria novoCategoria = servicoCategoria.save(categoria);
			return ResponseEntity.created(new URI("/api/categoria/" + novoCategoria.getId())).body(categoria);
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = "/categoria/{id}")
	public ResponseEntity<Categoria> updateCategoria(@Valid @RequestBody Categoria categoria, @PathVariable long id) {
		try {
			categoria.setId(id);
			servicoCategoria.update(categoria);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping(path = "/categoria/{id}")
	public ResponseEntity<Void> deleteCategoriaById(@PathVariable long id) {
		try {
			servicoCategoria.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}