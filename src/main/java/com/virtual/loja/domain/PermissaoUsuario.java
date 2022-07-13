package com.virtual.loja.domain;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissao_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class PermissaoUsuario {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Date dataAtribuicao;
	@ManyToOne
	@JoinColumn(name="idPermissao")
	private Permissao permissao;
	@ManyToOne
	@JoinColumn(name="idUsuario")
	private Usuario usuario;
	
	@CreationTimestamp
	private LocalDateTime dataCadastro;
	
	@UpdateTimestamp
	private LocalDateTime dataModificacao;
}