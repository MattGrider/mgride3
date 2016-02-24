import java.io.*;
import java.util.*;

/*
 * Name: Matt Grider
 * NetID: mgride3
 * 
 * Main back end for the minesweeper game
 */

public class Board {

	private int[][] board;     //2d array for board
	private int numBomb;       //number of bombs in the board
	private boolean[][] flags; //2d array storing where flags are
	private int num_flags;     //number of flags on board, should always be <= to numBomb
	private TopTen[] top_ten;     //holds the top ten values
	
	//Constructor for board
	public Board(int x, int y, int bombNum)
	{
		board = new int[x][y];
		numBomb = bombNum;
		flags = new boolean[board.length][board[0].length];
		for(int i = 0; i < flags.length; i++)
		{
			Arrays.fill(flags[i], Boolean.FALSE);
		}
		num_flags = 0;
		top_ten = new TopTen[10];
		
		File file = new File("TopTen.txt");
		
		int i = 0;
		
		if(file.exists())
		{
			Scanner reader;
			try {
				reader = new Scanner(file);

			
			while(reader.hasNextLine() && i < 10)
			{
				String line = reader.nextLine();
				String[] line_parse = line.split(", ");
				
				top_ten[i] = new TopTen(line_parse[0], Integer.parseInt(line_parse[1]));
				
				i++;
			}
			
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			
			this.make_board(0);
			
		}
		
		for(; i < 10; i++)
		{
			top_ten[i] = null;
		}
		
		
	}
	
	
	//checks space directly above the enter block
	private boolean notup_checker(int x, int y, int checking)
	{
		if(y == 0)
		{
			return true;
		}
		
		if(board[x][y-1] == checking || flags[x][y-1])
		{
			return true;
		}
		
		return false;
	}
	
	//checks space directly below the enter block
		private boolean notdown_checker(int x, int y, int checking)
		{
			if(y == (board[0].length - 1))
			{
				return true;
			}
			
			if(board[x][y+1] == checking || flags[x][y+1])
			{
				return true;
			}
			
			return false;
		}
		
		//checks space directly left the enter block
		private boolean notleft_checker(int x, int y, int checking)
		{
			if(x == 0)
			{
				return true;
			}
			
			if(board[x-1][y] == checking || flags[x-1][y])
			{
				return true;
			}
			
			return false;
		}
		
		//checks space directly right the enter block
		private boolean notright_checker(int x, int y, int checking)
		{
			if(x == (board.length - 1))
			{
				return true;
			}
			
			if(board[x+1][y] == checking || flags[x+1][y])
			{
				return true;
			}
			
			return false;
		}
		
		//checks space directly up and left the enter block
		private boolean notup_left_checker(int x, int y, int checking)
		{
			if(x == 0 || y == 0)
			{
				return true;
			}
					
			if(board[x-1][y-1] == checking || flags[x-1][y-1])
			{
				return true;
			}
					
			return false;
		}
		
		//checks space directly up and right the entered block
		private boolean notup_right_checker(int x, int y, int checking)
		{
			if(x == (board.length - 1) || y == 0)
			{
				return true;
			}
			
			if(board[x+1][y-1] == checking || flags[x+1][y-1])
			{
				return true;
			}
			
			return false;
		}
		
		//checks space directly down and left the entered block
		private boolean notdown_left_checker(int x, int y, int checking)
		{
			if(x == 0 || y == (board[0].length - 1))
			{
				return true;
			}
			
			if(board[x-1][y+1] == checking || flags[x-1][y+1])
			{
				return true;
			}
					
			return false;
		}
		
		//checks space directly down and right the entered block
		private boolean notdown_right_checker(int x, int y, int checking)
		{
			if(x == (board.length - 1) || y == (board[0].length -1))
			{
				return true;
			}
			
			if(board[x+1][y+1] == checking || flags[x+1][y+1])
			{
				return true;
			}
			
			return false;
		}
	
		//checks space directly above the enter block
		private boolean up_checker(int x, int y, int checking)
		{
			if(y == 0)
			{
				return false;
			}
			
			if(board[x][y-1] == checking && !(flags[x][y-1]))
			{
				return true;
			}
			
			return false;
		}
		
		//checks space directly below the enter block
			private boolean down_checker(int x, int y, int checking)
			{
				if(y == (board[0].length - 1))
				{
					return false;
				}
				
				if(board[x][y+1] == checking && !(flags[x][y+1]))
				{
					return true;
				}
				
				return false;
			}
			
			//checks space directly left the enter block
			private boolean left_checker(int x, int y, int checking)
			{
				if(x == 0)
				{
					return false;
				}
				
				if(board[x-1][y] == checking && !(flags[x-1][y]))
				{
					return true;
				}
				
				return false;
			}
			
			//checks space directly right the enter block
			private boolean right_checker(int x, int y, int checking)
			{
				if(x == (board.length - 1))
				{
					return false;
				}
				
				if(board[x+1][y] == checking && !(flags[x+1][y]))
				{
					return true;
				}
				
				return false;
			}
			
			//checks space directly up and left the enter block
			private boolean up_left_checker(int x, int y, int checking)
			{
				if(x == 0 || y == 0)
				{
					return false;
				}
						
				if(board[x-1][y-1] == checking && !(flags[x-1][y-1]))
				{
					return true;
				}
						
				return false;
			}
			
			//checks space directly up and right the entered block
			private boolean up_right_checker(int x, int y, int checking)
			{
				if(x == (board.length - 1) || y == 0)
				{
					return false;
				}
				
				if(board[x+1][y-1] == checking && !(flags[x+1][y-1]))
				{
					return true;
				}
				
				return false;
			}
			
			//checks space directly down and left the entered block
			private boolean down_left_checker(int x, int y, int checking)
			{
				if(x == 0 || y == (board[0].length - 1))
				{
					return false;
				}
				
				if(board[x-1][y+1] == checking && !(flags[x-1][y+1]))
				{
					return true;
				}
						
				return false;
			}
			
			//checks space directly down and right the entered block
			private boolean down_right_checker(int x, int y, int checking)
			{
				if(x == (board.length - 1) || y == (board[0].length -1))
				{
					return false;
				}
				
				if(board[x+1][y+1] == checking && !(flags[x+1][y+1]))
				{
					return true;
				}
				
				return false;
			}
		
		
		
	//checks for the value that will be entered in for the space
	private int check_space(int i, int j)
	{
		int counter = 0;
		
		if(up_checker(i, j, -1))
		{
			counter++;
		}
		if(down_checker(i, j, -1))
		{	
			counter++;
		}
		if(left_checker(i, j, -1))
		{	
			counter++;
		}
		if(right_checker(i, j, -1))
		{
			counter++;
		}
		if(up_left_checker(i, j, -1))
		{
			counter++;
		}
		if(up_right_checker(i, j, -1))
		{
			counter++;
		}
		if(down_left_checker(i, j, -1))
		{
			counter++;
		}
		if(down_right_checker(i, j, -1))
		{
			counter++;
		}
		
		return counter;
	}
	
	
	//fills the board with values associated with where the bombs are placed
	public void make_board(int button)
	{
		Random rand = new Random();
		
		int x_button = (button / board[0].length);
		int y_button = (button - (x_button * board[0].length));
		
		num_flags = 0;
		
		for(int i = 0; i < flags.length; i++)
		{
			Arrays.fill(flags[i], Boolean.FALSE);
		}
		
		for(int i = 0; i < numBomb; i++)
		{
			int x = rand.nextInt((board.length - 1));
			int y = rand.nextInt((board[0].length - 1));
			if(board[x][y] != -1 && x != x_button && y != y_button)
			{
				board[x][y] = -1;
			}
			else
			{
				i--;
			}
		}
		
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				if(board[i][j] != -1)
				{
					board[i][j] = check_space(i, j);
				}
			}
		}
	}


	//rec depth first search
	private int rec_depth(int i, int j, boolean[][] visited)
	{
		int counter = 1;
		
		visited[i][j] = true;
		
		if(board[i][j] != 0)
		{
			return counter;
		}
		
		if(!(notup_checker(i, j, -1)) && visited[i][j-1] == false)
		{
			counter += rec_depth(i, j-1, visited);
		}
		if(!(notdown_checker(i, j, -1)) && visited[i][j+1] == false)
		{	
			counter += rec_depth(i, j+1, visited);
		}
		if(!(notleft_checker(i, j, -1)) && visited[i-1][j] == false)
		{	
			counter += rec_depth(i-1, j, visited);
		}
		if(!(notright_checker(i, j, -1)) && visited[i+1][j] == false)
		{
			counter += rec_depth(i+1, j, visited);
		}
		if(!(notup_left_checker(i, j, -1)) && visited[i-1][j-1] == false)
		{
			counter += rec_depth(i-1, j-1, visited);
		}
		if(!(notup_right_checker(i, j, -1)) && visited[i+1][j-1] == false)
		{
			counter += rec_depth(i+1, j-1, visited);
		}
		if(!(notdown_left_checker(i, j, -1)) && visited[i-1][j+1] == false)
		{
			counter += rec_depth(i-1, j+1, visited);
		}
		if(!(notdown_right_checker(i, j, -1)) && visited[i+1][j+1] == false)
		{
			counter += rec_depth(i+1, j+1, visited);
		}
		
		return counter;
	}
	
	
	//wrapper for depth first search alg returns an int array of buttons
	//it returns null if the first button was a bomb
	private int[] depth_first(int x, int y)
	{
		int[] buttons_pressed;
		boolean[][] visited = new boolean[board.length][board[0].length];
		for(int i = 0; i < visited.length; i++)
		{
			Arrays.fill(visited[i], Boolean.FALSE);
		}
		
		if(board[x][y] == -1)
		{
			return null;
		}
		
		int num = rec_depth(x, y, visited);
		
		buttons_pressed = new int[num];
		
		int counter = 0;
		int button_num;
		
		for(int i = 0; i < visited.length; i++)
		{
			for(int j = 0; j < visited[0].length; j++)
			{
				if(visited[i][j])
				{
					button_num = ((i * board[0].length) + j);
					buttons_pressed[counter] = button_num;
					counter++;
				}
			}
		}
		
		System.out.println("Testing Depth first search: ");
		for(int i = 0; i < buttons_pressed.length; i++)
		{
			System.out.println(" " + buttons_pressed[i]);
		}
		
		return buttons_pressed;
	}
	
	//enter a button location to be pressed and checked
	public int[] press_button(int button)
	{
		int x = (button / board[0].length);
		int y = (button - (x * board[0].length));
		
		System.out.println("x: " + x + "  y: " + y);
		
		return depth_first(x, y);
	}
	
	//given a button number it returns the value at that button
	public int at_button(int button)
	{
		int x = (button / board[0].length);
		int y = (button - (x * board[0].length));
		
		return (board[x][y]);
	}
	
	
	//places a flag at buttons point, returns success or failure
	public boolean place_flag(int button)
	{
		int x = (button / board[0].length);
		int y = (button - (x * board[0].length));
		
		if(num_flags < numBomb)
		{
			flags[x][y] = true;
			num_flags++;
			return true;
		}
		return false;
	}
	
	
	//removes flag at button
	//returns true if successful and flase if there was no flag at that button
	public boolean remove_flag(int button)
	{
		int x = (button / board[0].length);
		int y = (button - (x * board[0].length));
		
		if(flags[x][y] = true)
		{
			flags[x][y] = false;
			num_flags--;
			return true;
		}
		return false;
	}

	//opens and adds top ten score STILL NEEDS TO BE FIXED
	public void add_top_ten(String name, int score)
	{
		TopTen new_entry = new TopTen(name, score);
		
		int i = 0;
		boolean found = false;
		
		while(i < top_ten.length && top_ten[i] != null && !found)
		{
			if(new_entry.compare(top_ten[i]) >= 0)
			{
				found = true;
			}
			i++;
		}
		
		
		if(found)
		{
			i--;
		}
		else if(top_ten[i] == null)
		{
			top_ten[i] = new_entry;
			return;
		}
		
		
		TopTen temp = top_ten[i];
		top_ten[i] = new_entry;
		TopTen temp2;
		
		while(i < (top_ten.length - 1))
		{
			temp2 = top_ten[i+1];
			top_ten[i+1] = temp;
			temp = temp2;
			i++;
		}
	}
	
	
	//clears top ten
	public void clear_topten()
	{
		for(int i = 0; i < top_ten.length; i++)
		{
			top_ten[i] = null;
		}
	}
	
	//creates a .txt file to save the top ten scores
	public void close_program()
	{
		
		try
		{
		PrintStream fileStream = new PrintStream(new File("TopTen.txt"));
		
		for(int i = 0; i < top_ten.length; i++)
		{
			if(top_ten[i] != null)
			{
				fileStream.println(top_ten[i].get_name() + ", " + top_ten[i].get_score());
			}
		}
		
		fileStream.close();
		}
		catch(IOException e)
		{
			System.err.println("Problem writing to the file TopTen.txt");
		}
			
	}
	
	
	//returns an array of strings with top ten names and scores
	public String[] get_top_ten()
	{
		String[] names = new String[10];
		
		for(int i = 0; i < top_ten.length; i++)
		{
			if(top_ten[i] != null)
			{
				names[i] = top_ten[i].get_name() + ", " + top_ten[i].get_score();
			}
			else
			{
				names[i] = null;
			}
		}
		
		return names;
	}
	
	//gets number of bombs
	public int get_bombs()
	{
		return numBomb;
	}
	
	
	//gets number of flags
	public int get_flags()
	{
		return num_flags;
	}
	
	//prints board in terminal
	public void print_board()
	{
		System.out.println("    The board    ");
		System.out.println("----------------------------------------");
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				if(board[i][j] < 0)
				{
					System.out.print(" " + board[i][j]);
				}
				else
				{
					System.out.print("  " + board[i][j]);
				}
			}
			System.out.println("");
		}
		System.out.println("----------------------------------------");
	}

}