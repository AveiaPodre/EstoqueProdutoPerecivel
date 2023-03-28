package AverinaldoJunior.DAO;

import AverinaldoJunior.DAO.Conexao;
import AverinaldoJunior.Estoque.Fornecedor;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.Lote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class LoteDAO {

    public static void substituir(ArrayList<Lote> lotesaux) throws SQLException, ClassNotFoundException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "delete from lote where validade != 0;";
        stmt.execute(sql);
        stmt.close();
        for(Lote lote: lotesaux){
            inserir(lote);
        }
    }

    public static ArrayList<Lote> pesquisar(int codigoProduto) throws SQLException, ClassNotFoundException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from lote where codigoProduto = " + codigoProduto;
        ResultSet rs = stmt.executeQuery(sql);

        ArrayList<Lote> lotes = new ArrayList<>();
        while (rs.next()) {
            int quantidade = rs.getInt("quantidade");
            Date validade = rs.getDate("validade");
            Lote l = new Lote(quantidade, validade, codigoProduto);
            lotes.add(l);
        }
        return lotes;
    }

    public static void inserir(Lote l) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        PreparedStatement stmt = con.prepareStatement("insert into lote (quantidade, validade, codigoProduto) values (?, ?, ?)");
        stmt.setInt(1, l.getQuant());
        stmt.setDate(2, new java.sql.Date(l.getValidade().getTime()));
        stmt.setInt(3, l.getCodProd());
        stmt.execute();
    }

    public Lote pesquisarPertoDeVencer(int cod_prod) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from lote where cod_prod = " + cod_prod;
        ResultSet rs = stmt.executeQuery(sql);

        Lote pertoDeVencer = null;
        Date dataAtual = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));
        while (rs.next()) {
            if (pertoDeVencer == null) {
                int quant = rs.getInt("quant");
                Date val = rs.getDate("validade");
                int codProd = rs.getInt("cod_prod");
                pertoDeVencer = new Lote(quant, val, codProd);
            }
            int quantidade = rs.getInt("quant");
            Date validade = rs.getDate("validade");
            int codigoProd = rs.getInt("cod_prod");
            if (validade.compareTo(dataAtual) > 0) {
                if (validade.compareTo(pertoDeVencer.getValidade()) < 0 && quantidade > 0) {
                    Lote novo_l = new Lote(quantidade, validade, codigoProd);
                    pertoDeVencer = novo_l;
                }
            }
        }
        if (pertoDeVencer.getQuant() > 0 && pertoDeVencer.getValidade().compareTo(dataAtual) > 0) {
            return pertoDeVencer;
        }
        return null;
    }

    public int produtosVencidos(int cod_prod) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from lote where cod_prod = " + cod_prod;
        ResultSet rs = stmt.executeQuery(sql);
        int produtosVencidos = 0;
        Date dataAtual = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));
        while (rs.next()) {
            int quantidade = rs.getInt("quant");
            Date validade = rs.getDate("validade");
            int codigoProd = rs.getInt("cod_prod");
            Lote l = new Lote(quantidade, validade, codigoProd);
            if (l.getValidade().compareTo(dataAtual) <= 0 && l.getQuant() > 0) {
                produtosVencidos += l.getQuant();
            }
        }
        return produtosVencidos;
    }

    public int produtosNaoVencidos(int cod_prod) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.conectar();
        Statement stmt = con.createStatement();
        String sql = "select * from lote where cod_prod = " + cod_prod;
        ResultSet rs = stmt.executeQuery(sql);
        int produtosNaoVencidos = 0;
        Date dataAtual = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));
        while (rs.next()) {
            int quantidade = rs.getInt("quant");
            Date validade = rs.getDate("validade");
            int codigoProd = rs.getInt("cod_prod");
            Lote l = new Lote(quantidade, validade, codigoProd);
            if (l.getValidade().compareTo(dataAtual) > 0 && l.getQuant() > 0) {
                produtosNaoVencidos += l.getQuant();
            }
        }
        return produtosNaoVencidos;
    }
}
