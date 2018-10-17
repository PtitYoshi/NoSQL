package Request_Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class TripletStore {

	ArrayList<RDFTriplets> store;
	HashMap<Integer, String> map;
	
	public TripletStore()
	{
		store = new ArrayList<RDFTriplets>();
		map = new HashMap<Integer, String>();
	}
	
	public void add(String s, String p, String o)
	{
		
		int ns = searchKey(s);
		int np = searchKey(p);
		int no = searchKey(o);
		
		store.add(new RDFTriplets(ns,np,no));
	}
	
	public int searchKey(String s) {
		// TODO Auto-generated method stub
		int res=map.size();

		for(int i=0;i<map.size();i++)
		{
			if(map.get(i)==s)
			{
				res=i;
				break;
			}
		}
		if(res==map.size())
		{
			map.put(map.size(), s);
		}
		
		return res;
	}

	public void displayDico() {
		// TODO Auto-generated method stub
		System.out.println(map.toString());
		
	}

	public ArrayList<RDFTriplets> getRDFs()
	{
		return store;
	}

	public int size() {
		// TODO Auto-generated method stub
		return store.size();
	}

	public void displayTriplets(int i, boolean compactMode) {
		// TODO Auto-generated method stub
		if(compactMode)
			store.get(i).display();
		else
		{
			displayExtractedRDF(store.get(i));
		}
	}

	private void displayExtractedRDF(RDFTriplets r) {
		// TODO Auto-generated method stub
		System.out.println("{"+map.get(r.s)+","+map.get(r.p)+","+map.get(r.o)+"}");
	}
}
