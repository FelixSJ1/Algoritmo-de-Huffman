




public class Node extends ArvoreHuffman {

public final ArvoreHuffman esquerda, direita;

    public Node(ArvoreHuffman esquerda, ArvoreHuffman direita) {
        super(esquerda.frequencia+ direita.frequencia);
        this.esquerda = esquerda;
        this.direita = direita;
    }
}
