package com.virtual.loja.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name="usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 4048798961366546485L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Schema(description="Nome do usuário",example="Matheus Henrique",required=true)
	private String nome;
	
	@Schema(description="CPF do usuário",example="000.000.000-00",required=true)
	private String cpf;
	@Schema(description="Email do usuário",example="email@email.com.br",required=true)
	private String email;
	@Schema(description="Senha do usuário",example="******",required=true)
	private String senha;
	
	@Lob
	private byte[] imagemPerfilBase64;
	
	@CreationTimestamp
	private LocalDateTime dataCadastro;
	
	@UpdateTimestamp
	private LocalDateTime dataModificacao;
	
	

}
