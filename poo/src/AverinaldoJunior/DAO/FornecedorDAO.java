package AverinaldoJunior.DAO;

import AverinaldoJunior.Estoque.Fornecedor;
import AverinaldoJunior.Estoque.Produto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FornecedorDAO {

    public static Fornecedor pesquisar(int cnpj) throws SQLException, DAOException {
        Connection con = Conexao.getCon();
        Statement stmt = con.createStatement();
        String sql = "select * from fornecedor where cnpj = " + cnpj;
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        Fornecedor f = null;
        if (rs.next()) {
            cnpj = rs.getInt("cnpj");
            String nome = rs.getString("nome");
            f = new Fornecedor(cnpj, nome);
        }
        return f;
    }
}
