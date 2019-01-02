package fr.dauphine.reseau.DSE;

import javax.security.auth.kerberos.KerberosTicket;
import java.util.ArrayList;


public class DES {
    public Block block;
    public Key key;
    public static int[] p10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static int[] p8 = {6, 3, 7, 4, 8, 5, 10, 9};
    public static int[] ip = {2, 6, 3, 1, 4, 8, 5, 7};
    public static int[] ipINV = {4, 1, 3, 5, 7, 2, 8, 6};
    public static int[] ep = {4, 1, 2, 3, 2, 3, 4, 1};
    public static int[] p4 = {2, 4, 3, 1};
    public static int[][] s0 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 3, 2}};
    public static int[][] s1 = {{0, 1, 2, 3}, {2, 0, 1, 3}, {3, 0, 1, 2}, {2, 1, 0, 3}};

    /**
     * constructeur qui se contente de créer un instance à partir des deux
     * paramètres.
     *
     * @param block block to encode
     * @param key   key to code
     */
    public DES(Block block, Key key) {
        this.key = key;
        this.block = block;
    }

    public DES(Key key) {
        this.key = key;
    }

    /**
     * Constructeur par defaut.
     */
    public DES() {

    }

    /**
     * applique la permutation P10 sur la clé (sans la modifier) et renvoie le résultat
     *
     * @param key
     * @return
     */
    public Key P10(Key key) {
        boolean[] newKey = new boolean[key.key.length];
        int count = 0;
        for (int i : p10) {
            newKey[count] = key.key[i - 1];
            count++;
        }
        return new Key(newKey);
    }

    /**
     * applique la permutation P8 sur la clé (sans la modifier) et renvoie le résultat sous la
     * forme d’un object de type Block (8 bits)
     *
     * @param key
     * @return
     */
    public Block P8(Key key) {
        boolean[] block = new boolean[8];
        int count = 0;
        for (int i : p8) {
            block[count] = key.key[i - 1];
            count++;
        }
        try {
            return new Block(block);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * effectue séparément le décalage à gauche d’un bit sur les deux blocks de 5 bits de la
     * clé (sans la modifier) et renvoie le résultat
     *
     * @param key
     * @return
     */
    public Key LS1(Key key) {
        boolean[] newKey = new boolean[key.size()];
        for (int i = key.size() - 1, j = (key.size() / 2) - 1; j > 0; i--, j--) {
            newKey[i - 1] = key.key[i];
            newKey[j - 1] = key.key[j];
        }

        //adding first of two list
        newKey[key.size() / 2 - 1] = key.key[0];
        newKey[key.size() - 1] = key.key[(key.size() / 2)];

        return new Key(newKey);
    }

    /**
     * effectue séparément le décalage à gauche de deux bits sur les deux blocks de 5 bits de
     * la clé (sans la modifier) et renvoie le résultat
     *
     * @param key
     * @return
     */
    public Key LS2(Key key) {
        DES des = new DES();
        return des.LS1(des.LS1(key));
    }

    /**
     * génère les deux sous-clés qui sont stockées dans une ArrayList qui est retournés
     * par la fonction
     *
     * @return
     */
    public ArrayList<Block> KeyGen() {
        ArrayList<Block> keyGen = new ArrayList<>();
        //copy the key given in parameter.
        Key key = this.key.clone();
        //permutation 10
        Key p10 = P10(key);
        //LS1 function
        Key ls1 = LS1(p10);
        //Producing first key.
        Block k1 = P8(ls1);
        //LS2 function.
        Key ls2 = LS2(ls1);
        //Producing key 2
        Block k2 = P8(ls2);

        //adding two key.
        keyGen.add(k1);
        keyGen.add(k2);

        return keyGen;
    }

    /**
     * applique la permutation IP sur le block (sans le modifier) et renvoie le résultat
     *
     * @param block
     * @return
     */
    public Block IP(Block block) {
        boolean[] block1 = new boolean[8];
        int count = 0;
        for (int i : ip) {
            block1[count] = block.block[i - 1];
            count++;
        }
        try {
            return new Block(block1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * remplace les 4 premiers bits de block par les
     * 4 bits de halfBlock
     *
     * @param block
     * @param halfBlock
     * @return
     */
    public Block FirstHalfChange(Block block, boolean[] halfBlock) {
        for (int i = 0; i < 4; i++) {
            block.block[i] = halfBlock[i];
        }
        return block;
    }

    /**
     * applique la permutation inverse de IP sur le block (sans le modifier) et
     * renvoie le résultat
     *
     * @param block
     * @return
     */
    public Block IPinv(Block block) {
        boolean[] block1 = new boolean[8];
        int count = 0;
        for (int i : ipINV) {
            block1[count] = block.block[i - 1];
            count++;
        }
        try {
            return new Block(block1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * applique l’ extension/permutation E/P du block (sans le modifier) et renvoie le
     * résultat
     *
     * @param block
     * @return
     */
    public Block EP(Block block) {
        boolean[] block1 = new boolean[8];
        int count = 0;
        for (int i : ep) {
            block1[count] = block.block[i - 1];
            count++;
        }
        try {
            return new Block(block1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * effectue un XOR entre les deux blocks et renvoie le résultat
     *
     * @param block
     * @param key
     * @return
     */
    public Block XOR8(Block block, Block key) {
        boolean[] block1 = new boolean[8];
        for (int i = 0; i < 8; i++) {
            if (block.block[i] == key.block[i]) {
                block1[i] = false;
            } else {
                block1[i] = true;
            }
        }
        try {
            return new Block(block1);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * effectue un XOR entre deux tableau de booléens
     * de taille 4 et renvoie le résultat
     *
     * @param block
     * @param halfBlock
     * @return
     */
    public boolean[] XOR4(Block block, boolean[] halfBlock) {
        boolean[] res = new boolean[4];
        for (int i = 0; i < 4; i++) {
            res[i] = block.block[i] ^ halfBlock[i];
        }
        return res;
    }

    /**
     * donne la valeur entière correspondant à 2 × b0 + b1
     *
     * @param b0
     * @param b1
     * @return
     */
    public int BoolToInt(boolean b0, boolean b1) {
        int B0, B1;
        if (b0 == false) {
            B0 = 0;
        } else {
            B0 = 1;
        }
        if (b1 == false) {
            B1 = 0;
        } else {
            B1 = 1;
        }

        return 2 * B0 + B1;
    }

    /**
     * donne la valeur booléenne du bit le plus à gauche de la représentation
     * binaire de i (par exemple si i=2, la représentation binaire est 01 et la valeur retournée est 0)
     *
     * @param i
     * @return
     */
    public boolean IntToBool0(int i) {
        String string = Integer.toBinaryString(i);
        String[] s = string.split("");
        if (s[0].equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * donne la valeur booléenne du bit le plus à droite de la représentation
     * binaire de i (par exemple si i=2, la représentation binaire est 01 et la valeur retournée est 1)
     *
     * @param i
     * @return
     */
    public boolean IntToBool1(int i) {
        String string = Integer.toBinaryString(i);
        String[] s = string.split("");
        if (s[s.length - 1].equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * applique la fonction S0 aux 4 premiers bits de block, et la fonction S1 aux 4
     * derniers bits de block, et renvoie le résultat sous la forme d’un tableau de 4 booléens (les 2 premiers
     * booléens correspondent à S0, et les 2 suivants à S1)
     *
     * @param block
     * @return
     */

    public boolean[] S(Block block) {
        boolean[] res = new boolean[4];

        //application  de S0 aux 4 premiers bits

        //recupérer le premier et le quatrième bit du bloc sous forme d'entier
        //cet entier correspond au numero de la ligne dans la matrice S0
        int nb1 = BoolToInt(block.block[0], block.block[3]);

        //recupérer le deuxième et le troisième bit du bloc sous forme d'entier
        // cet entier correspond au numéro de la colonne dans la matrice S0
        int nb2 = BoolToInt(block.block[1], block.block[2]);

        // récupérer l'entier dans la matrice S0
        int IntRes0 = s0[nb1][nb2];

        // convertir cet entier en binaire et l'insérer dans les deux premières cases du tableau de résultat
        res[0] = IntToBool0(IntRes0);
        res[1] = IntToBool1(IntRes0);

        //application de S1 aux 4 derniers bits

        //recupérer le premier et le quatrième bit du bloc sous forme d'entier
        //cet entier correspond au numero de la ligne dans la matrice S1
        int nb3 = BoolToInt(block.block[4], block.block[7]);

        //recupérer le deuxième et le troisième bit du bloc sous forme d'entier
        // cet entier correspond au numéro de la colonne dans la matrice S1
        int nb4 = BoolToInt(block.block[5], block.block[6]);

        // récupérer l'entier dans la matrice S0
        int IntRes1 = s1[nb3][nb4];

        // convertir cet entier en binaire et l'insérer dans les deux dernières cases du tableau de résultat
        res[2] = IntToBool0(IntRes1);
        res[3] = IntToBool1(IntRes1);

        return res;
    }

    /**
     * applique la permutation P4 sur un tableau de booléens de taille 4
     * (sans le modifier) et renvoie le résultat.
     *
     * @param halfBlock
     * @return
     */
    public boolean[] P4(boolean[] halfBlock) {
        boolean[] res = new boolean[4];
        int count = 0;
        for (int i : p4) {
            res[count] = halfBlock[i - 1];
            count++;
        }
        return res;
    }

    /**
     * échange les 4 premier bits de block avec les 4 derniers,
     * et remplace les 4 derniers bits de block par les valeurs de halfBlock (sans changer le block) et renvoie
     * le résultat décrite
     *
     * @param block
     * @param halfBlock
     * @return
     */
    public Block SW(Block block, boolean[] halfBlock) {
        Block newBlock = new Block();
        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                newBlock.block[i] = block.block[i + 4];
            } else {
                newBlock.block[i] = halfBlock[i - 4];
            }
        }
        return newBlock;
    }

    /**
     * applique la function fK décrite dans l’annexe
     *
     * @param block
     * @param key
     * @return
     */
    public boolean[] FK(Block block, Block key) {

        // découper le bloc en deux sous-blocs(Left, Right)
        Block L = new Block();
        Block R = new Block();
        for (int i = 0; i < block.block.length; i++) {
            if (i < 4) L.block[i] = block.block[i];
            else {
                R.block[i - 4] = block.block[i];
            }
        }

        //Appliquer la fonction Ep sur le bloc Right
        Block nwB = EP(R);

        //Appliquer le XOR sur le nouveau block et la clé
        Block res = XOR8(nwB, key);

        //Appliquer la fonction S au résultat précedent
        boolean[] RES = S(res);

        // appliquer P4 sur le résultat
        boolean[] resultat = P4(RES);

        //appliquer la fonction xor4 sur ce résultat et le block gauche
        boolean[] Re = XOR4(L, resultat);


        // ajouter le block droit à ce résultat
        boolean[] Resultat_final = new boolean[8];
        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                Resultat_final[i] = Re[i];
            } else {
                Resultat_final[i] = block.block[i];
            }

        }


        return Resultat_final;
    }

    /**
     * effectue le chiffrement du block de l’instance en utilisant la clé de l’instance et renvoie
     * le résultat sous la forme d’un block
     *
     * @return
     */
    public Block Encode() {
        //génerer les deux clés
        ArrayList<Block> keys = KeyGen();

        //appliquer la permutation  IP sur le bloc
        Block resultat = IP(block);

        //appliquer la première FK
        boolean[] res = FK(resultat, keys.get(0));

        resultat.block = res;

        //appliquer SW
        Block Res = SW(resultat, res);

        //appliquer une deuxième FK
        res = FK(Res, keys.get(1));


        resultat.block = res;


        //appliquer IPInv
        resultat = IPinv(resultat);


        return resultat;
    }

    /**
     * effectue le déchiffrement du block (en paramètre) en utilisant la clé de
     * l’instance et renvoie le résultat sous la forme d’un block
     */

    public Block Decode(Block b) {
        //génerer les deux clés
        ArrayList<Block> keys = KeyGen();

        //appliquer la permutation  IP sur le bloc
        Block resultat = IP(b);

        //appliquer la première FK
        boolean[] res = FK(resultat, keys.get(1));

        resultat.block = res;

        //appliquer SW
        Block Res = SW(resultat, res);

        //appliquer une deuxième FK
        res = FK(Res, keys.get(0));

        resultat.block = res;


        //appliquer IPInv
        resultat = IPinv(resultat);
        return resultat;
    }

    public static void main(String[] args) throws Exception {

        Key key = new Key("1010000010");

        Block b = new Block("10111101");
        DES des = new DES(b, key);
        System.out.println("Plaintext : " + b);

        Block BLOCK = des.Encode();
        System.out.println("Encode : " + BLOCK);

        Block res = des.Decode(BLOCK);
        System.out.println("DECODE : " + res);

        System.out.println("Fin");
    }
}
