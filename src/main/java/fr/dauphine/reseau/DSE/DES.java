package fr.dauphine.reseau.DSE;
import javax.security.auth.kerberos.KerberosTicket;
import java.util.ArrayList;


public class DES {
	public Block block;
	public Key key;
	public static int[] p10= {3,5,2,7,4,10,1,9,8,6};
	public static int[] p8= {6,3,7,4,8,5,10,9};
	public static int[] ip={2,6,3,1,4,8,5,7};
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
			System.out.println(i + ", " + j );
		}
		//adding first of two list
		newKey[key.size()/2 - 1] = key.key[0];
		newKey[key.size()-1] = key.key[(key.size()/2) - 1];
		return new Key(newKey);
	}
	public Key LS2(Key key){
		key=LS1(key);
		key=LS1(key);
		return key;
	}
	public ArrayList KeyGen() {
		return null;
	}
	public Block IP(Block block){
		return null;
	}
	public Block FirstHalfChange(Block block,boolean[] halfBlock) {
		return null;
	}
	public Block IPinv(Block block){
		return null;
	}
	public Block EP(Block block){
		return null;
	}
	public Block XOR8(Block block,Block key){
		return null;
	}
	public boolean[] XOR4(Block block,boolean[] halfBlock){
		return new boolean[]{true,false};
	}
	public int BoolToInt(boolean b0,boolean b1){
		return 0;
	}
	public boolean IntToBool0(int i){
		return true;
	}
	public boolean IntToBool1(int i){
		return true;
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
		for(boolean b : key.key)
			System.out.print(b + ", ");
		for(boolean b : des.LS1(key).key)
			System.out.print(b + ", ");

		/*
		DES crypto=null;
		try {
			crypto=new DES(new Block("11110011"),new Key("1010000010"));
		}catch(Exception e) {
			System.out.println(e);
		}
			Block encod=crypto.Encode();
			Block decod=crypto.Decode(encod);*/
	}
}
