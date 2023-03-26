package AverinaldoJunior.DAO;

import AverinaldoJunior.Estoque.Fornecedor;
import AverinaldoJunior.Estoque.Produto;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.Lote;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.ProdutoInexistente;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.ProdutoJaCadastrado;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.ProdutoPerecivel;

import java.net.StandardSocketOptions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProdutoDAO {

    public static void inserir(Produto p) throws DAOException, SQLException, ProdutoJaCadastrado{
        Connection con =Conexao.getCon();
        Statement stmt = con.createStatement();
        try{
            pesquisar(p.getCodigo());
            throw new ProdutoJaCadastrado();
        } catch (ProdutoInexistente e){
            String discriminante = "A";
            if(p instanceof ProdutoPerecivel){
                discriminante = "P";
            }
            else{
                discriminante = "N";
            }
            String sql = "insert into produto (cod, descr, precoCompra, precoVenda, lucro, quantidade, estoqueMinimo," +
                    " fornecedor, discriminante) values (" + p.getCodigo() + ", \'" + p.getDescricao() + "\', " +
                    p.getPrecoDeCompra() + ", \'" + p.getPrecoDeVenda() + ", \'" + p.getLucro() + ",\'" + p.getQuantidade() +
                    ", \'" + p.getEstoqueMinimo() + ", \'" + p.getFornecedor().getCnpj() + ",\'" + discriminante + ")";
            System.out.println(sql);
            stmt.execute(sql);
        }
        stmt.close();
    }

    public static Produto pesquisar(int cod) throws SQLException, DAOException, ProdutoInexistente {
        Connection con = Conexao.getCon();
        Statement stmt = con.createStatement();
        String sql = "select * from produto where codigo = " + cod;
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        Produto p = null;
        if(rs.next()) {
            int codigo = rs.getInt("codigo");
            String descr = rs.getString("descr");
            double precoCompra = rs.getDouble("precoCompra");
            double precoVenda = rs.getDouble("precoVenda");
            double lucro = rs.getDouble("lucro");
            int quantidade = rs.getInt("quantidade");
            int estoqueMinimo = rs.getInt("estoqueMinimo");
            int codFornecedor = rs.getInt("fornecedor");
            Fornecedor forn = FornecedorDAO.pesquisar(codFornecedor);
            String discriminante = rs.getString("discriminante");

            if (discriminante.equals("P")){
                p = new ProdutoPerecivel(codigo, descr, estoqueMinimo, lucro, forn);
                p.setQuantidade(quantidade);
                p.setPrecoDeCompra(precoCompra);
                p.setPrecoDeVenda(precoVenda);
                ArrayList<Lote> lotes = LotesDAO.pesquisar(p.getCodigo());
                ((ProdutoPerecivel) p).setLotes(lotes);
            }
            else if(discriminante.equals("N")){
                p = new Produto(codigo, descr, estoqueMinimo, lucro, forn);
                p.setQuantidade(quantidade);
                p.setPrecoDeCompra(precoCompra);
                p.setPrecoDeVenda(precoVenda);
            }
        }
        else {
            throw new ProdutoInexistente();
        }
        return p;
    }

}
