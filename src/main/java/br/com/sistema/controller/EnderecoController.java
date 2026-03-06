package br.com.sistema.controller;

import br.com.sistema.dao.EnderecoDAO;
import br.com.sistema.model.Endereco;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {
    private EnderecoDAO enderecoDAO = new EnderecoDAO();

    @GetMapping("/mapa")
    public String mapaArmazemento(Model model){
        model.addAttribute("enderecos", enderecoDAO.listarTodos());
        return "mapaArmazenamento";
    }
}
