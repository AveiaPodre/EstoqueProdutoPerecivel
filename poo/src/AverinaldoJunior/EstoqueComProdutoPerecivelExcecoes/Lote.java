package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;
import java.util.Date;

public class Lote {
    private int quant;
    private Date validade;
    private int codProd;

    public Lote(int quant, Date validade, int codProd) {
        this.quant = quant;
        this.validade = validade;
        this.codProd = codProd;
    }
    public int getCodProd() {
        return codProd;
    }
    public void setCodProd(int codProd) {
        this.codProd = codProd;
    }
    public int getQuant() {
        return quant;
    }
    public void setQuant(int quant) {
        this.quant = quant;
    }
    public Date getValidade() {
        return validade;
    }
    public void setValidade(Date validade) {
        this.validade = validade;
    }

}
