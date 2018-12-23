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
	public DES(Block block,Key key) {
		
	}
	public DES(){
	}

	public Key P10(Key key) {
		boolean[] newKey = new boolean[key.key.length];
		int count = 0;
		for(int i : p10){
			newKey[count] = key.key[i-1];
			count++;
		}
		return new Key(newKey);
	}
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
	public Key LS2(Key key){
		DES des = new DES();
		return des.LS1(des.LS1(key));
	}
	public ArrayList KeyGen() {
		return null;
	}
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
	public Block FirstHalfChange(Block block,boolean[] halfBlock) {
		for(int i=0; i<4;i++){
			block.block[i]=halfBlock[i];
		}
		return block;
	}


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



	public boolean[] XOR4(Block block,boolean[] halfBlock){ //TODO: peut etre deja commencer ???
		return new boolean[]{true,false};
	}
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
	public boolean IntToBool0(int i){
		String string=Integer.toBinaryString(i);
		String[] s=string.split("");
		if(s[string.length()-1].equals("1")){
			return true;
		} else {
			return false;
		}
	}




	public boolean IntToBool1(int i){
		String string=Integer.toBinaryString(i);
		String[] s=string.split("");
		if(s[0].equals("1")){
			return true;
		} else {
			return false;
		}
	}
	public boolean[] S(Block block){
		return null;
	}
	public boolean[] P4(boolean[] halfBlock){
		return null;
	}
	public Block SW(Block block,boolean[] halfBlock){
		return null;
	}
	public boolean[] FK(Block block,Block key){
		return null;
	}
	public Block Encode(){
		return null;
	}
	public Block Decode(Block block){
		return null;
	}
	public static void main(String[] args) throws Exception {

		Key key = new Key("1010000010");
		DES des = new DES();
		Key p10 =des.P10(key);
		System.out.println("P10 : "  + p10);
		Key ls1 = des.LS1(p10);
		System.out.println("LS1 : " + ls1);
		Block k1 = des.P8(ls1);
		System.out.println("P8 : "  + k1);
		Key ls2 = des.LS2(ls1);
		System.out.println("LS2 : "  + ls2);
		Block k2 = des.P8(ls2);
		System.out.println("Block : " + k2);

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
