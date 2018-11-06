package Request_Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class Query {
	String select;
	ArrayList<String> where;
	
	public Query(String s, ArrayList<String> w) {
		select = s;
		where = w;
	}
	
	public ArrayList<Integer> execute(Dictionary dico, Index i) {
		ArrayList<Integer> resultat = null;
		for (String w : where) {
			ArrayList<Integer> res = new ArrayList<Integer>();
			String[] clauseWhereTriplet = new String[4];
			clauseWhereTriplet = w.split(" ");
			int np = dico.getKeyFromValue(clauseWhereTriplet[1]);
			int no = dico.getKeyFromValue(clauseWhereTriplet[2]);
			
			HashMap<Integer, Integer> freq = i.getFrequence();
			int freqNp = 0, freqNo = 0;
			if (freq.get(np) != null) { freqNp = freq.get(np); }
			if (freq.get(no) != null) { freqNo = freq.get(no); }
			
			if (freqNp != 0 && freqNp <= freqNo) {
			
				if (i.getPos().get(np) != null) {
					if (i.getPos().get(np).get(no) != null) {
						res.addAll(i.getPos().get(np).get(no));
					}
				}
			} else {
				if (i.getOps().get(no) != null) {
					if (i.getOps().get(no).get(np) != null) {
						res.addAll(i.getOps().get(no).get(np));
					}
				}
			}
			
			if (resultat == null) { resultat = res; }
			else { resultat = merge(resultat, res); }
		}
		if (resultat == null) { return new ArrayList<Integer>(); }
		else { return resultat; }
	}
	
	private ArrayList<Integer> merge(ArrayList<Integer> a1, ArrayList<Integer> a2) {
		ArrayList<Integer> common = new ArrayList<Integer>(a1);
		common.retainAll(a2);
		return common;
	}
	
	public String toString() {
		String clause = "{ ";
		for (String s : where) {
			clause = clause + s + " ";
		}
		return "SELECT " + select + " WHERE " + clause + " }";
	}
}
