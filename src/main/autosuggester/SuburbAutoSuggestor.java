package main.autosuggester;
import java.util.*;

public class SuburbAutoSuggestor implements AutoSuggestor<String>{
    private List<String> suburbs;
                
    public SuburbAutoSuggestor(){
	suburbs = new ArrayList<String>(SUBURBS.length);
	for(String sub : SUBURBS)
	    suburbs.add(new String(sub));
    }

    @Override
    public List<String> getSuggestions(String query) {
	if(query.isEmpty())
	    return Collections.<String>emptyList();
	List<String> matches = new ArrayList<String>();
	for(String sub : suburbs){
	    if(sub.toLowerCase().startsWith(query.toLowerCase()))
		matches.add(sub);
	}
                        
	return matches;
    }
    
    public static String suburbList(){
	StringBuilder ans = new StringBuilder();
	for (int i=0; i<SUBURBS.length; i++){
	    ans.append(SUBURBS[i]).append(", ");
	    if (i%3==2) {ans.append("\n");}
	}
	return ans.toString();
    }
        
    //bunch of Wellington suburbs
    public final static String[] SUBURBS = new String[]{
	"Broadmeadows", "Churton Park", "Glenside",
	"Grenada", "Grenada North", "Horokiwi", "Johnsonville", "Khandallah", "Newlands",
	"Ohariu", "Paprangi", "Tawa", "Takapu Valley", "Woodridge", "Karori", "Northland",
	"Crofton Downs", "Kaiwharawhara", "Ngaio", "Ngauranga", "Makara", "Makara Beach",
	"Wadestown", "Wilton", "Brooklyn", "Aro Valley", "Kelburn", "Mount Victoria", 
	"Oriental Bay", "Te Aro", "Thorndon", "Highbury", "Pipitea", "Berhampore",
	"Island Bay", "Newtown", "Vogeltown", "Houghton Bay", "Kingston", "Mornington", 
	"Mount Cook", "Owhiro Bay", "Southgate", "Hataitai", "Lyall Bay", "Kilbirnie",
	"Miramar", "Seatoun", "Breaker Bay", "Karaka Bays", "Maupuia", "Melrose", "Moa Point",
	"Rongotai", "Roseneath", "Strathmore"};

}
