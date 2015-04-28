package kognitver.algorithmus;

import java.util.ArrayList;

import snowballstemmer.GermanStemmer;

public class EditTags {
  ReturnTagList tags;

  public EditTags(ReturnTagList returnTagList) {
    this.tags = returnTagList;
  }

  public ReturnTagList getTags() {
    return tags;
  }
  
  /** Kölner Phonetik Algorithmus zur Erkennung von Zusammenhängen zwischen ähnlichen Wörtern und verbinden dieser zu einem.
   *  Die Gruppen entschprechen jeweils dem Code, der aus dem Buchstaben kreiert wird. 'group0' zum Beispiel verwandelt alle darin enthaltenen Buchstaben in eine 0 um.
   *
   * @author Steffen Schreiber
   */
  
  private static char[] group0 = new char[] { 'A', 'E', 'I', 'J', 'O', 'U', 'Y', 'Ä', 'Ö', 'Ü' };
  private static char[] group3 = new char[] { 'F', 'V', 'W' };
  private static char[] group4 = new char[] { 'G', 'K', 'Q', };
  private static char[] group6 = new char[] { 'M', 'N' };
  private static char[] group8 = new char[] { 'S', 'Z', 'ß' };
  private static char[] groupCFirst = new char[] { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' };
  private static char[] groupCNoFirst = new char[] { 'A', 'H', 'K', 'O', 'Q', 'U', 'X' };
  private static char[] groupCPrevious = new char[] { 'S', 'Z' };
  private static char[] groupDTPrevious = new char[] { 'C', 'S', 'Z' };
  private static char[] groupXFollow = new char[] { 'C', 'K', 'Q' };
  

  	String hallo = "hallo";
	
  	private static String Encoding(String hallo){
		  
  		char[] content = new char[hallo.length()];{
		for(int c=0; c<hallo.length();c++){
			content[c] = hallo.toUpperCase().charAt(c);
		}
	}
		  StringBuilder sb = new StringBuilder();
		  for (int i = 0; i < content.length; i++){
			  char entry = content[i];
			  if ((entry >= 'A' && entry <= 'Z')){
				  continue;  
			  }
			  
			  if (entry=='H'){
				  continue;
			  }
			  
			  if (Tag.contains(entry, group0)){
				  sb.append("0");
				  continue;
			  }
			  if (Tag.contains(entry, group3)){
				  sb.append("3");
				  continue;
			  }
			  if (Tag.contains(entry, group4)){
				  sb.append("4");
				  continue;
			  }	
			  if (Tag.contains(entry, group6)){
				  sb.append("6");
				  continue;
			  }
			  if (Tag.contains(entry, group8)){
				  sb.append("8");
				  continue;
			  }
			  if (entry=='B'){
				  sb.append("1");
				  continue;
			  }
			  if (entry=='L'){
				  sb.append("5");
				  continue;
			  }
			  if (entry=='R'){
				  sb.append("7");
				  continue;
			  }
			  if (entry=='P'){
				  if (i + 1 >= content.length){
					  sb.append("1");
					  continue;
				  }
				  char next = content[i + 1];
				  	if (next=='H'){
				  		sb.append("3");
				  		continue;
				  	}
				  sb.append("1");
				  continue;
			  }
			  if (entry=='X'){
				  if (i == 0){
					  sb.append("48");
					  continue;
				  }
				  char previous = content[i - 1];
			  		if (Tag.contains(previous, groupXFollow)){
			  			sb.append("8");
			  			continue;
			  		}
			  	  sb.append("48");
			      continue;
			  }
			  if (entry=='D' || entry=='T'){
				  if (i + 1 >= content.length){
					  sb.append("2");
					  continue;
				  }
				  char next = content[i + 1];
				  	if (Tag.contains(next, groupDTPrevious)){
				  		sb.append("8");
				  		continue;
				  	}
	
				  sb.append("2");
				  continue;
			  }
			  if (entry=='C'){
				  if (i == 0){
					  char next = content[i + 1];
					  	if (Tag.contains(next, groupCFirst)){
					  		sb.append("4");
					  		continue;
					  	}
					  sb.append("8");
					  continue;
				  }
			  else{
				  if (i + 1 >= content.length){
					  continue;
				  }
				  char next = content[i + 1];
				  char previous = content[i - 1];
				  	if (Tag.contains(previous, groupCPrevious)){
					  sb.append("8");
					  continue;
				  	}
				  else{
					if (Tag.contains(next, groupCNoFirst)){
						sb.append("4");
						continue;
					}
				  sb.append("8");
				  continue;
				  }
			  }
			  }
		  }
		  return sb.toString();
	  }

  	  //Hier werden alle doppelten Zahlen entfernt.
	  private static String CleaningDoubles(String hallo){
	   
		  StringBuilder sb = new StringBuilder();
		  char[] content = hallo.toCharArray();
		  char previous = 'B';
		  	for (int i = 0; i < content.length; i++){
		  		char entry = content[i];
		  			if (entry!=previous){
		  				sb.append(entry);
		  			}
		  		previous = entry;
		  	}
		  return sb.toString();
	  }
	  
	  //Hier werden aus dem Code alle Nullen ausßer der ersten entfernt. Nach diesem Durchgang ist der Kölner Phonetik abgeschlossen.
	  private static String CleaningZeroes(String hallo){
		  
		  StringBuilder sb = new StringBuilder();
		  char[] content = hallo.toCharArray();
	  		for (int i = 0; i < content.length; i++){
		  		char entry = content[i];
	  				if(i != 0){
	  					if(content[i] != 0){
	  						sb.append(entry);
	  					}
	  				}
	  		}
	  	  return sb.toString();
	  }


	/** Hier endet der Kölner Phonetik Algorithmus
	 */
	  
public void stem() {
    // create a new instance of PorterStemmer
    GermanStemmer stemmer = new GermanStemmer();

    for (int i = 0; i < tags.getsize(); i++) {
      // set the word to be stemmed
      stemmer.setCurrent(tags.getTagObject(i).gettag());


      // call stem() method. stem() method returns boolean value.
      if (stemmer.stem()) {
        tags.renameTag(tags.getTagObject(i).gettag(), stemmer.getCurrent());
        // If stemming is successful obtain the stem of the given word
      }
    }
  }
}
