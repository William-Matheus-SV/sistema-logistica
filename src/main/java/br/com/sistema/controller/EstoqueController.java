package br.com.sistema.controller;

import br.com.sistema.dao.EstoqueDAO;
import br.com.sistema.dao.ProdutoDAO;
import br.com.sistema.dao.EnderecoDAO;
import br.com.sistema.model.Estoque;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {
    private EstoqueDAO estoqueDAO = new EstoqueDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private EnderecoDAO enderecoDAO = new EnderecoDAO();

    // Abre a tela de Alocação
    @GetMapping("/alocar")
    public String telaAlocacao(Model model) {
        model.addAttribute("estoque", new Estoque());
        model.addAttribute("produtos", produtoDAO.listarTodos()); // Lista para o Select
        model.addAttribute("enderecosLivres", enderecoDAO.listarDisponiveis()); // Apenas os Vazios!
        return "form-alocacao";
    }

    // Executa a alocação (A transação ACID está sendo aplicado a Atomicidade Tudo ou Nada para a alocação)
    @PostMapping("/confirmar")
    public String confirmarAlocacao(@ModelAttribute("estoque") Estoque estoque) {
        estoqueDAO.alocarProduto(estoque);
        return "redirect:/enderecos/mapa"; // Após alocar, mostra o mapa atualizado
    }
}

