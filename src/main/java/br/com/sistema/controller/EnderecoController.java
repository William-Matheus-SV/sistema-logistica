package br.com.sistema.controller;

import br.com.sistema.dao.EnderecoDAO;
import br.com.sistema.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoDAO enderecoDAO;

    @GetMapping("/mapa")
    public String mapaArmazemento(Model model){
        List<Endereco> todosEnderecos = enderecoDAO.listarTodos();
        model.addAttribute("enderecos", todosEnderecos);

        // Cria uma lista apenas com os nomes das ruas, sem repetir e ordenada
        List<String> ruas = todosEnderecos.stream()
                .map(Endereco::getRua)
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("ruas", ruas);
        return "mapa-armazem";
    }
}
