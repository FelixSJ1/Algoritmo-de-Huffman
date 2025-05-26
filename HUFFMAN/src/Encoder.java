import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.PriorityQueue;
import java.nio.file.StandardOpenOption;
public class Encoder {
    public static void main(String[] args) {
        String caminhoDoArquivo = "C:\\Users\\CLIENTE\\Pictures\\Testes\\Teste.txt";
        String textoDoArquivo = null;
        try {

            textoDoArquivo = Files.readString(Paths.get(caminhoDoArquivo));
            System.out.println(textoDoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }



        System.out.println();

        int [] freqLetra = new int[256];
        for(char a: textoDoArquivo.toCharArray())
            freqLetra[a]++;



        ArvoreHuffman arvore = ConstruirArvore(freqLetra);

        String encode = encode(arvore, textoDoArquivo);

        System.out.println("TEXTO CODIFICADO: " + encode);


        try {
            String caminhoSaida = "C:\\Users\\CLIENTE\\Pictures\\Testes\\HuffmanSerializado.txt";

            // Serializa a árvore em pré-ordem
            String serializacao = PreOrdem(arvore);

            // Escreve no arquivo (árvore + texto codificado)
            String conteudo = "ÁRVORE PRE-ORDEM:\n" + serializacao +
                    "\n\nTEXTO CODIFICADO:\n" + encode;

            Files.writeString(
                    Paths.get(caminhoSaida),
                    conteudo,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            System.out.println("\nArquivo salvo em: " + caminhoSaida);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }





    }

    private static String PreOrdem(ArvoreHuffman arvore) {
        if (arvore instanceof FolhaH) {
            FolhaH folha = (FolhaH) arvore; //F DE FOLHA E N DE NOS COM FILHOS
            return "F" + (int)folha.valor + ":" + folha.frequencia;
        } else {
            Node node = (Node) arvore;
            return "N" +
                    PreOrdem(node.esquerda) +
                    PreOrdem(node.direita);
        }
    }

    public static String ReceberCodigo(ArvoreHuffman arvore, StringBuffer acumulacao, char b){
    assert arvore != null;

    if( arvore instanceof FolhaH){
        FolhaH folha = (FolhaH)arvore;

        if(folha.valor == b){
            return acumulacao.toString();
        }
    } else if (arvore instanceof Node){
        Node node = (Node)arvore;

        acumulacao.append('0');
        String esquerda = ReceberCodigo(node.esquerda, acumulacao, b);
        acumulacao.deleteCharAt(acumulacao.length()-1);


        acumulacao.append('1');
        String direita = ReceberCodigo(node.direita, acumulacao, b);
        acumulacao.deleteCharAt(acumulacao.length()-1);

        if (esquerda == null) return direita; else return esquerda;
    }
        return null;
    }

    public static ArvoreHuffman ConstruirArvore(int[] frequenciaLetra){

        PriorityQueue<ArvoreHuffman> arvore = new PriorityQueue<ArvoreHuffman>();

        for (int i = 0; i < frequenciaLetra.length; i++){
            if (frequenciaLetra[i]>0){
                arvore.offer(new FolhaH(frequenciaLetra[i], (char)i));
            }
        }
        while(arvore.size()>1){
            ArvoreHuffman a = arvore.poll();
            ArvoreHuffman b = arvore.poll();

            arvore.offer(new Node(a,b));
        }
        return arvore.poll();
    }


    public static String encode(ArvoreHuffman arvore, String encode){
        assert arvore!= null;

        String encodeTexto = "";
        for( char c: encode.toCharArray()){
            encodeTexto +=(ReceberCodigo(arvore, new StringBuffer(), c));
        }
        return encodeTexto;
    }
}