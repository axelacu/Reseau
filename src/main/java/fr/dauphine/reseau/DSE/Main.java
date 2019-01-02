package fr.dauphine.reseau.DSE;

public class Main {

    public static void main(String[] args) throws Exception{

        Key key = new Key("1010000010");

        Block b = new Block("10111101");
        DES des = new DES(b, key);
        System.out.println("Plaintext : " + b);

        Block BLOCK = des.Encode();
        System.out.println("Encode : " + BLOCK);

        Block res = des.Decode(BLOCK);
        System.out.println("DECODE : " + res);
    }
}
