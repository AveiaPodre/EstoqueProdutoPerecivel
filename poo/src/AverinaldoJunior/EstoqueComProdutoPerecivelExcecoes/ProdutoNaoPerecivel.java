package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

public class ProdutoNaoPerecivel extends Exception
{
    // Parameterless Constructor
    public ProdutoNaoPerecivel() {}

    // Constructor that accepts a message
    public ProdutoNaoPerecivel(String message)
    {
        super("Produto nao perecivel");
    }
}
