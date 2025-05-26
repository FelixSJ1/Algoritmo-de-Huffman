import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Decoder {
    private static int index = 0; // Auxiliar para percorrer a string serializada

    public static void main(String[] args) {
        try {
            // Lê o arquivo serializado
            String conteudo = Files.readString(Paths.get("C:\\Users\\CLIENTE\\Pictures\\Testes\\HuffmanSerializado.txt"));

            // Extrai as partes (árvore e texto codificado)
            String[] partes = conteudo.split("\n\n");
            String serializacao = partes[0].replace("ÁRVORE PRE-ORDEM:\n", "");
            String textoCodificado = partes[1].replace("TEXTO CODIFICADO:\n", "");

            // Reconstroi a árvore
            ArvoreHuffman arvore = reconstruirArvore(serializacao);
            index = 0; // Reseta o índice para possíveis reusos

            // Decodifica o texto
            String textoOriginal = decode(arvore, textoCodificado);
            System.out.println("TEXTO DECODIFICADO: " + textoOriginal);

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }

    }


    private static ArvoreHuffman reconstruirArvore(String serializacao) {
        if (index >= serializacao.length()) return null;

        char tipo = serializacao.charAt(index++);
        if (tipo == 'F') { // Folha
            int ascii = 0;
            int freq = 0;

            // Lê até encontrar ':'
            StringBuilder sb = new StringBuilder();
            while (serializacao.charAt(index) != ':') {
                sb.append(serializacao.charAt(index++));
            }
            index++; // Pula ':'
            ascii = Integer.parseInt(sb.toString());

            // Lê a frequência
            sb = new StringBuilder();
            while (index < serializacao.length() && Character.isDigit(serializacao.charAt(index))) {
                sb.append(serializacao.charAt(index++));
            }
            freq = Integer.parseInt(sb.toString());

            return new FolhaH(freq, (char)ascii);
        }
        else if (tipo == 'N') {
            ArvoreHuffman esquerda = reconstruirArvore(serializacao);
            ArvoreHuffman direita = reconstruirArvore(serializacao);
            return new Node(esquerda, direita);
        }
        return null;
    }


    private static String decode(ArvoreHuffman arvore, String textoCodificado) {
        StringBuilder resultado = new StringBuilder();
        ArvoreHuffman atual = arvore;

        for (char bit : textoCodificado.toCharArray()) {
            if (atual instanceof Node) {
                Node node = (Node) atual;
                atual = (bit == '0') ? node.esquerda : node.direita;
            }

            if (atual instanceof FolhaH) {
                FolhaH folha = (FolhaH) atual;
                resultado.append(folha.valor);
                atual = arvore; // Volta para a raiz
            }
        }
        return resultado.toString();
    }
}