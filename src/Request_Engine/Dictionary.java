package Request_Engine;

import java.util.HashMap;
import java.util.Map.Entry;

public class Dictionary {
	
	HashMap<Integer, String> dico;
	
	public Dictionary()	{
		dico = new HashMap<Integer, String>();
	}
	
	public void add(String s) {
		int key = createNewKey(s);
		if (key != -1) 
		{
			dico.put(key, s);
		}
	}
	
	private int createNewKey(String s) {
		if (dico.containsValue(s))
		{
			return -1;
		} else {
			return dico.size();
		}		
	}
	
	public int getKeyByValue(String s) {
		for (Entry<Integer, String> entry : dico.entrySet()){
			if (entry.getValue().equals(s)) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	public String toString() {
		return dico.toString();
	}

}
