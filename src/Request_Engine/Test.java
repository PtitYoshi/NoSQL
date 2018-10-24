package Request_Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		long debut = System.currentTimeMillis();
//		tests();
		
		
		Dictionary dico = new Dictionary();
		Index index = new Index();
		lireFichier("500K.rdf", dico, index);
		System.out.println("Dictionnaire : " + dico.size());
		System.out.println("Index : " + index.size());
		
		
		Query q = new Query("tet");
		ArrayList<Integer> array = q.execute(dico, index);
		for (Integer n : array) {
			System.out.println(n + " : " + dico.getValueFromKey(n));
		}
		
//		
		long fin = System.currentTimeMillis();
		System.out.println("Total en " + Long.toString(fin - debut) + " millisecondes");
	}
	
	private static void tests() {
		System.out.println("!!!!!!!!!! TEST !!!!!!!!!!");
		System.out.println("** Contruction du dictionnaire **");
		//TODO : lire les fichiers pour ajout automatique
		Dictionary dicoTEST = new Dictionary();
		dicoTEST.add("Alice");
		dicoTEST.add("knows");
		dicoTEST.add("Bob");
		System.out.println("Dictionnaire : " + dicoTEST);
		System.out.println("TEST : ajout d'un doublon 'knows'");
		dicoTEST.add("knows");
		System.out.println("Dictionnaire : " + dicoTEST);
		
		System.out.println("\n\n ** Construction de l'index **");
		Index index = new Index();
		System.out.println("TEST : ajout de 'Bob knows Bob'");
		index.add(dicoTEST, "Bob", "knows", "Bob");
		System.out.println("TEST : ajout de 'Alice knows Bob'");
		index.add(dicoTEST, "Alice","knows","Bob");
		System.out.println("TEST : ajout de 'Bob knows Alice'");
		index.add(dicoTEST, "Bob","knows","Alice");
		System.out.println("Index : " + index);
	}
	
	private static void lireFichier(String nomFichier, Dictionary d, Index i) {
		long debut = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomFichier));
			String line;
			int n = 0;
			while ((line = br.readLine()) != null && n<10000) {
				String[] tab = new String[3];
				tab = line.split("\\t");
				if (tab[2].endsWith(" .")) {
					tab[2] = tab[2].substring(0, tab[2].length()-2);
				}
				d.add(tab[0]);
				d.add(tab[1]);
				d.add(tab[2]);
				i.add(d, tab[0], tab[1], tab[2]);
//				n++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long fin = System.currentTimeMillis();
		System.out.println("Creation dico + index en " + Long.toString(fin - debut) + " millisecondes");
	}
}
