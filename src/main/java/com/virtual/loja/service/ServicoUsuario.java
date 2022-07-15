package com.virtual.loja.service;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.virtual.loja.domain.Permissao;
import com.virtual.loja.domain.PermissaoUsuario;
import com.virtual.loja.domain.Usuario;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioPermissao;
import com.virtual.loja.repository.RepositorioPermissaoUsuario;
import com.virtual.loja.repository.RepositorioUsuario;

@Service
public class ServicoUsuario {
	private static final String EMAIL_PATTERN = 
	        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private RepositorioPermissaoUsuario repositorioPermissaoUsuario;
	
	@Autowired
	private RepositorioPermissao repositorioPermissao;
	
	private boolean existsbyId(Long id) {
		return repositorioUsuario.existsById(id);
	}
	
	private boolean existsbyCpf(String cpf) {
		return repositorioUsuario.existsByCpf(cpf);
	}
	
	public Usuario findById(Long id) throws ResourceNotFoundException {
		Usuario usuario = repositorioUsuario.findById(id).orElse(null);
		if(usuario== null) {
			throw new ResourceNotFoundException("Usuário não foi encontrado com o id: "+id);
		}else {
			return usuario;
		}
	}
	
	public Page<Usuario> findAll(Pageable pageable){
		return repositorioUsuario.findAll(pageable);
	}
	
	public Usuario save(Usuario usuario) throws BadResourceException, ResourceAlreadyExistsException{
		if(!StringUtils.isEmpty(usuario.getNome()) && validarCPF(usuario.getCpf())) {
			Matcher matcher  = pattern.matcher(usuario.getEmail());
		    if(matcher.matches()==false) usuario.setEmail("");
			 //usuario.setImagemPerfilBase64(Base64.getEncoder().encode(usuario.getImagemPerfilBase64()));;
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
			if(usuario.getCpf() != null && existsbyCpf(usuario.getCpf())) {
				throw new ResourceAlreadyExistsException("Usuário com o CPF: "+usuario.getId()+"\n já existe");
			}
			if(usuario.getId() != null && existsbyId(usuario.getId())) {
				throw new ResourceAlreadyExistsException("Usuário com o id: "+usuario.getId()+"\n já existe");
			}
			Usuario usuarioNovo = repositorioUsuario.save(usuario);
			List<Permissao> permissoes = repositorioPermissao.buscarPermissaoNome("funcionario");
			if(permissoes.size()>0) {
				PermissaoUsuario permissaoUsuario = new PermissaoUsuario();
				permissaoUsuario.setPermissao(permissoes.get(0));
				permissaoUsuario.setUsuario(usuarioNovo);
				repositorioPermissaoUsuario.save(permissaoUsuario);
			}
			return usuarioNovo;
		}else {
			BadResourceException exc = new BadResourceException("Erro ao salvar Usuário");
			exc.addErrorMessages("Usuário está vazio ou é nulo");
			throw exc;
		}
	}
	
	public void update(Usuario usuario) throws BadResourceException, ResourceNotFoundException{
		if(!StringUtils.isEmpty(usuario.getNome()) && validarCPF(usuario.getCpf())) {
		    Matcher matcher  = pattern.matcher(usuario.getEmail());
		    if(matcher.matches()==false) usuario.setEmail("");
			usuario.setImagemPerfilBase64(Base64.getEncoder().encode(usuario.getImagemPerfilBase64()));;
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
			if(!existsbyId(usuario.getId())) {
				throw new ResourceNotFoundException("Usuário não encontrado com o id: "+usuario.getId());
			}
			repositorioUsuario.save(usuario);
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException{
		if(!existsbyId(id)) {
			throw new ResourceNotFoundException("Usuário não encontrado com o id: "+id);
		}else {
			repositorioUsuario.deleteById(id);
		}
	}
	
	public Long count() {
		return repositorioUsuario.count();
	}
	public static boolean validarCPF(String CPF) {

		if (CPF.equals("00000000000") ||
				CPF.equals("11111111111") ||
				CPF.equals("22222222222") || CPF.equals("33333333333") ||
				CPF.equals("44444444444") || CPF.equals("55555555555") ||
				CPF.equals("66666666666") || CPF.equals("77777777777") ||
				CPF.equals("88888888888") || CPF.equals("99999999999") ||
				(CPF.length() != 11))
			return(false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 10;
			for (i=0; i<9; i++) {
				num = (int)(CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else dig10 = (char)(r + 48);

			sm = 0;
			peso = 11;
			for(i=0; i<10; i++) {
				num = (int)(CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else dig11 = (char)(r + 48);

			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return(true);
			else return(false);
		} catch (java.util.InputMismatchException erro) {
			return(false);
		}
	}
}