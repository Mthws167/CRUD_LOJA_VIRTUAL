package com.virtual.loja.dto;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import com.virtual.loja.domain.Usuario;

import lombok.Data;

@Data
public class UsuarioDTO {
	private String nome;
	private String email;
	private String cpf;
	
	public UsuarioDTO converter(Usuario usuario) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();		
		BeanUtils.copyProperties(usuario, usuarioDTO);
		return usuarioDTO;
	}
	
	public Page<UsuarioDTO> converterListaUsuarioDTO(Page<Usuario> pageUsuario){
		Page<UsuarioDTO> listaUsuarioDTO = pageUsuario.map(this::converter);
		return listaUsuarioDTO;
	}
}
