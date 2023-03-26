package AverinaldoJunior.DAO;

import AverinaldoJunior.Estoque.Fornecedor;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.Lote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class LotesDAO {

    public static ArrayList<Lote> pesquisar(int codigo) throws SQLException, DAOException {
        Connection con = Conexao.getCon();
        Statement stmt = con.createStatement();
        String sql = "select * from lote where produto = " + codigo;
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        ArrayList<Lote> lotes = null;
        while (rs.next()) {
            int quantidade = rs.getInt("quantidade");
            Date validade = rs.getDate("validade");
            Lote lote = new Lote(quantidade, validade);
            lotes.add(lote);
        }
        return lotes;
    }
}
