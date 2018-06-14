package org.tec.datosII.OdysseyClient.Huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

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

class Huffman
{
    public static void encode(Node root, String str, Map<Character, String> huffmanCode)
    {
        if (root == null)
            return;

        if (root.left == null && root.right == null) {
            huffmanCode.put(root.ch, str);
        }
        encode(root.left, str+"0", huffmanCode);
        encode(root.right, str + "1", huffmanCode);
    }

    public static void decode(Node root,String sb)
    {
        String ans = "";
        Node curr = root;
        for (int i=0;i<sb.length();i++)
        {
            if (sb.charAt(i) == '0')
                curr = curr.left;
            else
                curr = curr.right;

            if (curr.left==null && curr.right==null)
            {
                ans += curr.ch;
                curr = root;
            }
        }
       System.out.print(ans);
    }

    public static void buildHuffmanTree(String text)
    {
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0 ; i < text.length(); i++) {
            if (!freq.containsKey(text.charAt(i))) {
                freq.put(text.charAt(i), 0);
            }
            freq.put(text.charAt(i), freq.get(text.charAt(i)) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>((l, r) -> l.freq - r.freq);

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() != 1)
        {
            Node left = pq.poll();
            Node right = pq.poll();

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

        String sb="";
        for (int i = 0 ; i < text.length(); i++) {
            sb+=huffmanCode.get(text.charAt(i));

        }

        System.out.println("\nEncoded string is :\n" + sb);

        // traverse the Huffman Tree again and this time
        // decode the encoded string

        System.out.println("\nDecoded string is: \n");
        decode(root, sb);
    }

    public static void main(String[] args)
    {
        buildHuffmanTree("hola");
    }
}