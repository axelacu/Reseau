package fr.dauphine.reseau.DSE;

public class Key implements Cloneable{
	boolean key[];
	public Key() {
		this.key=new boolean[10];
	}
	public Key(boolean[] key){
		this.key = key;
	}
	public Key(String s) throws Exception{
		this();
		if(s.length()!=10)
			throw new Exception("String of size "+s.length()+" instead of 10");
		for(int i=0;i<10;i++) {
			if(s.charAt(i)=='0')
				this.key[i]=false;
			else {
				if(s.charAt(i)=='1') {
					this.key[i]=true;
				}
				else
					throw new Exception("Key: bit "+i+" has value "+s.charAt(i)+" different from 0 or 1");
			}
		}
	}

	public void setKey(boolean[] key){
		this.key = key;
	}
	public Key clone() {
		Key clone=new Key();
		for(int i=0;i<10;i++)
			clone.key[i]=this.key[i];
		return clone;
	}
	public int size(){
		return key.length;
	}
}
