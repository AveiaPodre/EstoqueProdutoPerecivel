package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

import AverinaldoJunior.Estoque.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface InterfaceEstoqueComExcecoes {
    public void incluir(Produto p) throws ProdutoJaCadastrado, DadosInvalidos, SQLException, ClassNotFoundException;

    //Se for enviado validade e o produto não for perecível, exceção ProdutoNaoPerecivel
    public void comprar(int cod, int quant, double preco, Date val) throws ProdutoInexistente, DadosInvalidos, ProdutoNaoPerecivel, SQLException, ClassNotFoundException;

    public double vender(int cod, int quant) throws ProdutoInexistente, ProdutoVencido, DadosInvalidos, SQLException, ClassNotFoundException;

    public Produto pesquisar (int cod) throws ProdutoInexistente, SQLException, ClassNotFoundException;

    public ArrayList<Produto> estoqueAbaixoDoMinimo() throws SQLException, ClassNotFoundException;

    public ArrayList<Produto> estoqueVencido() throws SQLException, ClassNotFoundException;

    public int quantidadeVencidos(int cod) throws ProdutoInexistente, SQLException, ClassNotFoundException;

}

