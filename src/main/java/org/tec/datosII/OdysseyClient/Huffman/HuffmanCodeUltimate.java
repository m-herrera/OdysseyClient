package org.tec.datosII.OdysseyClient.Huffman;

import java.util.*;

class Node
{
    char ch;
    int freq;
    Node left = null, right = null;

    Node(char ch, int freq)
    {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(char ch, int freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

class Huffman {
    public static void encode(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null)
            return;

        if (root.left == null && root.right == null) {
            huffmanCode.put(root.ch, str);
        }
        encode(root.left, str + "0", huffmanCode);
        encode(root.right, str + "1", huffmanCode);
    }
//descomprime el codigo de huffman al original, necesita el root del arbol.
    public static String decode(Node root, String sb) {
        String ans = "";
        Node curr = root;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '0')
                curr = curr.left;
            else
                curr = curr.right;

            if (curr.left == null && curr.right == null) {
                ans += curr.ch;
                curr = root;
            }
        }
        System.out.print(ans);
        return ans;
    }
//este comprime el string y returna el cosigo de huffman
    public static String buildHuffmanTree(String text) {
        Map<Character, Integer> freq = new HashMap<>();
        char texto[] = text.toCharArray();
        Arrays.sort(texto);
        String data = "";
        for (int i = 0; i < texto.length; i++) {
            if (!freq.containsKey(texto[i])) {
                freq.put(texto[i], 0);
                data += texto[i];
            }
            freq.put(texto[i], freq.get(texto[i]) + 1);
        }
        LinkedList<Node> pq = new LinkedList<>();
        char datas[] = data.toCharArray();
        int k = 0;
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            Node nodo = new Node(datas[k], entry.getValue());
            pq.add(nodo);
            k++;
        }
        while (pq.size() != 1) {
            Node left = ((LinkedList<Node>) pq).pollFirst();
            Node right = ((LinkedList<Node>) pq).pollFirst();

            int sum = left.freq + right.freq;
            pq.add(new Node('\0', sum, left, right));
        }

        Node root = pq.peek();
        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);
        System.out.println("Huffman Codes are :\n");
        for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("\nOriginal string was :\n" + text);

        String sb = "";
        for (int i = 0; i < text.length(); i++) {
            sb += huffmanCode.get(text.charAt(i));
        }

        System.out.println("\nEncoded string is :\n" + sb);
        return sb;

    }


    public static void main(String[] args) {
        buildHuffmanTree("Que onda que pez");
    }
}