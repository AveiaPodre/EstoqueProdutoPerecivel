package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

public class ProdutoVencido extends Exception
{
    // Parameterless Constructor
    public ProdutoVencido() {}

    // Constructor that accepts a message
    public ProdutoVencido(String message)
    {
        super("Produto vencido");
    }
}
