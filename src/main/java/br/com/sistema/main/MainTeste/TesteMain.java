package br.com.sistema.main.MainTeste;

import br.com.sistema.dao.ProdutoDAO;
import br.com.sistema.model.Produto;

    public class TesteMain {
        public static void main(String[] args) {

        // 1. Criamos um objeto de produto
        Produto p1 = new Produto();
        p1.setSku("ALX-001");
        p1.setNome("Cabo de Aço 10mm");
        p1.setDescricao("Material de alta resistência para elevação");

        // 2. Usamos o DAO para salvar no banco
        ProdutoDAO dao = new ProdutoDAO();
        dao.salvar(p1);
    }
}