package Request_Engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Application {
	public static void main(String[] args) {
		long debutAll, debut, fin, tpsAll, tpsDico, tpsQuery, tpsExec, tpsExportSta, tpsExportRes;
		debutAll = debut = fin = tpsAll = tpsDico = tpsQuery = tpsExec = tpsExportSta = tpsExportRes = -1;
		debutAll = System.currentTimeMillis();
		
// Initialisaion des options
		String queryFolder = null, dataFolder = null, outputFolder = null;
		boolean output = false, verbose = false, export_results = false, export_stats = false, workload_time = false;

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
				export_stats = true;
			} else if (args[i].equals("-workload_time")) {
				workload_time = true;
			}
		}
		if (queryFolder == null || dataFolder == null) {
			System.out.println("Queries folder and data folder are not optional");
			System.exit(-1);
		} else if (verbose || export_results || export_stats) {
			if (!output) {
				System.out.println("Output folder can't be empty");
				System.exit(-1);
			}
		}
		
// Debut de l'application
		ArrayList<Query> queries = new ArrayList<Query>();
		Dictionary dico = new Dictionary();
		Index index = new Index();
		
		debut = System.currentTimeMillis();
		lireRepDico(dico, index, dataFolder);
		fin = System.currentTimeMillis();
		tpsDico = fin - debut;
		if (verbose)
			System.out.println("Temps de creation de l'index et du dictionnaire : " + Long.toString(tpsDico) + " millisecondes");
		
		debut = System.currentTimeMillis();
		lireRepQuery(queries, queryFolder);
		fin = System.currentTimeMillis();
		tpsQuery = fin - debut;
		if (verbose)
			System.out.println("Temps de creation des requetes : " + Long.toString(tpsQuery) + " millisecondes");
		
		debut = System.currentTimeMillis();
		for (Query q : queries) {
			q.execute(dico, index);
		}
		fin = System.currentTimeMillis();
		tpsExec = fin - debut;
		if (workload_time)
			System.out.println("Temps d'execution de toutes les requetes (" + queries.size() + " requetes) : " + Long.toString(tpsExec) + " millisecondes");
		
		if (export_stats) {
			debut = System.currentTimeMillis();
			exportStats(queries, outputFolder);
			fin = System.currentTimeMillis();
			tpsExportSta = fin - debut;
			if (verbose)
				System.out.println("Temps d'export des statistiques : " + Long.toString(tpsExportSta) + " millisecondes");
		}
		
		if (export_results) {
			debut = System.currentTimeMillis();
			exportResults(dico, queries, outputFolder);
			fin = System.currentTimeMillis();
			tpsExportRes = fin - debut;
			if (verbose)
				System.out.println("Temps d'export des resultats : " + Long.toString(tpsExportRes) + " millisecondes");
		}
		
		fin = System.currentTimeMillis();
		tpsAll = fin - debutAll;
		if (verbose)
			System.out.println("Temps d'execution total : " + Long.toString(tpsAll) + " millisecondes");
		
// Export des temps dexecution dans un fichier csv
		if (output) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFolder + "/verbose.csv"));
				bw.write("Creation de l'index et du dictionnaire, " + Long.toString(tpsDico) + "ms");
				bw.write("\nCreation de la liste des requetes, " + Long.toString(tpsQuery) + "ms");
				bw.write("\nExecution de toutes les requetes, " + Long.toString(tpsExec) + "ms");
				if (export_stats) { bw.write("\nExport des statistiques, " + Long.toString(tpsExportSta) + "ms"); }
				if (export_results) { bw.write("\nExport des resultats, " + Long.toString(tpsExportRes) + "ms"); }
				bw.write("\nTemps total, " + Long.toString(tpsAll) + "ms");
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
			createQueryList(list, folderName);
		}
	}
	private static void createQueryList(ArrayList<Query> list, String fileName) {
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

	private static void exportStats(ArrayList<Query> list, String outputFolder) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFolder + "/stats.csv"));
			bw.write("Query,Nombre de resultat,Temps d'execution de la requete (ns),Methode utilise");
			for (Query q : list) {
				bw.write("\n\"" + q.toString() + "\"," + q.results.size() + "," + q.tps + "ns," + q.methode);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void exportResults(Dictionary dico, ArrayList<Query> list, String outputFolder) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFolder + "/results.csv"));
			bw.write("Query,Results");
			for (Query q : list) {
				String resultat = "\n\"" + q.toString() + "\"";
				for (Integer i : q.results) {
					resultat += "," + dico.getValueFromKey(i);
				}
				bw.write(resultat);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}