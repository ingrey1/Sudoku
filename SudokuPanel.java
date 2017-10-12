import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

/**
 * The SudokuPanel class draws the Sudoku board, and user interface element. 
 * 
 * @author Jason M ingrey1@gmail.com
 **/


@SuppressWarnings("serial")
public class SudokuPanel extends JPanel { 
	
	
	private ArrayList<ArrayList<JTextField>> board = new ArrayList<ArrayList<JTextField>>();  
	private int boardSize = 9;
	private Font numberFont = new Font("SansSerif", Font.BOLD, 20 ); 
	private String statusText = ""; // text informing user of correct or incorrect solution
	private int score = 0; 
	
	/**
	 * Creates a SudokuPanel and sets up the game board, buttons, and mouse listeners
	 * 
	 * @throws IOException if there are problems loading the Sudoku board configuration data or image files
	 */
	
	public SudokuPanel() throws IOException { 
	
		setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][][][][grow][grow][grow][][][][][]", "[][][][][][][][][][]"));
		
	
		ImageIcon newGameIcon = new ImageIcon(getClass().getClassLoader().getResource("newGameIcon.jpg"));
		JButton newGameButton = new JButton("", newGameIcon); 
		add(newGameButton, "pos 87.5% 5%, wmax 75px,hmax 60px,aligny bottom"); 
    	newGameButton.addMouseListener(new MouseAdapter() {  
    		@Override
    		public void mouseClicked(MouseEvent e) { 
    			
    			statusText = ""; 
    			
    			Sudoku.clearBoard(board); 
    			
    			Sudoku.setRandomGame(Sudoku.boards, board);
    			repaint(); 
    		}
    	});
    	
    	
    	ImageIcon checkSolutionIcon = new ImageIcon(getClass().getClassLoader().getResource("checkSolutionImage.jpg"));
		JButton checkSolutionButton = new JButton("", checkSolutionIcon);
		add(checkSolutionButton, "pos 87.5% 15%, wmax 70px,hmax 45px,aligny bottom");
    	checkSolutionButton.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			
    			if (Sudoku.checkSudokuSolution(board, 3)) {
    				
    				statusText = "Congratulations! Your Solution is correct!";
    				score += 10; 
    				
    			} else {
    				
    				statusText = "Sorry, your solution is incorrect.";
    				
    			}
    			repaint();
    			
    		}
    	});
		
		
		// board is configured for the first time -- the JTextFields are created
		int height = 5;
		for (int row = 0; row < boardSize; row++) { 
			
				board.add(new ArrayList<JTextField>());  
				int initialWidth = 20; 
				String location =  ""; 
			
			for (int col = 0; col < boardSize; col++) { 
				
				board.get(row).add(new JTextField());
			    board.get(row).get(col).setColumns(10);
			    board.get(row).get(col).setFont(numberFont);
			    board.get(row).get(col).setHorizontalAlignment(JTextField.CENTER);
			    location = "pos " + (initialWidth + col * 6) + "% " + height + "%," + "growx," + "wmax 5%," + "hmin 5%";
			    add(board.get(row).get(col), location);
			} 
			
			height = height + 6;
		} 
		
		Sudoku.setRandomGame(Sudoku.boards, board);		
				
	}
	/**
	 * paintComponent() draws Sudoku board, and game status elements
	 * 
	 * @param g graphics object used to draw the Sudoku board on panel canvas 
	 **/
	
	public void paintComponent(Graphics g) { 
		
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();
		g.setFont(numberFont);
		g.setColor(Color.CYAN);
		g.drawString(statusText, (width / 3), (height - height / 3));
		g.drawString(String.format("Score: %d ", score), (width - width / 8), (int)(height - height / 1.5));	
	}
}
