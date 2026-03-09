package br.com.sistema.main.MainTeste;
import br.com.sistema.dao.EnderecoDAO;
import br.com.sistema.dao.EstoqueDAO;
import br.com.sistema.dao.ProdutoDAO;
import br.com.sistema.model.Endereco;
import br.com.sistema.model.Produto;

    public class TesteMain {
        public static void main(String[] args) {

        /*/ 1. Criamos um objeto de produto
        Produto p1 = new Produto();
        p1.setSku("ALX-001");
        p1.setNome("Cabo de Aço 10mm");
        p1.setDescricao("Material de alta resistência para elevação");

        // 2. Usamos o DAO para salvar no banco
        ProdutoDAO dao = new ProdutoDAO();
        dao.salvar(p1);
*/
        Produto p2 = new Produto();
        p2.setSku("ALX-002");
        p2.setNome("Cabo de Aço 30mm");
        p2.setDescricao("Material de alta resitência para elevação");

        ProdutoDAO dao2 = new ProdutoDAO();
        dao2.atualizar(p2);

        //Testar updates de produtos, aparentemente não está funcionando

    }
}