package org.tec.datosII.OdysseyClient.Huffman;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;
import org.tec.datosII.OdysseyClient.NioClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;

class Node{
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

    public Element toXML(){
        Element root = DocumentHelper.createElement("tree");

        root.addElement("ch").setText(String.valueOf(this.ch));
        root.addElement("freq").setText(String.valueOf(this.freq));

        Element left;
        if(this.left == null) {
            left = root.addElement("left");
            left.addElement("freq").setText("-1");
        }else{
            left = this.left.toXML();
            left.setQName(new QName("left"));
            root.add(left);
        }

        Element right;
        if(this.right == null) {
            right = root.addElement("right");
            right.addElement("freq").setText("-1");
        }else{
            right = this.right.toXML();
            right.setQName(new QName("right"));
            root.add(right);
        }

        return root;
    }
}

public class Huffman {
    public static Node root;

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
//este comprime el string y returna el codigo de huffman
    public static Element buildHuffmanTree(String text) {
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

        root = pq.peek();
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

        return null;
//        Element parent = DocumentHelper.createElement("compressed");
//        Element tree = root.toXML();
//        parent.addElement("content").addText(sb);
//        parent.add(tree);
//        System.out.print(parent.asXML());
//        return parent;
    }

    private static Node getRoot(Element element){
        if(element.elementIterator("freq").next().getText().equals("-1")){
            return null;
        }
        char ch = element.elementIterator("ch").next().getText().charAt(0);
        int freq = Integer.parseInt(element.elementIterator("freq").next().getText());
        Node left = getRoot(element.elementIterator("left").next());
        Node right = getRoot(element.elementIterator("right").next());

        return new Node(ch, freq, left, right);
    }

    public static String getTree(Element element){
        String decode = "";
        Node root = null;
        decode = element.elementIterator("content").next().getText();
        root = getRoot(element.elementIterator("tree").next());

        return decode(root, decode);
    }





    public static void main(String[] args) {
        try {
            byte[] file = Files.readAllBytes(new File("/Users/Jai/Desktop/syncWeb.mp4").toPath());
            String encodedFile = Base64.getEncoder().encodeToString(file);

            long first = System.currentTimeMillis();
            Element tree = buildHuffmanTree("amedipqodr");
            long last = System.currentTimeMillis();
            System.out.println(last - first);
            System.out.println("Duraria " + (encodedFile.length()/10 *(last - first)) );
//            System.out.println(tree.asXML());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}