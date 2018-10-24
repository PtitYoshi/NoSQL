package Request_Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class Index {
	
	ArrayList<RDFTriplets> base;
	TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> pos; //POS
	TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> ops; //POS
	HashMap<Integer, Integer> frequence; // ID de la valeur ; frequence d'apparition
	
	public Index() {
		this.base = new ArrayList<RDFTriplets>();
		this.pos = new TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>>();
		this.ops = new TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>>();
		this.frequence = new HashMap<Integer, Integer>();
	}
	
	public void add(Dictionary dico, String s, String p, String o) {
		int ns = dico.getKeyFromValue(s);
		int np = dico.getKeyFromValue(p);
		int no = dico.getKeyFromValue(o);
		base.add(new RDFTriplets(ns, np, no));
		
//		frequenceAdd(ns);
		frequenceAdd(np);
		frequenceAdd(no);
		
		pos.putIfAbsent(np, new TreeMap<Integer, TreeSet<Integer>>());
		TreeMap<Integer, TreeSet<Integer>> mapNP = pos.get(np);
		mapNP.putIfAbsent(no, new TreeSet<Integer>());
		mapNP.get(no).add(ns);
		
		ops.putIfAbsent(no, new TreeMap<Integer, TreeSet<Integer>>());
		TreeMap<Integer, TreeSet<Integer>> mapNO = ops.get(no);
		mapNO.putIfAbsent(np, new TreeSet<Integer>());
		mapNO.get(np).add(ns);
	}
	
	private void frequenceAdd(int n) {
		if (frequence.containsKey(n)) {
			frequence.replace(n, frequence.get(n) + 1);
		} else {
			frequence.put(n, 1);
		}
	}
	
	public ArrayList<RDFTriplets> getBase() { return base; }
	public TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> getPos() { return pos; }
	public TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> getOps() { return ops; }
	public HashMap<Integer, Integer> getFrequence() { return frequence; }
	
	public int size() {
		return base.size();
	}
}
