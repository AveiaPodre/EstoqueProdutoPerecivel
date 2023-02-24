package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

import java.util.Date;

public class Lote {
    int quant;
    Date validade;

    public Lote(int quant, Date validade){
        this.quant = quant;
        this.validade = validade;
    }

    public int getQuant() {
        return quant;
    }

    public Date getValidade(){
        return validade;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }
}
