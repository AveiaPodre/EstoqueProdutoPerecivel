package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

public class DadosInvalidos extends Exception
{
    // Parameterless Constructor
    public DadosInvalidos() {}

    // Constructor that accepts a message
    public DadosInvalidos(String message)
    {
        super("Dados invalidos");
    }
}
