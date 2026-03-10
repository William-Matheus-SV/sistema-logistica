package br.com.sistema.controller;

import br.com.sistema.dao.EnderecoDAO;
import br.com.sistema.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoDAO enderecoDAO;

    @GetMapping("/mapa")
    public String mapaArmazemento(Model model){
        model.addAttribute("enderecos", enderecoDAO.listarTodos());
        return "mapa-armazem";
    }
}
