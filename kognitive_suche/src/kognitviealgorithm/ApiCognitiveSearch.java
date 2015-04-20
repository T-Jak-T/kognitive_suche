package kognitviealgorithm;

import komplexeSuche.TagObjectList;

public class ApiCognitiveSearch {
	/**
	 * Algorithmus zur Erkennung von Schlüsselbegriffen
	 * Diese Klasse nimmt ein Array von Textblöcken, optional mit Adressen (URL oder Speicheradressen) entgegen
	 * und gibt eine sich aus diesen Textblöcken ergebende Auswahl an repräsentativen Schlüsselbegriffen (Tags) zurück.
	 * 
	 * Dies wird durch die Kombination von Häufigkeit und Nähe der Wörter zum Suchbegriff realisiert.
	 * 
	 * @param searchText - Ein Array von Textblöcken, welche durch eine kognitive Suche analysiert werden sollen
	 * @param searchWord - Der/die gesuchte/n Begriff/e
	 *
	 * @author Tobias Lenz
	 */

	public TagObjectList ApiCognitiveSearch (String[] searchText, String searchWord){
		
		WordCount count = new WordCount();
		count.addText(searchText, searchWord);
	  
		return null;
		
	}
	
}