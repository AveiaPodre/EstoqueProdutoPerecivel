package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

public class ProdutoJaCadastrado extends Exception
{
    // Parameterless Constructor
    public ProdutoJaCadastrado() {}

    // Constructor that accepts a message
    public ProdutoJaCadastrado(String message)
    {
        super("Produto ja cadastrado");
    }
}
