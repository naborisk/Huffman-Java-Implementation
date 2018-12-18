import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    private static final int ALPHABET_SIZE = 256;

    public static HuffmanEncodedResult compress(final String data){
        final int[] freq = buildFreqTable(data);
        final Node root = buildHuffmanTree((freq));
        final Map<Character, String> lookupTable = buildLookupTable(root);

        return new HuffmanEncodedResult(generateEncodedData(data, lookupTable), root);
    }

    /* The function that builds the String of encoded data by
     * getting the binary code from the lookup table
     * then add them to the String using StringBuilder
     */
    private static String generateEncodedData(String data, Map<Character, String> lookupTable) {
        final StringBuilder sb = new StringBuilder();

        for(final char c: data.toCharArray()){
            sb.append(lookupTable.get(c));
        }

        return sb.toString();
    }

    /*
     * (Implementation of the) Recursive function that maps each
     * Character to String of it's binary counterpart.
     * The algorithm works through in-order tree traversal.
     * The algorithm prioritize the left node because in huffman's algorithm,
     * the higher the freq, the shorter it's binary counterpart should be
     */
    private static void buildLookupTableRecursive(Node node, String s, Map<Character, String> lookupTable) {
        if(!node.isLeaf()){
            buildLookupTableRecursive(node.left, s + '0', lookupTable);
            buildLookupTableRecursive(node.right, s+ 1, lookupTable);
        } else {
            lookupTable.put(node.c, s);
        }
    }

    // The function that builds the lookup table
    private static Map<Character, String> buildLookupTable(final Node root){

        final Map<Character, String> lookupTable = new HashMap<>();

        buildLookupTableRecursive(root, "", lookupTable);

        return lookupTable;

    }



    private static Node buildHuffmanTree(int[] freq){
        final PriorityQueue<Node> pq = new PriorityQueue<>();

        for(char i = 0; i < ALPHABET_SIZE; i++){
            if(freq[i] > 0) pq.add(new Node(i, freq[i], null, null));
        }

        if(pq.size() == 1) pq.add(new Node('\0', 1, null, null));

        while (pq.size() > 1){
            final Node left = pq.poll();
            final Node right = pq.poll();
            final Node parent = new Node('\0', left.freq + right.freq, left, right);

            pq.add(parent);
        }

        return pq.poll();
    }

    private static int[] buildFreqTable(final String data){
        final int[] freq = new int[ALPHABET_SIZE];

        for(final char c : data.toCharArray()){freq[c]++;}  // char can be treated as int

        return freq;
    }

    public String decompress(final HuffmanEncodedResult result){
        return null;
    }

    static class HuffmanEncodedResult {
        final Node root;
        final String encodedData;

        HuffmanEncodedResult(final String encodedData, final Node root){
            this.encodedData = encodedData;
            this.root = root;
        }
    }

    static class Node implements Comparable<Node> {
        private final char c;
        private final int freq;
        private final Node left, right;

        private Node(final char c,
                     final int freq,
                     final Node left,
                     final Node right){
            this.c = c;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf(){ return this.left == null && this.right == null; }

        @Override
        public int compareTo(final Node that){
            final int freqCompare = Integer.compare(this.freq, that.freq);

            if(freqCompare != 0){
                return freqCompare;
            }

            return Integer.compare(this.freq, that.freq);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();

        final int[] ft = buildFreqTable(text);
        final Node n = buildHuffmanTree(ft);
        final Map<Character, String> lookup = buildLookupTable(n);
        final String encoded = compress(text).encodedData;

        System.out.println("----------");
        lookup.forEach((key, val) -> System.out.println(key + ": " + val));
        System.out.println("----------");

        System.out.println("Encoded String: " + encoded);
    }
}