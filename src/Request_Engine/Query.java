package Request_Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class Query {
	String select, methode;
	ArrayList<String> where;
	ArrayList<Integer> results;
	long tps;
	
	public Query(String s, ArrayList<String> w) {
		select = s;
		where = w;
		results = new ArrayList<Integer>();
		methode = "\"";
		tps = 0;
	}
	
	public void execute(Dictionary dico, Index i) {
		long deb = System.nanoTime();
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
				methode += "POS-";
				if (i.getPos().get(np) != null) {
					if (i.getPos().get(np).get(no) != null) {
						res.addAll(i.getPos().get(np).get(no));
					}
				}
			} else {
				methode += "OPS-";
				if (i.getOps().get(no) != null) {
					if (i.getOps().get(no).get(np) != null) {
						res.addAll(i.getOps().get(no).get(np));
					}
				}
			}

			if (res.size() ==0) {
				resultat = res;
				break;
			}
			
			if (resultat == null) { resultat = res; }
			else { resultat.retainAll(res); }
		}
		methode = methode.substring(0, methode.length()-1) + "\"";
		if (resultat != null) { this.results = resultat; }
		long fin = System.nanoTime();
		tps = fin - deb;
	}
	
	public String toString() {
		String clause = "{ ";
		for (String s : where) {
			clause = clause + s + " ";
		}
		return "SELECT " + select + " WHERE " + clause + " }";
	}
}
