package AverinaldoJunior.EstoqueComProdutoPerecivelExcecoes;

import java.util.Comparator;

public class CustomComparator implements Comparator<Lote> {
    public int compare(Lote o1, Lote o2) {
        return o1.getValidade().compareTo(o2.getValidade());
    }
}