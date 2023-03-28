package AverinaldoJunior.Estoque;

import AverinaldoJunior.DAO.ProdutoDAO;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.DadosInvalidos;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.ProdutoInexistente;
import AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes.ProdutoVencido;

import java.sql.SQLException;

public class Produto {
    protected int codigo;
    protected String descricao;
    protected double precoDeCompra;
    protected double precoDeVenda;
    protected double lucro;
    protected int quantidade;
    protected int estoqueMinimo;

    protected Fornecedor fornecedor;

    public Produto(int cod, String desc, int min, double lucro, Fornecedor forn){
        this.codigo = cod;
        this.descricao = desc;
        this.estoqueMinimo = min;
        this.lucro = lucro;
        this.fornecedor = forn;
    }

    public int compra(int quant, double val) throws DadosInvalidos, SQLException, ClassNotFoundException {
        if(quant <= 0 || val <= 0){
            throw new DadosInvalidos();
        }

        this.setPrecoDeCompra(quant, val);
        this.quantidade += quant;
        ProdutoDAO.updateCompra(codigo, this.precoDeCompra, this.precoDeVenda, this.quantidade);

        return this.quantidade;
    }

    public double venda(int quant) throws DadosInvalidos, ProdutoVencido, SQLException, ClassNotFoundException {
        if(quant <= 0 || quant>this.quantidade){
            throw new DadosInvalidos();
        }
        this.quantidade -= quant;
        ProdutoDAO.updateVenda(codigo, this.quantidade);

        return quant * this.precoDeVenda;
    }

    public double getPrecoDeCompra(){
        return precoDeCompra;
    }

    public void setPrecoDeCompra(int quantidade, double val) throws SQLException, ClassNotFoundException {
        this.precoDeCompra = (this.quantidade * this.precoDeCompra + quantidade * val) / (this.quantidade + quantidade);
        this.precoDeVenda = this.precoDeCompra + (this.precoDeCompra * this.lucro);
    }

    public void setPrecoDeCompra(double precoDeCompra){
        this.precoDeCompra = precoDeCompra;
    }
    public int getQuantidade(){
        return this.quantidade;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }

    public int getCodigo() {
        return codigo;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public int getEstoqueMinimo(){
        return this.estoqueMinimo;
    }

    public boolean produtoAbaixoDoMinimo(){
        return this.quantidade < this.estoqueMinimo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPrecoDeVenda(){
        return precoDeVenda;
    }

    public void setPrecoDeVenda(double precoDeVenda) {
        this.precoDeVenda = precoDeVenda;
    }

    public double getLucro(){
        return lucro;
    }

}
