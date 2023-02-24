package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

public class ProdutoInexistente extends Exception
{
    // Parameterless Constructor
    public ProdutoInexistente() {}

    // Constructor that accepts a message
    public ProdutoInexistente(String message)
    {
        super("Produto inexistente");
    }
}
