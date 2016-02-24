/**
 * CS 342 MineSweeper Game
 * 
 * Writers: Josue Alvarez and Matthew Grider
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.border.TitledBorder;

import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import java.util.*;
import java.util.Timer;
import java.io.*;

public class MineSweeper extends JFrame implements ActionListener{
	   private MyJButton buttons[];
	   private int totalTime = 0;
	   JLabel onesTimer, tensTimer, hundredTimer;
	   private Boolean firstButtonPush = false;
	   private JLabel timerTest;
	   private Thread t;
	   private JLabel flagCounter;
	   private int countFlag = 10;
	   private JMenuItem ResetItem, TopTenItem, eXitItem, HelpItem, AboutItem, ClearItem;
	   private Board MyBoard;
	   
	   
	   /**
	    * Constructor
	    * All the container, panels, menu, menu items, and buttons are initialized here
	    * The container contains 2 panels
	    * 	The first panel is for the Dropdown menus of Game and Help and its sub options
	    * 	The second panel is for the Button array and the Flag Counter/Reset/Timer are on
	    */
	   public MineSweeper()
	   {
		   //Title of the Game
	      super( "Minesweeper" );

	      System.out.println("Entering Minesweeper");
	      firstButtonPush = false;
		  t = new Thread(new timerThread());
		 
		  
	      //Dropdown menu is created
	     JMenuBar dropDown = new JMenuBar();
	     dropDown.setLayout(new FlowLayout());
	     JMenu menu1 = new JMenu("Game");
	     menu1.setMnemonic(KeyEvent.VK_G);
	     JMenu menu2 = new JMenu("Help");
	     menu2.setMnemonic(KeyEvent.VK_H);
	     JMenu menu3 = new JMenu("Other");
	     menu3.setMnemonic(KeyEvent.VK_O);

	     /*Option to Reset/Clear the current Top Ten*/
	     ClearItem = new JMenuItem("Clear Top Ten");
	     ClearItem.addMouseListener(new MouseClickHandler());
	     ClearItem.setMnemonic(KeyEvent.VK_T);
	     ClearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
	     ClearItem.addActionListener(this);	     
	     
	     /*Option to Reset the current game being played*/
	     ResetItem = new JMenuItem("Reset");
	     ResetItem.addMouseListener(new MouseClickHandler());
	     ResetItem.setMnemonic(KeyEvent.VK_R);
	     ResetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
	     ResetItem.addActionListener(this);
	     /*Option to see the current Top Ten highest scores */
	     TopTenItem = new JMenuItem("Top Ten");
	     TopTenItem.addMouseListener(new MouseClickHandler());
	     TopTenItem.setMnemonic(KeyEvent.VK_T);
	     TopTenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
	     TopTenItem.addActionListener(this);
	     /*Option to close the program*/
	     eXitItem = new JMenuItem("eXit");
	     eXitItem.addMouseListener(new MouseClickHandler());
	     eXitItem.setMnemonic(KeyEvent.VK_E);
	     eXitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
	     eXitItem.addActionListener(this);
	     
	     /*Option to obtain Help*/
	     HelpItem = new JMenuItem("Help");
	     HelpItem.addMouseListener(new MouseClickHandler());
	     HelpItem.setMnemonic(KeyEvent.VK_H);
	     HelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
	     HelpItem.addActionListener(this);
	     
	     /*Option to learn more about the program*/
	     AboutItem = new JMenuItem("About");
	     AboutItem.addMouseListener(new MouseClickHandler());
	     AboutItem.setMnemonic(KeyEvent.VK_A);
	     AboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
	     AboutItem.addActionListener(this);

	     /*Add all these sub menus to their appropriate menus and adding those menus to the Dropdown panel*/
	     menu1.add(ResetItem);
	     menu1.add(TopTenItem);
	     menu1.add(eXitItem);
	     menu2.add(HelpItem);
	     menu2.add(AboutItem);
	     menu3.add(ClearItem);
	     dropDown.add(menu1);
	     dropDown.add(menu2);
	     dropDown.add(menu3);
     
	     /*Creating the panel containing the Flag counter, Smiley reset button, and Timer*/
	     JPanel gridAndSmilePanel = new JPanel();
	     gridAndSmilePanel.setLayout(new BoxLayout(gridAndSmilePanel, BoxLayout.Y_AXIS));
	     /*Reset button created*/
	      MyJButton smileyButton = new MyJButton(": )");
	      smileyButton.setNumber(100);
	      smileyButton.addMouseListener(new MouseClickHandler());
	 
	      /*Count up timer for how long user plays for created*/
	      timerTest = new JLabel("Your time: " + totalTime);
	      timerTest.setLayout(new FlowLayout());
	      /*Flag counter created*/
	      flagCounter = new JLabel("Number of flags left: " + countFlag);
	      flagCounter.setLayout(new FlowLayout());
	      
	      /*Panel where the Flag counter, Smeiley reset button and Timer go onto*/
	      JPanel smileyPanel = new JPanel();
	      smileyPanel.setLayout(new FlowLayout());
	      smileyPanel.add(flagCounter,BorderLayout.LINE_START);
	      smileyPanel.add(smileyButton, BorderLayout.EAST);
	      smileyPanel.add(timerTest,BorderLayout.LINE_END);
	      
	      /*Creating the panel for the grid of the buttons. 1D array, all of type MyJButtons*/
	      JPanel grid2 = new JPanel();
	      grid2.setLayout(new GridLayout(10, 10));
	      /*Creating the board of MyJButtons*/
	      MyBoard = new Board(10,10,10);
		  MyBoard.print_board();
		  /*Populating board*/
	      buttons = new MyJButton[100];
	      for(int count = 0; count < 100; count++)
	      {
	 	    		 buttons[ count ] = new MyJButton("");
			         buttons[count].setNumber ( count );
			         buttons[count].setPreferredSize(new Dimension(20,20));
			         buttons[count].addMouseListener(new MouseClickHandler());
			         buttons[count].pushed = 0;
			         grid2.add(buttons[count]);
	      }

	      /*Adding the two ecnompassing parts togeher onto the same container*/
	      Container container = getContentPane();
	      container.add(dropDown, BorderLayout.NORTH);
	      gridAndSmilePanel.add(smileyPanel, BorderLayout.SOUTH);
	      gridAndSmilePanel.add(grid2, BorderLayout.CENTER);
	      pack();
	      container.add(gridAndSmilePanel,BorderLayout.CENTER);
	      pack();
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      setLocationRelativeTo(null);
	      setSize( 400, 400 );
	      setVisible( true );

	   } // end constructor GridLayoutDemo


	   /*This class implements the functionality of the count up clock for the game
	    * by creating a Thread so the counter can run asynchronous with program*/
	   private class timerThread implements Runnable
	   {
		   public int counter;
		 public timerThread()
		 {
			 System.out.println("Thread was Entered, inside Constructor");
	    	  counter = totalTime;
		 }
		@Override
		public void run() {
			try{
				while(true)
				{
					counter++;
					totalTime = counter;	
					timerTest.setText("Your time: " + totalTime);
					Thread.sleep(1000);

				}
			}catch(Exception e)
			{
			}
		}
	   }//End of timerThread
	   
	   /**
	    * Give functionality of playing a new game by setting all flags and 
	    * conditions to original states.
	    */
		public void restartGame()
		{
			t.stop();
			totalTime = 0;
			firstButtonPush = false;
			timerTest.setText("Your time: " + totalTime);
			t = new Thread(new timerThread());
			MyBoard = new Board(10,10,10);
			countFlag = 10;
			for(int y = 0; y < 100; y++)
			{
				buttons[y].setEnabled(true);
		        buttons[y].pushed = 0;
		        buttons[y].flag = 0;
		        buttons[y].setText("");

			}
			return;
		}//End of restartGame
	   
	   
		/*
		 * 
		 */
	   private class MouseClickHandler extends MouseAdapter
	   {
		   
		public boolean runningForFirstTime()
		{
			for(int i = 0; i < 100; i++)
			{
				if(buttons[i].pushed == 1 )
				{
					return true;
				}
			}
			return false;
		}
		
		public int howManyButtonsPressed()
		{
			int totalNum = 0;
			for(int i = 0; i < 100; i++)
			{
				if(buttons[i].pushed == 1)
				{
					totalNum++;
				}
			}
			return totalNum;
		}
		

	    @SuppressWarnings("deprecation")
		public void mouseClicked (MouseEvent e)
	      {
	    	MyJButton currButton;
	    	int buttonPos;
		      String s = "";
		      String tmp = "";
		      System.out.println("Entering MouseEvent ");
		      
		      /**************IF MOUSE IS LEFT CLICKED*************/
		      if (SwingUtilities.isLeftMouseButton(e))
		      {
		    	  s = "Left Mouse Click";
		   /************CHECK IF LEFT MOUSE CLICK IS FOR ANY MENU OPTIONS**********/ 	

		    	  	if(e.getSource().equals(ResetItem))
		    	  	{
		    	  		 	System.out.println("IN REST OPTIOn");
		    	  		 	restartGame();
		    	  		 	return;
		    	  	}
				    else if(e.getSource().equals(HelpItem))
				    {
				    		System.out.println("IN HELP OPTION");
		    				  String[] blah = {"Left clicking on boxes uncovers the buttons. Right\n" +
		    				  			"clicking on boxes flags them, button won't be\n" +
		    				  			"able to be interacted with. Use numbers from uncovered\n"+
		    				  			"buttons to guide you to make best educated decision."};
		    				  JOptionPane.showMessageDialog(null, blah);
				    		return;
				    }
				    else if(e.getSource().equals(TopTenItem))
				    {
				    		System.out.println("IN TOPTEN OPTION");
							   String arr[] = MyBoard.get_top_ten();
							   String blah = "";
							   for(int j = 0; j < arr.length;j++)
							   {
								    blah = blah + "1." + arr[j];
							   }
				  				  JOptionPane.showMessageDialog(null, blah);
				    		return;
				    }
				    else if(e.getSource().equals(eXitItem))
				    {
				    		System.out.println("IN EXIT OPTION");
				    		MyBoard.close_program();
				    		System.exit(0);
				    		return;
				    }
				    else if(e.getSource().equals(AboutItem))
				    {
				    		System.out.println("IN ABOUT OPTION");
		    				String[] blah = {"This is a MineSweeper game developed by\n" + 
				    						"Josue Alvarez and Matthew Grider. The objective\n" +
		    								"is to left click on the grid of boxes. Attempt to uncover\n" +
				    						"all the boxes that don't have a mine. If a mine is uncovered\n"+
		    								"it is a game over!"};
		    				JOptionPane.showMessageDialog(null, blah);
				    		return;
				    }
				    else if(e.getSource().equals(ClearItem))
				    {
				    	MyBoard.clear_topten();
				    	return;
				    }
		   /************CHECK IF LEFT MOUSE CLICK IS FOR ANY BUTTON IN ARRAY OPTION**********/ 	

				  currButton = (MyJButton) e.getSource();
			      buttonPos = currButton.getNumber();

			      if(currButton.getNumber() == 100)
			      {
			    	  restartGame();
			    	  return;
			      }
		    /*******IS THE BUTTON CURRENTLY LOOKED AT A MINE****/	 
			      
			      if(MyBoard.at_button(currButton.getNumber()) == -1)
			      {
			    	  for(int i = 0; i < 100; i++)
			    	  {
			    		  if(MyBoard.at_button(buttons[i].getNumber()) == -1)
			    		  {
			    			  buttons[i].setEnabled(false);
			    			  buttons[i].setDisabledIcon(buttons[i].getIcon());
			    			  buttons[i].setText("BOMB");
			    		  }
			    		 // if(buttons[i].pushed != 1)
			    		  else
			    		  {
			    			  buttons[i].setEnabled(false);
			    			  buttons[i].setDisabledIcon(buttons[i].getIcon());
			    		  }
			    	  }

			    	  t.stop();
					  String[] blah = {"Sorry you've lost :("};
    				  JOptionPane.showMessageDialog(null, blah);
			    	  return;
			      }
		      
		    	  for(int i = 0; i < 100; i++)
		    	  {
		    		  if(currButton.num == buttons[i].num)
		    		  {
		    			  System.out.println("Entering left mouse button click");
	    				  currButton.pushed = 1;
	    				  /*If button is first to be pushed, set the timer*/
		    			  if((firstButtonPush != runningForFirstTime()) && ((currButton.flag != 1) || (currButton.flag != 2)))
		    			  {
		    				  System.out.println("Entering 'Creating Thread'");
//		    				  t = new Thread(new timerThread());
		    				  t.start();
		    				  firstButtonPush = true;
		    				  
		    			  }
		    			  /*If button has been flagged, cannot enter inside*/
		    			  if(currButton.flag == 1 || currButton.flag == 2)
		    			  {
		    				  String[] blah = {"Can't enter, Button is flagged"};
		    				  JOptionPane.showMessageDialog(null, blah);
		    				  return;
		    			  }
		    			  /*Press the button and perform the search for surrounding buttons*/
		    			  else
		    			  {
		    				  int[] tempArr = MyBoard.press_button(currButton.getNumber());
		    				  currButton.setEnabled(false);
		    				  currButton.setText(""+ MyBoard.at_button(currButton.getNumber()));
		    				  currButton.pushed = 1;

	    						  //Check for any more available moves. Are there any more buttons to be pressed.
	    				/*******IS THE BUTTON CURRENTLY LOOKED AT THE LAST BUTTON W/O A MINE****/	 
	    						  if(howManyButtonsPressed() == 90)
	    						  {
	    							  System.out.println("GAME IS OVER YOU'VE WON");
	    							  t.stop();
	    					    	  String winnerName = JOptionPane.showInputDialog("You Win! :) Please enter you name", null);
	    							  MyBoard.add_top_ten(winnerName,totalTime);
	    					    	  for(int f = 0; f < 100; f++)
	    					    	  {
	    					    		  if(MyBoard.at_button(buttons[f].getNumber()) == -1)
	    					    		  {
	    					    			  buttons[f].setEnabled(false);
	    					    			  buttons[f].setDisabledIcon(buttons[f].getIcon());
	    					    		  }
	    					    	  }
	    							  return;
	    						  }
	    					  System.out.println("Array length: " + tempArr.length + "getNumber() = "+ currButton.getNumber());
		    				  /*Disable all the appropriate buttons*/
	    					  for(int y = 0; y < tempArr.length; y++)
		    				  {
		    					  	  currButton = buttons[tempArr[y]]; 	
				    				  currButton.setEnabled(false);
				    				  currButton.setText(""+ MyBoard.at_button(currButton.getNumber()));
				    				  currButton.pushed = 1;
		    				  }
		    				  MyBoard.print_board();

		    			  }

		    		  }
		    	  }
		      }
		      /**************IF MOUSE IS RIGHT CLICKED*************/
		      else if (SwingUtilities.isRightMouseButton(e))
		      {
		    	  s = "Right Mouse Click";
				  currButton = (MyJButton) e.getSource();
			      buttonPos = currButton.getNumber();

		    	  //Button has no text on it
		    	  if(currButton.flag == 0)
		    	  {

		    		  if(countFlag != 0)
		    		  {
		    			  countFlag--;
		    			  flagCounter.setText("Number of flags left: " + countFlag);
		    			  currButton.flag = 1;
		    			  currButton.setText("M");
		    		  }
		    		//Button has a flag on it
		    	  }
		    	  else if(currButton.flag == 1)
		    	  {
		    		  currButton.flag = 2;
		    		  currButton.setText("?");
//		    		  currButton.setDisabledIcon(currButton.getIcon());
		    		  countFlag++;
		    		  flagCounter.setText("Number of flags left: " + countFlag);
		    	  }
		    	  //Button has a question mark on its
		    	  else
		    	  {
		    		  currButton.flag = 0;
		    		  currButton.setText("");  
		    	  }
		      }
	      }
	   }//End MouseClickHandler
		   
	 	   // handle button events by toggling between layouts
	   /**
	    * Handles hot key usage
	    */
		   public void actionPerformed( ActionEvent event )
		   {
			   //If the hot key is the HelpItem option
			   if(event.getSource() == HelpItem)
			   {
		    		System.out.println("IN HELP OPTION");
  				  String[] blah = {"Left clicking on boxes uncovers the buttons. Right\n" +
  				  			"clicking on boxes flags them, button won't be\n" +
  				  			"able to be interacted with. Use numbers from uncovered\n"+
  				  			"buttons to guide you to make best educated decision."};
  				  JOptionPane.showMessageDialog(null, blah);
		    		return;
			   }
			   //If the hot key is the ResetItem option
			   else if(event.getSource() == ResetItem)
			   {
   	  		 	restartGame();
   	  		 	return;
			   }
			   //If the hot key is the TopTenItem option
			   else if(event.getSource() == TopTenItem)
			   {
				   String arr[] = MyBoard.get_top_ten();
				   String blah = "";
				   for(int j = 0; j < arr.length;j++)
				   {
					    blah = blah + "1." + arr[j];
				   }
	  				  JOptionPane.showMessageDialog(null, blah);
				   return;
				   
			   }
			   //If the hot key is the eXitItem option
			   else if(event.getSource() == eXitItem)
			   {
		    		System.exit(0);
		    		return;
			   }
			   else if(event.getSource() == AboutItem)
			   {
   				String[] blah = {"This is a MineSweeper game developed by\n" + 
						"Josue Alvarez and Matthew Grider. The objective\n" +
						"is to left click on the grid of boxes. Attempt to uncover\n" +
						"all the boxes that don't have a mine. If a mine is uncovered\n"+
						"it is a game over!"};
   				JOptionPane.showMessageDialog(null, blah);
   				return;
			   }
			   //If the hot key is the ClearItem option
			   else if(event.getSource() == ClearItem)
			   {
			    	MyBoard.clear_topten();
			    	return;
			   }

		   }
	   
/**
 * 
 * Heart of the oeprations, where the GUI is made and stays in animation.
 */
	   public static void main( String args[] )
	   {
		   MineSweeper application = new MineSweeper();	
		   application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	   } 

/**
 * MyJButton is an extension of JButton with a couple add fields to make checking simpler.
 * Field pushed represents if a buttons was pressed or not. O for no and 1 for yes.
 * Field flag represents if a button contains a flag on it
 */
class MyJButton extends JButton
{
  private int num;
  private int flag = 0;
  private int pushed;
  
  public MyJButton (String text )
  {
    super (text);
    pushed = 0;
    //setText (text);
  }
  
  
  public MyJButton ( String text , int n)
  {
    super (text);
    num = n;
  }
   
  
  public void setNumber (int n)
  {
    num = n;
  }
  
  public int getNumber ()
  {
    return num;
  }
}



}