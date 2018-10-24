package Request_Engine;

import java.util.HashMap;
import java.util.Map.Entry;

public class Dictionary {
	
	HashMap<Integer, String> dico;
	HashMap<String, Integer> dicoInverse;
	
	public Dictionary()	{
		dico = new HashMap<Integer, String>();
		dicoInverse = new HashMap<String, Integer>();
	}
	
	public void add(String s) {
		int key = createNewKey(s);
		if (key != -1) 
		{
			dico.put(key, s);
			dicoInverse.put(s, key);
		}
	}
	
	private int createNewKey(String s) {
		if (dicoInverse.containsKey(s)) {
			return -1;
		} else {
			return dico.size();
		}		
	}
	
	public int getKeyFromValue(String s) {
		if (dicoInverse.get(s) != null) {
			return dicoInverse.get(s);
		} else {
			return -1;
		}
	}
	
	public String getValueFromKey(int s) {
		return dico.get(s);
	}
	
	public int size() {
		return dico.size();
	}
	
	public String toString() {
		return dico.toString();
	}

}
