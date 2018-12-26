package fr.dauphine.reseau.DSE;

public class Main {

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(2));
        try {

            Key key = new Key("0000111000");
        } catch (Exception e){
            System.out.println(e.getMessage()+ "prob String");
        }


    }
}
