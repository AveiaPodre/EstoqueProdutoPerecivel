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

    public static void inserir(Produto p) throws SQLException, ProdutoJaCadastrado, ClassNotFoundException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        try {
            pesquisar(p.getCodigo());
            throw new ProdutoJaCadastrado();
        } catch (ProdutoInexistente e) {
            String discriminante = "A";
            if (p instanceof ProdutoPerecivel) {
                discriminante = "P";
            } else {
                discriminante = "N";
            }
            String sql = "insert into produto (codigo, descr, precoCompra, precoVenda, lucro, quantidade, estoqueMinimo," +
                    " fornecedor, discriminante) values (" + p.getCodigo() + ", \'" + p.getDescricao() + "\', " +
                    p.getPrecoDeCompra() + ", " + p.getPrecoDeVenda() + ", " + p.getLucro() + ", " + p.getQuantidade() +
                    ", " + p.getEstoqueMinimo() + ", " + p.getFornecedor().getCnpj() + ",\'" + discriminante + "\')";
            System.out.printf(sql);
            stmt.execute(sql);
        }
        stmt.close();
    }

    public static Produto pesquisar(int cod) throws SQLException, ProdutoInexistente, ClassNotFoundException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from produto where codigo = " + cod;
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
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

            if (discriminante.equals("P")) {
                ProdutoPerecivel p = new ProdutoPerecivel(codigo, descr, estoqueMinimo, lucro, forn);
                p.setQuantidade(quantidade);
                p.setPrecoDeCompra(precoCompra);
                p.setPrecoDeVenda(precoVenda);
                return p;
            } else {
                Produto p = new Produto(codigo, descr, estoqueMinimo, lucro, forn);
                p.setQuantidade(quantidade);
                p.setPrecoDeCompra(precoCompra);
                p.setPrecoDeVenda(precoVenda);
                return p;
            }
        } else {
            throw new ProdutoInexistente();
        }
    }

    public static void updateCompra(int cod, double precoCompra, double precoVenda, int quant)
            throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from produto where codigo = " + cod;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "update produto set precoCompra = " + precoCompra + ", precoVenda = " + precoVenda + ", quantidade = " + quant
                    + " where codigo = " + cod;
            stmt.execute(sql);
        }
    }

    public static void updateVenda(int cod, int quant) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from produto where codigo = " + cod;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "update produto set quantidade = " + quant + " where codigo = " + cod;
            stmt.execute(sql);
        }
    }

    public static ArrayList<Produto> listarProdutos() throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from produto";
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        while (rs.next()) {
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
            Produto p = null;
            if (discriminante.equals("N")) {
                p = new Produto(codigo, descr, estoqueMinimo, lucro, forn);
                p.setPrecoDeCompra(precoCompra);
                p.setPrecoDeVenda(precoVenda);
                p.setQuantidade(quantidade);
            } else if (discriminante.equals("P")) {
                p = new ProdutoPerecivel(codigo, descr, estoqueMinimo, lucro, forn);
                p.setPrecoDeCompra(precoCompra);
                p.setPrecoDeVenda(precoVenda);
                p.setQuantidade(quantidade);
            }
            produtos.add(p);
        }
        return produtos;
    }
}
