package AverinaldoJunior.Estoque;

import AverinaldoJunior.DAO.Conexao;
import AverinaldoJunior.DAO.ProdutoDAO;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;

public class Estoque implements InterfaceEstoqueComExcecoes {
    Date data = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));

    public void removerTudo() {
        try {
            Connection con = Conexao.conectar();
            Statement stmt = con.createStatement();
            String sql1 = "delete from produto where codigo > 0;";
            String sql2 = "delete from lote where validade != 0;";
            String sql3 = "delete from fornecedor where cnpj > 0;";
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incluir(Produto p) throws ProdutoJaCadastrado, DadosInvalidos, SQLException, ClassNotFoundException {
        if(p == null || p.codigo<0 || p.fornecedor == null || p.descricao == null || p.descricao.isBlank() ||
        p.getEstoqueMinimo() <= 0 || p.getLucro() <= 0){
            throw new DadosInvalidos();
        }
        else ProdutoDAO.inserir(p);
    }

    public void comprar(int cod, int quant, double preco, Date val) throws ProdutoInexistente, DadosInvalidos, ProdutoNaoPerecivel, SQLException, ClassNotFoundException {
        Produto p = ProdutoDAO.pesquisar(cod);
        if (cod <= 0){
            throw new DadosInvalidos();
        }
        if(p instanceof ProdutoPerecivel) {
            ((ProdutoPerecivel)p).compra(quant, preco, val);
        } else if (val != null) {
            throw new ProdutoNaoPerecivel();
        }
        else{
            p.compra(quant, preco);
        }
    }


    public double vender(int cod, int quant) throws ProdutoInexistente, ProdutoVencido, DadosInvalidos, SQLException, ClassNotFoundException {
        Produto p = ProdutoDAO.pesquisar(cod);
        if(cod <= 0 || quant <= 0){
            throw new DadosInvalidos();
        }
        return p.venda(quant);
        }

    @Override
    public Produto pesquisar (int cod) throws ProdutoInexistente, SQLException, ClassNotFoundException {
        Produto p = ProdutoDAO.pesquisar(cod);
        if (p == null) {
            throw new ProdutoInexistente();
        } else {
            return p;
        }
    }

    @Override
    public ArrayList<Produto> estoqueAbaixoDoMinimo() throws SQLException, ClassNotFoundException {
        ArrayList<Produto> estoqueAbaixoDoMinimo = new ArrayList<>();
        for(Produto produto : ProdutoDAO.listarProdutos()){
            if(produto.produtoAbaixoDoMinimo()) {
                estoqueAbaixoDoMinimo.add(produto);
            }
        }
        return estoqueAbaixoDoMinimo;
    }

    @Override
    public ArrayList<Produto> estoqueVencido() throws SQLException, ClassNotFoundException {
        ArrayList<Produto> estoqueVencido = new ArrayList<>();
        ArrayList<Produto> produtos = ProdutoDAO.listarProdutos();
        for(Produto produto : produtos){
            if(produto instanceof ProdutoPerecivel){
                if(((ProdutoPerecivel) produto).loteVencido()){
                    estoqueVencido.add(produto);
                }
            }
        }
        return estoqueVencido;
    }

    @Override
    public int quantidadeVencidos(int cod) throws ProdutoInexistente, SQLException, ClassNotFoundException {
        int quantVencidos = 0;
        boolean achou = false;
        ArrayList<Produto> produtos = ProdutoDAO.listarProdutos();
        for (Produto produto : produtos) {
            if (produto.getCodigo() == cod) {
                achou = true;
                if (produto instanceof ProdutoPerecivel) {
                    if (((ProdutoPerecivel) produto).loteVencido()) {
                        quantVencidos += ((ProdutoPerecivel) produto).quantVencidos();
                        return quantVencidos;
                    }
                }
            }
        }
        if(!achou) {
            throw new ProdutoInexistente();
        }
        return quantVencidos;
    }
}
