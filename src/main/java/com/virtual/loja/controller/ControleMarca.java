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

import com.virtual.loja.domain.Marca;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.service.ServicoMarca;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name="Marca", description="Controle Marca")
public class ControleMarca {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ServicoMarca servicoMarca;

	@GetMapping(value = "/marca", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Marca>> findAll(
			@Parameter(description="Nome para pesquisa...",allowEmptyValue=true)
			@RequestBody(required = false) String descricao, 
			@Parameter(description ="Paginação",example = "{\"page\":0,\"size\":1}", allowEmptyValue = true)
			Pageable pageable) {
		if (StringUtils.isEmpty(descricao)) {
			return ResponseEntity.ok(servicoMarca.findAll(pageable));
		} else {
			return ResponseEntity.ok(servicoMarca.findAllByDescricao(descricao, pageable));
		}
	}
	
	@Operation(summary="Buscar ID", description="Buscar Marca pelo ID", tags={"Marca"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Marca encontrado!"),
			@ApiResponse(responseCode = "404", description = "Marca não encontrado!")})
	@GetMapping(value = "/marca/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Marca> findMarcaById(@PathVariable long id) {
		try {
			Marca marca = servicoMarca.findById(id);
			return ResponseEntity.ok(marca);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@PostMapping(value = "/marca")
	public ResponseEntity<Marca> addMarca(@RequestBody Marca marca) throws URISyntaxException {
		try {
			Marca novomarca = servicoMarca.save(marca);
			return ResponseEntity.created(new URI("/api/marca/" + novomarca.getId())).body(marca);
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = "/marca/{id}")
	public ResponseEntity<Marca> updateMarca(@Valid @RequestBody Marca marca, @PathVariable long id) {
		try {
			marca.setId(id);
			servicoMarca.update(marca);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping(path = "/marca/{id}")
	public ResponseEntity<Void> deleteMarcaById(@PathVariable long id) {
		try {
			servicoMarca.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}