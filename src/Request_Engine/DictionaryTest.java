package Request_Engine;

public class DictionaryTest {

	public static void main(String[] args) {
		// Test de la classe Dictionary
		// Le dictionnaire stocke chaque ressource RDF en leur donnant un entier.
		// On attribue une map au dictionnaire pour cela
		
		//TEST
		System.out.println("Début du test !**");
		
		System.out.println("**Construction du TripletStore**");
		TripletStore store = new TripletStore();
		System.out.println("**Done**\n");
		
		System.out.println("**Création de triplets**");
		store.add("Bob", "knows", "Bob");
		store.add("Alice","knows","Bob");
		store.add("Bob","knows","Alice");
		System.out.println("**Done**\n");
		
		System.out.println("**Affichage du dictionnaire**");
		store.displayDico();
		System.out.println("\n");
		
		for(int i=0; i<store.size();i++)
		{
			System.out.println("**Affichage du triplet non compacté**");
			store.displayTriplets(i, true);
			
			System.out.println("**Affichage du triplet compacté**");
			store.displayTriplets(i, false);
			System.out.println("\n");
		}
		
		System.out.println("**Test terminé !");
	}

}
