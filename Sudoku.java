import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * The Sudoku class runs a Sudoku GUI application, where the user 
 * can select one of many Sudoku game configurations to play. 
 * 
 * @author Jason M ingrey1@gmail.com
 **/

public class Sudoku { 
	
	static int[][][] boards; // stores Sudoku board configuration starting values
	
	
	/**
	 * setRandomGame() Populates board with a random starting game configuration from boards.
	 * @param boards all available Sudoku game board starting configurations
	 * @param board the Sudoku board the user interacts with.
	 */
	
	public static void setRandomGame(int[][][] boards, ArrayList<ArrayList<JTextField>> board  ) { 
		
		int randomNum = (int)(Math.random() * boards.length); // used to select random starting values
		
		for (int x = 0; x < boards[0].length; x++) { 
			
			for (int y = 0; y < boards[0][0].length; y++) { // populate board for new game 
				
				if (boards[randomNum][x][y] != 0) { 
					board.get(x).get(y).setForeground(Color.BLUE);
					board.get(x).get(y).setText(String.format("%d", boards[randomNum][x][y]));
					board.get(x).get(y).setEditable(false);
					
				} 
			} 	
		}
		
	} 
	
	/**
	 * 
	 * @param board the Sudoku board user interacts with
	 **/
	public static void clearBoard(ArrayList<ArrayList<JTextField>> board) {
		for (int x = 0; x < board.size(); x++) { // begin loop A
			for (int y = 0; y < board.get(0).size(); y++) {
				
				board.get(x).get(y).setText("");
				board.get(x).get(y).setForeground(Color.BLACK);
				board.get(x).get(y).setEditable(true);
			}
		}
	}
	
	
	/**
	 * 
	 * @param grid is a Sudoku board. 
	 * @param subSquareSize the dimensions of grid
	 * @return true if the Sudoku game has been solved successfully, false otherwise.
	 */	
	
	static boolean checkSudokuSolution(ArrayList<ArrayList<JTextField>> grid, int subSquareSize) {
		
		final int size = grid.size();
		
		if (!checkValues(grid, 1, size)) return false; 
		
		for (int row = 0; row < size; row++) { 
			
			if (!checkRow(grid, row)) return false;	
		}
		for (int row = 0; row < size; row++) { 
			if (!checkColumn(grid, row)) return false;	
		}
		// go through subSquares in grid to make sure all squares have numbers
		for (int baseRow = 0; baseRow < size; baseRow += subSquareSize) { 
		   for (int baseCol = 0; baseCol < size; baseCol += subSquareSize) { 
		      if (!checkSquare(grid, baseRow, baseCol, subSquareSize)){ 
		    	  return false;
		      } 
		   } 
		 } 
				
		return true; 
	}

	/**
	 * 
	 * @param grid Sudoku game board
	 * @param baseRow rows in subSquare of grid
	 * @param baseCol columns in subSquare of grid
	 * @param subSquareSize subSquares' size
	 * @return true if every subSquare in board has numbers, false otherwise
	 */
	
	private static boolean checkSquare(ArrayList<ArrayList<JTextField>> grid, int baseRow, int baseCol, int subSquareSize) {
		
		boolean[] found = new boolean[subSquareSize * subSquareSize];
		
		for(int row = baseRow; row < subSquareSize; row++) {
			
			for (int col = baseCol; col < subSquareSize; col++) {
			
				 String index =  grid.get(row).get(col).getText();
				 int myIndex = Integer.parseInt(index) - 1; 
				 if (!found[myIndex]) found[myIndex] = true;
				 else return false;	
			}	
		}
		return true;
	}
	/**
	 * @param grid Sudoku board
	 * @param col columns in grid
	 * @return  true if col in grid is full of valid numbers, false otherwise. 
	 */

	private static boolean checkColumn(ArrayList<ArrayList<JTextField>> grid, int col) {
		
		final int size = grid.size();
		
		boolean[] found = new boolean[size]; 
		
		for (int row = 0; row < grid.size(); row++) { 
			
			String index = grid.get(row).get(col).getText(); 
			int myIndex = Integer.parseInt(index) - 1;
			if (!found[myIndex]) found[myIndex] = true; 
			else return false; 
			
		}
		
		return true; 
	}
	/**
	 * @param grid Sudoku board
	 * @param row index of row
	 * @return true if squares in row have valid numbers, otherwise false.
	 **/

	private static boolean checkRow(ArrayList<ArrayList<JTextField>> grid, int row) {
		final int size = grid.size();
		 boolean[] found = new boolean[size];
		 for (int col = 0; col < size; col++) { 
			 String index = grid.get(row).get(col).getText();
			 int myIndex = Integer.parseInt(index) - 1;
			 if (!found[myIndex]) {
				 found[myIndex] = true;
			 } else {
				 return false;
			 }
		 } 
		 return true; 
	}
	/**
	 * @param grid Sudoku board 
	 * @param min smallest valid number
	 * @param max largest valid number
	 * @return true if all values are in numerical range, false otherwise  
	 */
	private static boolean checkValues(ArrayList<ArrayList<JTextField>> grid, int min, int max) {
		
		for (ArrayList<JTextField> row : grid) { 
			
			for (int x = 0; x < row.size(); x++) { 
			
				String col = row.get(x).getText();
				try { 
					int colValue = Integer.parseInt(col);
				    if ((colValue < min) || (colValue > max)) return false; 				
				} catch (Exception ex) { 
					return false;
				}
			
			} 	
		} 
		return true; 
	}
	
	/**
	 * Main() runs the Sudoku application by populating the boards array and passing it to a new SudokuPanel.
	 * @throws IOException if there are problems reading the game configuration text file or one of the UI images
	 **/

	public static void main(String[] args) throws IOException {
		
		int counter = 0; // keeps track of where in the Sudoku starting values we are
		boards = new int[100][9][9]; // stores Sudoku starting board configurations. 
		
		try {
			
			InputStreamReader boardData = new InputStreamReader(Sudoku.class.getResourceAsStream("sudokuData.txt"));
			BufferedReader file = new BufferedReader(boardData);
			    
			for (int x = 0; x < boards.length; x++) {
				// each line of the text file is one complete Sudoku starter game.
				String[] game = file.readLine().split("");
				counter = 0;
				
				for (int y = 0; y < boards[0].length; y++) {
					
					for (int z = 0; z < boards[0][0].length; z++) {
						boards[x][y][z] = Integer.parseInt(game[counter]);	
						counter += 1;
					}					
				}			
			}
			
			file.close();
		
		} catch (IOException ex) {
			
			System.out.println("error" + ex.getMessage());
			
		}
		
		JFrame frame = new JFrame();
		JPanel panel = new SudokuPanel();
		panel.setBackground(Color.BLACK);
		frame.setSize(800, 800);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Sudoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
	}
	
		          
		
} 
		