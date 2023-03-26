package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

import AverinaldoJunior.Estoque.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ProdutoPerecivel extends Produto {
    private ArrayList<Lote> lotes = new ArrayList<>();

    Date data = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));

    public ProdutoPerecivel(int cod, String desc, int min, double lucro, Fornecedor forn) {
        super(cod, desc, min, lucro, forn);
    }

    public int compra(int quant, double valor, Date validade) throws DadosInvalidos{
        if (quant <= 0 || valor <= 0 || validade == null) {
            throw new DadosInvalidos();
        }

        this.setPrecoDeCompra(quant, valor);
        this.quantidade += quant;

        Lote lote = new Lote(quant, validade);
        lotes.add(lote);

        return this.quantidade;
    }

    public double venda(int quant) throws DadosInvalidos, ProdutoVencido {
        if (quant <= 0 || quant > this.quantidade) {
            throw new DadosInvalidos();
        }

        Collections.sort(lotes, new CustomComparator());

        boolean venceu = true;
        int aux = quant;
        for(Lote lote : lotes){
            if(lote.getValidade().after(data)) {
                venceu = false;
                if (lote.getQuant() > aux) {
                    lote.setQuant(lote.getQuant() - aux);
                    break;
                } else {
                    aux -= lote.getQuant();
                    lote.setQuant(0);
                }
            }
        }
        if(venceu)
            throw new ProdutoVencido();

        ArrayList<Lote> lotesaux = new ArrayList<>();
        for(Lote lote : lotes){
            if(lote.getQuant() != 0){
                lotesaux.add(lote);
            }
        }

        this.lotes = lotesaux;

        this.quantidade -= quant;
        return quant * this.precoDeVenda;
    }

    public boolean loteVencido(){
        boolean venceu = false;
        for(Lote lote : lotes){
            if(data.after(lote.getValidade()) || data.equals(lote.getValidade())){
                venceu = true;
                break;
            }
        }
        return venceu;
    }

    public int quantVencidos(){
        int quantVencidos = 0;
        for(Lote lote : lotes){
            if(data.after(lote.getValidade()) || data.equals(lote.getValidade())){
                quantVencidos += lote.getQuant();
            }
        }
        return quantVencidos;
    }

    public void setLotes(ArrayList<Lote> lotes) {
        this.lotes = lotes;
    }
}
