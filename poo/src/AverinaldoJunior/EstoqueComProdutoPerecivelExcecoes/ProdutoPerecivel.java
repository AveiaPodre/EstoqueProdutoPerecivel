package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

import AverinaldoJunior.DAO.LoteDAO;
import AverinaldoJunior.DAO.ProdutoDAO;
import AverinaldoJunior.Estoque.*;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ProdutoPerecivel extends Produto {

    Date data = Date.from(Instant.now(Clock.system(ZoneId.of("America/Sao_Paulo"))));

    public ProdutoPerecivel(int cod, String desc, int min, double lucro, Fornecedor forn) {
        super(cod, desc, min, lucro, forn);
    }

    public int compra(int quant, double valor, Date validade) throws DadosInvalidos, SQLException, ClassNotFoundException {
        if (quant <= 0 || valor <= 0 || validade == null) {
            throw new DadosInvalidos();
        }

        this.setPrecoDeCompra(quant, valor);
        this.quantidade += quant;
        ProdutoDAO.updateCompra(codigo, this.precoDeCompra, this.precoDeVenda, this.quantidade);

        Lote lote = new Lote(quant, validade, this.codigo);
        LoteDAO.inserir(lote);

        return this.quantidade;
    }

    public double venda(int quant) throws DadosInvalidos, ProdutoVencido, SQLException, ClassNotFoundException {
        if (quant <= 0 || quant > this.quantidade) {
            throw new DadosInvalidos();
        }

        ArrayList<Lote> lotes = LoteDAO.pesquisar(this.codigo);
        Collections.sort(lotes, new CustomComparator());

        boolean venceu = true;
        int aux = quant;
        for (Lote lote : lotes) {
            if (lote.getValidade().after(data)) {
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
        if (venceu)
            throw new ProdutoVencido();

        ArrayList<Lote> lotesaux = new ArrayList<>();
        for (Lote lote : lotes) {
            if (lote.getQuant() != 0) {
                lotesaux.add(lote);
            }
        }
        LoteDAO.substituir(lotesaux);

        this.quantidade -= quant;
        ProdutoDAO.updateVenda(codigo, this.quantidade);
        return quant * this.precoDeVenda;
    }

    public boolean loteVencido() throws SQLException, ClassNotFoundException {
        boolean venceu = false;
        ArrayList<Lote> lotes = LoteDAO.pesquisar(this.codigo);
        for (Lote lote : lotes) {
            if (data.after(lote.getValidade()) || data.equals(lote.getValidade())) {
                venceu = true;
                break;
            }
        }
        return venceu;
    }

    public int quantVencidos() throws SQLException, ClassNotFoundException {
        int quantVencidos = 0;
        ArrayList<Lote> lotes = LoteDAO.pesquisar(this.codigo);
        for (Lote lote : lotes) {
            if (data.after(lote.getValidade()) || data.equals(lote.getValidade())) {
                quantVencidos += lote.getQuant();
            }
        }
        return quantVencidos;
    }

}

