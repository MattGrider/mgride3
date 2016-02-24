/*
 * Name: Matt Grider
 * NetID: mgride3
 * 
 * Storage container for top ten
 */

public class TopTen 
{
	private String name;
	private int score;
	
	//Constructor for TopTen
	public TopTen(String input_name, int input_score)
	{
		name = input_name;
		score = input_score;
	}
	
	//gets name
	public String get_name()
	{
		return name;
	}
	
	//gets score
	public int get_score()
	{
		return score;
	}
	
	//sets name
	public void set_name(String new_name)
	{
		name = new_name;
	}
	
	//sets score
	public void set_score(int new_score)
	{
		score = new_score;
	}
	
	//compares two TopTen objects 
	//returns positive if this object is greater, zero if they're equal
	// and negative if the other is greater
	public int compare(TopTen other)
	{
		return (score - other.get_score());
	}
}
