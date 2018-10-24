package Request_Engine;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Index {
	
	ArrayList<RDFTriplets> base;
	TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> pos; //POS
	TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> ops; //POS
	
	public Index() {
		this.base = new ArrayList<RDFTriplets>();
		this.pos = new TreeMap<Integer, TreeMap<Integer,TreeSet<Integer>>>();
		this.ops = new TreeMap<Integer, TreeMap<Integer,TreeSet<Integer>>>();
	}
	
	public void add(Dictionary dico, String s, String p, String o) {
		int ns = dico.getKeyFromValue(s);
		int np = dico.getKeyFromValue(p);
		int no = dico.getKeyFromValue(o);
		base.add(new RDFTriplets(ns, np, no));
		
		// A OPTIMISER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		if (pos.get(np) != null) {
			if (pos.get(np).get(no) != null) {
				pos.get(np).get(no).add(ns);
			} else {
				pos.get(np).put(no, new TreeSet<Integer>());
				pos.get(np).get(no).add(ns);
			}
		} else {
			pos.put(np, new TreeMap<Integer, TreeSet<Integer>>());
			pos.get(np).put(no, new TreeSet<Integer>());
			pos.get(np).get(no).add(ns);
		}
		
		if (ops.get(no) != null) {
			if (ops.get(no).get(np) != null) {
				ops.get(no).get(np).add(ns);
			} else {
				ops.get(no).put(np, new TreeSet<Integer>());
				ops.get(no).get(np).add(ns);
			}
		} else {
			ops.put(no, new TreeMap<Integer, TreeSet<Integer>>());
			ops.get(no).put(np, new TreeSet<Integer>());
			ops.get(no).get(np).add(ns);
		}
	}
	
	public ArrayList<RDFTriplets> getBase() { return base; }
	public TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> getPos() { return pos; }
	public TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> getOps() { return ops; }
	
	public int size() {
		return base.size();
	}
}
