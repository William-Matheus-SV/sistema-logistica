package br.com.sistema.controller;

import br.com.sistema.dao.ProdutoDAO;
import br.com.sistema.model.Produto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller // Diz para o Spring que essa classe controla páginas web
@RequestMapping("/produtos")
public class ProdutoController {
    private  ProdutoDAO produtoDAO = new ProdutoDAO();


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
        if (produto.getId() > 0){
            produtoDAO.atualizar(produto); //Se tiver ID é um edit de produto
        }else{
            produtoDAO.salvar(produto); //Se não tiver ID é um produto novo
        }
        return "redirect:/produtos/listar";
    }
    @GetMapping("/excluir/{id}") //(DELETE)
    public String excluir(@PathVariable("id") int id) {
        produtoDAO.excluir(id);
        return "redirect:/produtos/listar";
    }
}

