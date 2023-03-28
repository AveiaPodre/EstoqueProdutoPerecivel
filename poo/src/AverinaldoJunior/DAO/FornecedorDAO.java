package AverinaldoJunior.DAO;

import AverinaldoJunior.Estoque.Fornecedor;
import AverinaldoJunior.Estoque.Produto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FornecedorDAO {

    public static Fornecedor pesquisar(int cnpj) throws SQLException, ClassNotFoundException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from fornecedor where cnpj = " + cnpj;
        ResultSet rs = stmt.executeQuery(sql);

        Fornecedor f = null;
        if (rs.next()) {
            String nome = rs.getString("nome");
            f = new Fornecedor(cnpj, nome);
        }
        return f;
    }
}
