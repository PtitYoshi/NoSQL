package Request_Engine;

import java.util.ArrayList;

public class Index {
	
	ArrayList<RDFTriplets> store;
	
	public Index()
	{
		this.store = new ArrayList<RDFTriplets>();
	}
	
	public void add(Dictionary dico, String s, String p, String o) {
		int ns = dico.getKeyByValue(s);
		int np = dico.getKeyByValue(p);
		int no = dico.getKeyByValue(o);
		store.add(new RDFTriplets(ns, np, no));
	}
	
	public int size() {
		return store.size();
	}
	
	public String toString() {
		return store.toString();
	}

}
