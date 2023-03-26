package AverinaldoJunior.Estoque;

import AverinaldoJunior.DAO.DAOException;
import AverinaldoJunior.DAO.ProdutoDAO;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.*;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;

public class Estoque implements InterfaceEstoqueComExcecoes {
    Date data = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));

    public void incluir(Produto p) throws ProdutoJaCadastrado, DadosInvalidos, DAOException, SQLException {
        if(p == null || p.codigo<0 || p.descricao.isBlank() || p.fornecedor.getCnpj()<=0 || p.fornecedor == null){
            throw new DadosInvalidos();
        }
        ProdutoDAO.inserir(p);
    }

    public void comprar(int cod, int quant, double preco, Date val)  throws ProdutoInexistente, DadosInvalidos, ProdutoNaoPerecivel{
        if (cod <= 0){
            throw new DadosInvalidos();
        }
        for (Produto produto : produtos) {
            if (produto.getCodigo() == cod) {
                if(produto instanceof ProdutoPerecivel) {
                    ((ProdutoPerecivel)produto).compra(quant, preco, val);
                } else if (val != null) {
                    throw new ProdutoNaoPerecivel();
                }
                else{
                    produto.compra(quant, preco);
                }
                return;
            }
        }
        throw new ProdutoInexistente();
    }

    public double vender(int cod, int quant) throws ProdutoInexistente, ProdutoVencido, DadosInvalidos{
        if(cod <= 0 || quant <= 0){
            throw new DadosInvalidos();
        }
        for (Produto produto : produtos) {
            if (produto.getCodigo() == cod) {
                return produto.venda(quant);
            }
        }

        throw new ProdutoInexistente();
    }

    @Override
    public Produto pesquisar (int cod) throws ProdutoInexistente, DAOException, SQLException {
        ProdutoDAO.pesquisar(cod);
        throw new ProdutoInexistente();
    }

    @Override
    public ArrayList<Produto> estoqueAbaixoDoMinimo() {
        ArrayList<Produto> estoqueAbaixoDoMinimo = new ArrayList<>();
        for(Produto produto : produtos){
            if(produto.produtoAbaixoDoMinimo()) {
                estoqueAbaixoDoMinimo.add(produto);
            }
        }
        return estoqueAbaixoDoMinimo;
    }

    public int quantidade(int cod) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == cod) {
                return produto.getQuantidade();
            }
        }

        return -1;
    }

    public Fornecedor fornecedor(int cod) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == cod) {
                return produto.getFornecedor();
            }
        }

        return null;
    }

    @Override
    public ArrayList<Produto> estoqueVencido() {
        ArrayList<Produto> estoqueVencido = new ArrayList<>();
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
    public int quantidadeVencidos(int cod) throws ProdutoInexistente {
        int quantVencidos = 0;
        boolean achou = false;
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
