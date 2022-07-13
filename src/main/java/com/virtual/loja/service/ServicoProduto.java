package com.virtual.loja.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.virtual.loja.domain.HistoricoPreco;
import com.virtual.loja.domain.Produto;
import com.virtual.loja.exception.BadResourceException;
import com.virtual.loja.exception.ResourceAlreadyExistsException;
import com.virtual.loja.exception.ResourceNotFoundException;
import com.virtual.loja.repository.RepositorioHistoricoPreco;
import com.virtual.loja.repository.RepositorioProduto;

@Service
public class ServicoProduto {

	@Autowired
	private RepositorioProduto repositorioProduto;

	@Autowired
	private RepositorioHistoricoPreco repositorioHistoricoPreco;

	private boolean existsById(Long id) {
		return repositorioProduto.existsById(id);
	}

	public Produto findById(Long id) throws ResourceNotFoundException {
		Produto Produto = repositorioProduto.findById(id).orElse(null);
		if (Produto == null) {
			throw new ResourceNotFoundException("Produto não encontrado com o código: " + id);
		} else
			return Produto;
	}

	public Page<Produto> findAll(Pageable pageable) {
		return repositorioProduto.findAll(pageable);
	}

	public Page<Produto> findAllByDescricao(String descricao, Pageable page) {
		Page<Produto> Produtos = repositorioProduto.findByDescricao(descricao, page);
		return Produtos;
	}

	public Page<Produto> findAllByMarca(String marca, Pageable page) {
		Page<Produto> Produtos = repositorioProduto.findByMarca(marca, page);
		return Produtos;
	}

	public Page<Produto> findAllByCategoria(String categoria, Pageable page) {
		Page<Produto> Produtos = repositorioProduto.findByCategoria(categoria, page);
		return Produtos;
	}

	public Produto save(Produto produto) throws BadResourceException, ResourceAlreadyExistsException {
		if (!StringUtils.isEmpty(produto.getDescricao())) {
			if (produto.getId() != null && existsById(produto.getId())) {
				throw new ResourceAlreadyExistsException("Produto com código: " + produto.getId() + "já existe.");
			}
			Produto produtoNovo = repositorioProduto.save(produto);
			HistoricoPreco historicoPreco = new HistoricoPreco();
			historicoPreco.setProduto(produto);
			historicoPreco.setValorCusto(produtoNovo.getValorCusto());
			historicoPreco.setValorVenda(produtoNovo.getValorVenda());
			repositorioHistoricoPreco.save(historicoPreco);
			return produtoNovo;
		} else {
			BadResourceException exc = new BadResourceException("Erro ao salvar produto");
			exc.addErrorMessages("Produto está vazio ou é nulo");
			throw exc;
		}
	}

	public void update(Produto produto) throws BadResourceException, ResourceNotFoundException {
		if (!StringUtils.isEmpty(produto.getDescricao())) {
			if (!existsById(produto.getId())) {
				throw new ResourceNotFoundException("Produto não encontrado com o código: " + produto.getId());
			}
			Produto produtoNovo = repositorioProduto.save(produto);
			HistoricoPreco historicoPreco = new HistoricoPreco();
			historicoPreco.setProduto(produto);
			historicoPreco.setValorCusto(produtoNovo.getValorCusto());
			historicoPreco.setValorVenda(produtoNovo.getValorVenda());
			repositorioHistoricoPreco.save(historicoPreco);

			repositorioProduto.save(produto);
		} else {
			BadResourceException exc = new BadResourceException("Falha ao salvar produto");
			exc.addErrorMessages("Produto está nulo ou em branco");
			throw exc;
		}
	}

	/** aumento: tipoOperador +, desconto: tipoOperador- **/
	public void atualizarValorProdutoCategoria(Long idCategoria, Double percentual, String tipoOperacao)
			throws BadResourceException, ResourceNotFoundException {
		List<Produto> produtos = repositorioProduto.buscarProdutosCategoria(idCategoria);

		for (Produto produto : produtos) {
			if (tipoOperacao.equals("+")) {
				produto.setValorVenda(produto.getValorVenda() * (1 + (percentual / 100)));
			} else if (tipoOperacao.equals("-")) {
				produto.setValorVenda(produto.getValorVenda() * (1 - (percentual / 100)));
			}
			update(produto);
		}

	}

	public void deleteById(Long id) throws ResourceNotFoundException {
		if (!existsById(id)) {
			throw new ResourceNotFoundException("Produto não encontrado com o código: " + id);
		} else {
			repositorioProduto.deleteById(id);
		}
	}

	public Long count() {
		return repositorioProduto.count();
	}
}
