package fr.dauphine.reseau.DSE;


public class Block implements Cloneable{
	boolean block[];
	public Block() {
		this.block=new boolean[8];
	}
    public Block(boolean[] s) throws Exception{
        this();
        this.block=new boolean[8];
        if(s.length !=8)
            throw new Exception("String of size "+s.length+" instead of 8");
        block = s;
    }

	public Block(String s) throws Exception{
		this();
		if(s.length()!=8)
			throw new Exception("String of size "+s.length()+" instead of 8");
		for(int i=0;i<8;i++) {
			if(s.charAt(i)=='0')
				this.block[i]=false;
			else {
				if(s.charAt(i)=='1') {
					this.block[i]=true;
				}
				else
					throw new Exception("Block: bit "+i+" has value "+s.charAt(i)+" different from 0 or 1");
			}
		}
	}


	public Block clone() {
		Block clone=new Block();
		for(int i=0;i<8;i++)
			clone.block[i]=this.block[i];
		return clone;
	}


	public String toString() {
		String result="";
		for(int i=0;i<8;i++) {
			result+=this.block[i]?"1":"0";
		}
		return result;
	}
}
