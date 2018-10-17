package Request_Engine;

public class Test {

	public static void main(String[] args) {
		System.out.println("** Contruction du dictionnaire **");
		//TODO : lire les fichiers pour ajout automatique
		Dictionary dico = new Dictionary();
		dico.add("Alice");
		dico.add("knows");
		dico.add("Bob");
		System.out.println("Dictionnaire : " + dico);
		System.out.println("TEST : ajout d'un doublon 'knows'");
		dico.add("knows");
		System.out.println("Dictionnaire : " + dico);
		
		System.out.println("\n\n ** Construction de l'index **");
		Index index = new Index();
		System.out.println("TEST : ajout de 'Bob knows Bob'");
		index.add(dico, "Bob", "knows", "Bob");
		System.out.println("TEST : ajout de 'Alice knows Bob'");
		index.add(dico, "Alice","knows","Bob");
		System.out.println("TEST : ajout de 'Bob knows Alice'");
		index.add(dico, "Bob","knows","Alice");
		System.out.println("Index : " + index);

	}
}
