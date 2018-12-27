package fr.dauphine.reseau.DSE;
import javax.security.auth.kerberos.KerberosTicket;
import java.util.ArrayList;


public class DES {
	public Block block;
	public Key key;
	public static int[] p10= {3,5,2,7,4,10,1,9,8,6};
	public static int[] p8= {6,3,7,4,8,5,10,9};
	public static int[] ip={2,6,3,1,4,8,5,7};
	public static int[] ipINV={4,1,3,5,7,2,8,6};
	public static int[] ep={4,1,2,3,2,3,4,1};
	public static int[] p4={2,4,3,1};
	public static int[][] s0={{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};
	public static int[][] s1={{0,1,2,3},{2,0,1,3},{3,0,1,2},{2,1,0,3}};

	/**
	 * constructeur qui se contente de créer un instance à partir des deux
	 * paramètres.
	 * @param block
	 * @param key
	 */
	public DES(Block block,Key key) {
		
	}
	public DES(Key key) {
		this.key = key;
	}
	/**
	 * Constructeur par defaut.
	 */
	public DES(){
	}

	/**
	 * applique la permutation P10 sur la clé (sans la modifier) et renvoie le résultat
	 * @param key
	 * @return
	 */
	public Key P10(Key key) {
		boolean[] newKey = new boolean[key.key.length];
		int count = 0;
		for(int i : p10){
			newKey[count] = key.key[i-1];
			count++;
		}
		return new Key(newKey);
	}

	/**
	 * applique la permutation P8 sur la clé (sans la modifier) et renvoie le résultat sous la
	 * forme d’un object de type Block (8 bits)
	 * @param key
	 * @return
	 */
	public Block P8(Key key) {
		boolean[] block = new boolean[8];
		int count = 0;
		for(int i : p8){
			block[count] = key.key[i-1];
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
	 * @param key
	 * @return
	 */
	public Key LS1(Key key){
		boolean[] newKey = new boolean[key.size()];
		for(int i = key.size() - 1,j = (key.size()/2) - 1 ; j>0 ; i--,j--){
			newKey[i - 1] = key.key[i];
			newKey[j-1] = key.key[j];
		}
		//adding first of two list
		newKey[key.size()/2 - 1] = key.key[0];
		newKey[key.size()-1] = key.key[(key.size()/2)];

		return new Key(newKey);
	}

	/**
	 *effectue séparément le décalage à gauche de deux bits sur les deux blocks de 5 bits de
	 * la clé (sans la modifier) et renvoie le résultat
	 *
	 * @param key
	 * @return
	 */
	public Key LS2(Key key){
		DES des = new DES();
		return des.LS1(des.LS1(key));
	}

	/**
	 * génère les deux sous-clés qui sont stockées dans une ArrayList qui est retournés
	 * par la fonction
	 * @return
	 */
	public ArrayList KeyGen() {
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
	 * @param block
	 * @return
	 */
	public Block IP(Block block){
		boolean[] block1 = new boolean[8];
		int count = 0;
		for(int i : ip){
			block1[count] = block.block[i-1];
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
	 * @param block
	 * @param halfBlock
	 * @return
	 */
	public Block FirstHalfChange(Block block,boolean[] halfBlock) {
		for(int i=0; i<4;i++){
			block.block[i]=halfBlock[i];
		}
		return block;
	}

	/**
	 * applique la permutation inverse de IP sur le block (sans le modifier) et
	 * renvoie le résultat
	 * @param block
	 * @return
	 */
	public Block IPinv(Block block){
		boolean[] block1 = new boolean[8];
		int count = 0;
		for(int i : ipINV){
			block1[count] = block.block[i-1];
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
	 * @param block
	 * @return
	 */
	public Block EP(Block block){
		boolean[] block1 = new boolean[8];
		int count = 0;
		for(int i : ep){
			block1[count] = block.block[i-1];
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
	 * @param block
	 * @param key
	 * @return
	 */
	public Block XOR8(Block block,Block key){
		boolean[] block1=new boolean[8];
		for(int i=0; i<8;i++){
			if(block.block[i]==key.block[i]){
				block1[i]=false;
			} else {
				block1[i]=true;
			}
		}
		try {
			return new Block(block1);
		}catch(Exception e) {
			return null;
		}
	}


	/**
	 * effectue un XOR entre deux tableau de booléens
	 * de taille 4 et renvoie le résultat
	 * @param block
	 * @param halfBlock
	 * @return
	 */
	public boolean[] XOR4(Block block,boolean[] halfBlock){
		boolean[] res =new boolean[4];
		for(int i=0; i<4;i++){
			res[i] = block.block[i] ^ halfBlock[i] ;
		}
		return res;
	}

	/**
	 * donne la valeur entière correspondant à 2 × b0 + b1
	 * @param b0
	 * @param b1
	 * @return
	 */
	public int BoolToInt(boolean b0,boolean b1){
		int B0, B1;
		if(b0==false){
			B0=0;
		} else {
			B0=1;
		}
		if(b1==false){
			B1=0;
		} else {
			B1=1;
		}

		return 2*B0+B1;
	}

	/**
	 * donne la valeur booléenne du bit le plus à gauche de la représentation
	 * binaire de i (par exemple si i=2, la représentation binaire est 01 et la valeur retournée est 0)
	 * @param i
	 * @return
	 */
	//TODO : Axel : A discuter de cette fonction, je pense qu'elle n'est pas bonne. Tu as pris le bit le plus à droite et non pas à gauche.
	public boolean IntToBool0(int i){
		String string=Integer.toBinaryString(i);
		String[] s=string.split("");
		if(s[string.length()-1].equals("1")){
			return true;
		} else {
			return false;
		}
	}

	/**
	 *donne la valeur booléenne du bit le plus à droite de la représentation
	 * binaire de i (par exemple si i=2, la représentation binaire est 01 et la valeur retournée est 1)
	 * @param i
	 * @return
	 */
	//TODO : Axel : même chose que la précédente sauf que prendre la plus à droit.
	public boolean IntToBool1(int i){
		String string=Integer.toBinaryString(i);
		//TODO  : Enlever la transformation du tableau augmente la complexité.
		String[] s=string.split("");
		if(s[0].equals("1")){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * applique la fonction S0 aux 4 premiers bits de block, et la fonction S1 aux 4
	 * derniers bits de block, et renvoie le résultat sous la forme d’un tableau de 4 booléens (les 2 premiers
	 * booléens correspondent à S0, et les 2 suivants à S1)
	 * @param block
	 * @return
	 */

	//TODO : Ania
	public boolean[] S(Block block){
		return null;
	}

	/**
	 * applique la permutation P4 sur un tableau de booléens de taille 4
	 * (sans le modifier) et renvoie le résultat.
	 * @param halfBlock
	 * @return
	 */
	public boolean[] P4(boolean[] halfBlock){
		boolean[] res = new boolean[4];
		int count = 0;
		for(int i : p4){
			res[count] = halfBlock[i-1];
			count++;
		}
		return res;
	}
	//TODO : Ania
	public Block SW(Block block,boolean[] halfBlock){
		return null;
	}
	//TODO : Ania
	public boolean[] FK(Block block,Block key){
		return null;
	}
	//TODO : Ania
	public Block Encode(){
		return null;
	}
	//TODO : Ania
	public Block Decode(Block block){
		return null;
	}
	public static void main(String[] args) throws Exception {

		Key key = new Key("1010000010");
		DES des = new DES(key);

		System.out.println("Key Generation : " + des.KeyGen());

		/*
		for(boolean b : des.LS1(key).key)
			System.out.print(b + ", ");*/

		/*
		DES crypto=null;
		try {
			crypto=new DES(new Block("11110011"),new Key("1010000010"));
		}catch(Exception e) {
			System.out.println(e);
		}
			Block encod=crypto.Encode();
			Block decod=crypto.Decode(encod);*/

		Block block =new Block("11110110");
		System.out.println(des.IPinv(block));
		boolean b[] = {true, true,true, true};
		des.FirstHalfChange(block,b);
		System.out.println(block);

		Block block1 =new Block("11101010");
		System.out.println(des.EP(block1));
		System.out.println("---xor---");

		System.out.println(des.XOR8(block,block1));
		System.out.println("---binaire---");
		System.out.println(des.IntToBool0(2));



	}
}
