


abstract class ArvoreHuffman implements Comparable <ArvoreHuffman> {

    public final int frequencia;

    public ArvoreHuffman(int frequencia) {
        this.frequencia = frequencia;
    }



    public int compareTo(ArvoreHuffman arvore) {
        return frequencia - arvore.frequencia;
    }





}
