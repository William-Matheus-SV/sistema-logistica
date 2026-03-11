package br.com.sistema.controller;

import br.com.sistema.dao.EstoqueDAO;
import br.com.sistema.dao.ProdutoDAO;
import br.com.sistema.model.Estoque;
import br.com.sistema.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller // Diz para o Spring que essa classe controla páginas web
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private  ProdutoDAO produtoDAO;
    @Autowired
    private EstoqueDAO estoqueDAO;

    //CRUD

    @GetMapping("/novo") //(CREATE)
    public String formularioNovo(Model model) {
        model.addAttribute("produto", new Produto());
        return "form-produto";
    }
    @GetMapping("/listar") //(READ)
    public String formularioListar(Model model) {
        List<Produto> lista = produtoDAO.listarTodos();
        model.addAttribute("produtos", lista);
        return "lista-produtos";
    }
    @PostMapping("/salvar") //(UPDATE)
    public String salvar(@ModelAttribute("produto") Produto produto) {
        if (produto.getId() == null || produto.getId() == 0) {
            produtoDAO.salvar(produto); //Se  não tiver ID é produto novo
        }else{
            produtoDAO.atualizar(produto); //Se tiver ID é um edit de produto
        }
        return "redirect:/produtos/listar";
    }
    @GetMapping("/excluir/{id}") //(DELETE)
    public String excluir(@PathVariable("id") Integer id) {
        estoqueDAO.excluirPorProdutoId(id);
        produtoDAO.excluir(id);
        return "redirect:/produtos/listar";
    }
    @GetMapping("/editar/{id}")
    public String exibirEdicao(@PathVariable("id") Integer id, Model model) {
        // Busca o produto real no banco usando o novo método do DAO
        Produto produtoExistente = produtoDAO.buscarPorId(id);

        if (produtoExistente != null) {
            model.addAttribute("produto", produtoExistente);

            Estoque estoque = estoqueDAO.buscarPorProdutoId(produtoExistente.getId());
            model.addAttribute("estoque", estoque);
            return "form-produto-edicao"; // O nome do seu HTML de edição
        } else {
            // Se o produto não existir, volta para a lista (segurança)
            return "redirect:/produtos/listar";
        }
    }

    // Recebe os dados alterados e salva
    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute("produto") Produto produto) {
        produtoDAO.atualizar(produto);
        return "redirect:/produtos/listar"; // Redireciona para a lista após salvar
    }

}

