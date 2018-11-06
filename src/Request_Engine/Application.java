package Request_Engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Application {
	public static void main(String[] args) {
		long debut = System.currentTimeMillis();
		
// Initialisaion des options
		String queryFolder = null, dataFolder = null, outputFolder = null;
		boolean output = false, verbose = false, export_results = false, export_stats = false;

// Lecture des arguments
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-queries")) {
				queryFolder = args[++i];
			} else if (args[i].equals("-data")) {
				dataFolder = args[++i];
			} else if (args[i].equals("-output")) {
				output = true;
				outputFolder = args[++i];
			} else if (args[i].equals("-verbose")) {
				verbose = true;
			} else if (args[i].equals("-export_results")) {
				export_results = true; 
			} else if (args[i].equals("-export_stats")) {
				export_stats = true; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! que fait l'export ? ("statistiques sur les req")
			}
		}
		if (queryFolder == null || dataFolder == null || outputFolder == null) {
			System.out.println("variable(s) non initialisée(s)");
			System.exit(-1);
		}
		
// Debut de l'application
		ArrayList<Query> queries = new ArrayList<Query>();
		Dictionary dico = new Dictionary();
		Index index = new Index();
		
		long debutIndex = System.currentTimeMillis();
		lireRepDico(dico, index, dataFolder);
		long finIndex = System.currentTimeMillis();
		
		long debutQuery = System.currentTimeMillis();
		lireRepQuery(queries, queryFolder);
		long finQuery = System.currentTimeMillis();
		
		long debutExec = System.currentTimeMillis();
		executeQueries(dico, index, queries, outputFolder, export_results);
		long finExec = System.currentTimeMillis();
		
		long fin = System.currentTimeMillis();
		
		
//		System.out.println(index.getFrequence().size());
//		System.out.println(dico.size());
		
// Affichage dans la terminal si l'option verbose etait presente
		if (verbose) {
			System.out.println("Temps de creation de l'index et du dictionnaire : " + Long.toString(finIndex - debutIndex) + " millisecondes");
			System.out.println("Temps de creation des requetes : " + Long.toString(finQuery - debutQuery) + " millisecondes");
			System.out.println("Temps d'execution de toutes les requetes (" + queries.size() + " requetes) : " + Long.toString(finExec - debutExec) + " millisecondes");
			System.out.println("Temps d'execution total : " + Long.toString(fin - debut) + " millisecondes");
		}
		
// Export des temps dexecution dans un fichier csv
		if (output) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFolder + "/verbose.csv"));
				bw.write("Creation index et dictionnaire, " + Long.toString(finIndex - debutIndex) + "ms");
				bw.write("\nCreation requetes, " + Long.toString(finQuery - debutQuery) + "ms");
				bw.write("\nExecution requetes, " + Long.toString(finExec - debutExec) + "ms");
				bw.write("\nTemps total, " + Long.toString(fin - debut) + "ms");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static void lireRepDico(Dictionary d, Index i, String folderName) {
		File rep = new File(folderName);
		if (rep.isDirectory()) {
			String fileList[] = rep.list();
			for (String f : fileList) {
				lireRepDico(d, i, folderName + "/" + f);
			}
		} else {
			createIndexAndDictionary(d, i, folderName);
		}
	}
	private static void createIndexAndDictionary(Dictionary d, Index i, String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null) { // Pour chaque ligne
				String[] tab = new String[3];
				tab = line.split("\\t"); // Split du triplet RDF en trois String
				if (tab[2].endsWith(" .")) { tab[2] = tab[2].substring(0, tab[2].length()-2); } // Suppression du point en fin de ligne
				d.add(tab[0]); // Ajout de la partie 1 dans le dictionnaire 
				d.add(tab[1]);
				d.add(tab[2]);
				i.add(d, tab[0], tab[1], tab[2]); // Ajout dans l'index
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void lireRepQuery(ArrayList<Query> list, String folderName) {
		File rep = new File(folderName);
		if (rep.isDirectory()) {
			String fileList[] = rep.list();
			for (String f : fileList) {
				lireRepQuery(list, folderName + "/" + f);
			}
		} else {
//			createQueryList(list, folderName);
			createBis(list, folderName);
		}
	}
	
	private static void createBis(ArrayList<Query> list, String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line, select = "";
			ArrayList<String> where = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if (line.startsWith("SELECT")) {
					select = line.substring(line.indexOf("SELECT")+6, line.indexOf("WHERE")).replaceAll(" ", "");;
					where = new ArrayList<String>();
				} else {
					String clause = line.replace("}", "").replace("\t", " ").trim();
					if (line.startsWith(" ")) {
						clause = clause.substring(1);
					}
					if (clause != null && clause.length() > 3) {
						where.add(clause);
					}
				}
				
				if (line.contains("}")) {
					list.add(new Query(select, where));
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void executeQueries(Dictionary dico, Index index, ArrayList<Query> queries, String outputFolder, boolean export_results) {
		if (export_results) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFolder + "/results.csv"));
				for (Query q : queries) {
					ArrayList<Integer> array = q.execute(dico, index);
					bw.write("\n\"" + q + "\", " + array); // Mise en forme de l'array en csv
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			for (Query q : queries) {
				q.execute(dico, index);
			}
		}
	}
}
