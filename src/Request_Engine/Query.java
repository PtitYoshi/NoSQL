package Request_Engine;

import java.util.ArrayList;

public class Query {
	String select;
	ArrayList<String> where;
	
	public Query(String s){//, ArrayList<String> w) {
		select = "?v0";
		where = new ArrayList<String>();
		where.add("?v0 <http://db.uwaterloo.ca/~galuc/wsdbm/subscribes> <http://db.uwaterloo.ca/~galuc/wsdbm/Website1> .");
	}
	
	public ArrayList<Integer> execute(Dictionary dico, Index i) {
		ArrayList<Integer> resultat = null;
		for (String w : where) {
			ArrayList<Integer> res = new ArrayList<Integer>();
			
			String[] clauseWhereTriplet = new String[3];
			clauseWhereTriplet = w.split(" ");
//			int ns = dico.getKeyFromValue(clauseWhereTriplet[0]);
			int np = dico.getKeyFromValue(clauseWhereTriplet[1]);
			int no = dico.getKeyFromValue(clauseWhereTriplet[2]);
			
			
			// optimisation possible : getPos ou getOps
			if (i.getPos().get(np) != null) {
				if (i.getPos().get(np).get(no) != null) {
					res.addAll(i.getPos().get(np).get(no));
				}
			}
									
			if (resultat == null) { resultat = res; }
			else { resultat = merge(resultat, res); }
		}
		return resultat;
	}
	
	private ArrayList<Integer> merge(ArrayList<Integer> a1, ArrayList<Integer> a2) {
		ArrayList<Integer> common = new ArrayList<Integer>(a1);
		common.retainAll(a2);
		return common;
	}
}
