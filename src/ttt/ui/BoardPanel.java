/**
 * 
 */
package ttt.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import ttt.engine.State3;

/**
 * Klasse zur Darstellung einer Benutzeroberflaeche. Enthaelt wichtige Methoden und Attribute
 * zur Verwaltung von Schwierigkeitsgraden des Spiels, Aenderung der Groesse des Bretts, zum neu Anfangen
 * von Spielen usw.. 
 * @author Omar Sharaki
 *
 */
public class BoardPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2925834459512575034L;
	/**
	 * Der aktuelle Zustand des Spiels. Diese Variable bekommt ihre Werte aus der Methode isGameOver().
	 */
	private char gameState;
	/**
	 * Auswahlmenue zum Auswaehlen von Schwierigkeitsgraden bzw. Brettgroesse.
	 */
	private JMenu m2difficultyMenu, m1boardSizeMenu;
	/**
	 * Menueleiste wo die Auswahlmenues und Buttons zum Neustarten angezeigt werden.
	 */
	private JMenuBar menuBar;
	/**
	 * Elemente der Auswahlmenues. Stellen die 3 Schwierigkeitsgrade und die 2 BrettGroessen dar.
	 */
	private JRadioButtonMenuItem m1r1, m1r2, m2r1, m2r2, m2r3;
	/**
	 * Buttons zum Neustarten des Spiels.
	 */
	private JButton startButton, XreplayButton, OreplayButton;
	/**
	 * Gruppen zur Aufgliederung von Meunueelemente
	 */
	private ButtonGroup m1Group, m2Group;
	/**
	 * Wird gebraucht, um beim Starten des Spiels zum ersten Mal eine Nachricht anzuzeigen.
	 */
	private JLabel label;
	/**
	 * Textfeld, das im untersten Teil des Fensters angezeigt wird. Wird zur Ermittlung von Nachrichten
	 * bezueglich des Spiels gebraucht
	 */
	private JTextField msgBox;
	/**
	 * Untaetige Panels, die zur Positionierung der Elements auf dem ersten Bildschirm gebraucht werden.
	 */
	private JPanel p1, p2, p3, board;
	/**
	 * Diese Buttons stellen die Spielfelder dar.
	 */
	private JButton [] button;
	/**
	 * Aktuell ausgewaehlte Groesse des Spiels.
	 * @author Omar Sharaki
	 *
	 */
	private enum boardSize {NINE, SIXTEEN}
	/**
	 * Variable zum Umgang mit dem enum boardSize.
	 */
	private boardSize size;
	/**
	 * Aktuell ausgewaehlter Schwierigkeitsgrad.
	 * @author Omar Sharaki
	 *
	 */
	private enum difficulty {EASY, MEDIUM, UNBEATABLE}
	/**
	 * Variable zum Umgang mit dem enum difficulty.
	 */
	private difficulty emu;//easy, medium, unbeatable==>emu
	/**
	 * Das eingentliche Brett.
	 */
	private State3 state;
	/**
	 * Variable zur Abbildung der Ergebnisse der Methode winningPattern auf den entsprechenden
	 * Reihen, Spalten bzw. Diagonale in 3x3.
	 */
	private Map<String,int []> StringtoPattern3by3;
	/**
	 * Variable zur Abbildung der Ergebnisse der Methode winningPattern auf den entsprechenden
	 * Reihen, Spalten bzw. Diagonale in 4x4.
	 */
	private Map<String,int []> StringtoPattern4by4;
	
	/**
	 * Erstellt ein neues JPanel mit 2 Auswahlmenues und Einem Button zum Starten des Spiels.
	 */
	public BoardPanel(){
		setLayout(new BorderLayout());
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		board=new JPanel(new GridLayout(3,3));
		StringtoPattern3by3=new HashMap<String, int[]>();
		MappingSmallBoard();
		StringtoPattern4by4=new HashMap<String, int[]>();
		MappingBigBoard();
		
		//Game state is not game over at the beginning
		gameState='N';
		
		//Setting the default board size
		size=boardSize.NINE;
		
		//Iniatializing state
		state=new State3(3);
		
		//Setting the default difficulty
		emu=difficulty.EASY;
		
		//Setting the borders
		p3.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
		p1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
		p2.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
		
		//creating menus
		m2difficultyMenu=new JMenu("Difficulty");
		m1boardSizeMenu=new JMenu("Board size");
		
		//creating menu bar
		menuBar=new JMenuBar();
		
		//creating buttons, radio buttons, check boxes, TextFields 
		label=new JLabel("Choose your settings then hit play. Good luck!");
		label.setFont(new Font("Times New Roman Bold",Font.PLAIN,25));
		m1r1=new JRadioButtonMenuItem("3x3");
		m1r2=new JRadioButtonMenuItem("4x4");
		m2r1=new JRadioButtonMenuItem("Easy");
		m2r2=new JRadioButtonMenuItem("Medium");
		m2r3=new JRadioButtonMenuItem("Unbeatable");
		startButton=new JButton("Play!");
		XreplayButton=new JButton("I'll start");
		OreplayButton=new JButton("Let comp start");
		msgBox=new JTextField();
		
		//Setting up grid buttons(Cells of the board)
		button=new JButton[16];
		for(int i=0; i<16; i++){
			button[i]=new JButton(" ");
			button[i].setFont(new Font("Times New Roman Bold",Font.PLAIN,95));
			button[i].addActionListener(this);
		}
		
		//Adjusting colors and "pressability"
		startButton.setBackground(Color.RED);
		startButton.setForeground(Color.white);
		XreplayButton.setEnabled(false);
		OreplayButton.setEnabled(false);
		msgBox.setEditable(false);
		
		//setting borders
		label.setBorder(BorderFactory.createEmptyBorder(200,5,100,0));
		
		//creating button groups
		m1Group=new ButtonGroup();
		m2Group=new ButtonGroup();
		
		//Assigning buttons to groups
		m1Group.add(m1r1);
		m1Group.add(m1r2);
		m2Group.add(m2r1);
		m2Group.add(m2r2);
		m2Group.add(m2r3);
		
		//Adding menus to the menu bar
		menuBar.add(m2difficultyMenu);
		menuBar.add(m1boardSizeMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(XreplayButton);
		menuBar.add(OreplayButton);
		
		//Adding buttons to difficulty menu
		m2difficultyMenu.add(m2r1);
		m2difficultyMenu.add(m2r2);
		m2difficultyMenu.add(m2r3);
		
		//Adding buttons to size menu
		m1boardSizeMenu.add(m1r1);
		m1boardSizeMenu.add(m1r2);
		
		//Adding components to panels
		add(p1, BorderLayout.LINE_START);
		add(p2, BorderLayout.LINE_END);
		add(p3, BorderLayout.PAGE_END);
		add(label, BorderLayout.PAGE_START);
		add(startButton, BorderLayout.CENTER);
		
		addListeners();
		m1r1.setSelected(true);
		m2r1.setSelected(true);
		startButton.setFont(new Font("Arial", 1, 40));
		setPreferredSize(new Dimension(500,500));
	}
	
	/**
	 * Bildet die Ergebnisse der Methode winningPattern() auf den entsprechenden
	 * Reihen, Spalten bzw. Diagonale in einem 3x3 Brett ab.
	 */
	public void MappingSmallBoard(){
		StringtoPattern3by3.put("r0",new int[]{0,1,2});
		StringtoPattern3by3.put("r1",new int[]{3,4,5});
		StringtoPattern3by3.put("r2",new int[]{6,7,8});
		StringtoPattern3by3.put("c0",new int[]{0,3,6});
		StringtoPattern3by3.put("c1",new int[]{1,4,7});
		StringtoPattern3by3.put("c2",new int[]{2,5,8});
		StringtoPattern3by3.put("d",new int[]{0,4,8});
		StringtoPattern3by3.put("ad",new int[]{6,4,2});
	}
	
	/**
	 * Bildet die Ergebnisse der Methode winningPattern() auf den entsprechenden
	 * Reihen, Spalten bzw. Diagonale in einem 4x4 Brett ab.
	 */
	public void MappingBigBoard(){
		StringtoPattern4by4.put("r0",new int[]{0,1,2,3});
		StringtoPattern4by4.put("r1",new int[]{4,5,6,7});
		StringtoPattern4by4.put("r2",new int[]{8,9,10,11});
		StringtoPattern4by4.put("r3",new int[]{12,13,14,15});
		StringtoPattern4by4.put("c0",new int[]{0,4,8,12});
		StringtoPattern4by4.put("c1",new int[]{1,5,9,13});
		StringtoPattern4by4.put("c2",new int[]{2,6,10,14});
		StringtoPattern4by4.put("c3",new int[]{3,7,11,15});
		StringtoPattern4by4.put("d",new int[]{0,5,10,15});
		StringtoPattern4by4.put("ad",new int[]{12,9,6,3});
	}
	
	/**
	 * Fuegt Actionlisteners zu den Elements des Fensters hinzu.
	 */
	public void addListeners(){
		m1r1.addActionListener(this);
		m1r2.addActionListener(this);
		m2r1.addActionListener(this);
		m2r2.addActionListener(this);
		m2r3.addActionListener(this);
		startButton.addActionListener(this);
		XreplayButton.addActionListener(this);
		OreplayButton.addActionListener(this);
	}
	
	/**
	 * Die Menueleiste wird zur Nutzung in der Klasse BoardFrame gebraucht.
	 * @return Die Menuleiste.
	 */
	public JMenuBar getBar(){
		return menuBar;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		//Setting the game state(X won, O won, Draw, Not game over) 
		gameState=state.isGameOver2();
		
		//Setting actions for size menu buttons
		if(e.getSource()==m1r1){
			System.out.println("3x3 initiated");
			size=boardSize.NINE;
			for(int i=0; i<16; i++){
				button[i].setEnabled(false);
			}
			XreplayButton.setBackground(Color.GREEN);
			OreplayButton.setBackground(Color.GREEN);
		}
		else if(e.getSource()==m1r2){
			System.out.println("4x4 initiated");
			size=boardSize.SIXTEEN;
			for(int i=0; i<16; i++){
				button[i].setEnabled(false);
			}
			XreplayButton.setBackground(Color.GREEN);
			OreplayButton.setBackground(Color.GREEN);
		}
		
		//Setting actions for difficulty menu buttons
		else if(e.getSource()==m2r1){
			System.out.println("Easy level initiated");
			emu=difficulty.EASY;
			for(int i=0; i<16; i++){
				button[i].setEnabled(false);
			}
			XreplayButton.setBackground(Color.GREEN);
			OreplayButton.setBackground(Color.GREEN);
		}
		else if(e.getSource()==m2r2){
			System.out.println("Medium level initiated");
			emu=difficulty.MEDIUM;
			for(int i=0; i<16; i++){
				button[i].setEnabled(false);
			}
			XreplayButton.setBackground(Color.GREEN);
			OreplayButton.setBackground(Color.GREEN);
		}
		else if(e.getSource()==m2r3){
			System.out.println("Unbeatable level initiated");
			emu=difficulty.UNBEATABLE;
			for(int i=0; i<16; i++){
				button[i].setEnabled(false);
			}
			XreplayButton.setBackground(Color.GREEN);
			OreplayButton.setBackground(Color.GREEN);
		}
		
		//Setting actions for big play button
		else if(e.getSource()==startButton){
			this.removeAll();
			XreplayButton.setEnabled(true);
			OreplayButton.setEnabled(true);
			this.add(board, BorderLayout.CENTER);
			this.add(msgBox, BorderLayout.PAGE_END);
			
			if(size==boardSize.NINE){
				//board.setLayout(new GridLayout(3,3));
				for(int i=0; i<9; i++){
					board.add(button[i]);
					button[i].setEnabled(true);
				}
			}
			else if(size==boardSize.SIXTEEN){
				//this.setLayout(new GridLayout(3,3));
				((GridLayout)board.getLayout()).setRows(4);
				state=new State3(4);
				for(int i=0; i<16; i++){
					board.add(button[i]);
					button[i].setEnabled(true);
				}
			}
			XreplayButton.setBackground(null);
			OreplayButton.setBackground(null);
			state.setTurn('X');
		}				
		
		//Setting actions for Xreplay menu button
		else if(e.getSource()==XreplayButton){ //X(Player) will play first
			System.out.println("I've chosen to play first");
			msgBox.setText(null);
			
			if(size==boardSize.NINE){
				((GridLayout)board.getLayout()).setRows(3);
				state=new State3(3);
				for(int i=0; i<16; i++){
					board.add(button[i]);
				}
				for(int i=0; i<7; i++){
					board.remove(button[i+9]);
				}
			}
			else if(size==boardSize.SIXTEEN){
				((GridLayout)board.getLayout()).setRows(4);
				state=new State3(4);
				for(int i=0; i<16; i++){
					board.add(button[i]);
				}
			}
			for(int i=0; i<button.length; i++){
				button[i].setText(null);
				button[i].setEnabled(true);
				button[i].setBackground(null);
			}
			XreplayButton.setBackground(null);
			OreplayButton.setBackground(null);
			state.setTurn('X');
			gameState='N';
			this.validate();
			this.repaint();
		}			
		
		//Setting actions for Oreplay menu button
		else if(e.getSource()==OreplayButton){ //O(Computer) will play first
			msgBox.setText(null);
			
			System.out.println("Comp will start");
			if(size==boardSize.NINE){
				((GridLayout)board.getLayout()).setRows(3);
				state=new State3(3);
				for(int i=0; i<16; i++){
					board.add(button[i]);
				}
				for(int i=0; i<7; i++){
					board.remove(button[i+9]);
				}
			}
			else if(size==boardSize.SIXTEEN){
				((GridLayout)board.getLayout()).setRows(4);
				state=new State3(4);
				for(int i=0; i<16; i++){
					board.add(button[i]);
				}
			}
			for(int i=0; i<button.length; i++){
				button[i].setText(null);
				button[i].setEnabled(true);
				button[i].setBackground(null);
			}
			XreplayButton.setBackground(null);
			OreplayButton.setBackground(null);
			state.setTurn('O');
			gameState='N';
			this.validate();
			this.repaint();
		}		
		
		else if(state.getTurn()=='X'){ //Checking who plays 
			if(gameState=='N'){
				//Setting actions for game board buttons
				for(int i=0; i<(state.getSize()); i++){
					if(e.getSource()==button[i]){
						//X makes his move
						if(state.getPosContent(i)=='-'){
							state.setX(i);
							System.out.println("Chosen position: "+i);
							button[i].setText("X");
							System.out.println(button[i].getText());
							gameState=state.isGameOver2();
							System.out.println(gameState);
							this.validate();
							this.repaint();
						}
					}
				}
			}
		}
		//O makes his move
		if(state.getTurn()=='O'){
			if(gameState=='N'){
				if(emu==difficulty.EASY){	//If the difficulty is easy the computer chooses his moves randomly.
					int randPos=-1;
					int [] availableMoves = state.getAvailableMoves();
					if(availableMoves.length!=0){
						randPos = (int)(Math.random() * (availableMoves.length));
						state.setO(availableMoves[randPos]);
						button[availableMoves[randPos]].setText("O");
					}
				}
				//If the difficulty is medium: First check where the starting player has placed his first move.
				//If he played in the middle then play in a corner and if he played in a corner
				//then play in the middle. If he played anywhere else, then also 
				//play in a corner. After the first 2 moves the position is chosen randomly
				//unless there's a winning move for either player.
				else if(emu==difficulty.MEDIUM){
					//If this is the first move of the game and the board size is nine,
					//then play at a random corner.
					if(state.getCount('X')==0&&state.getCount('O')==0&&size==boardSize.NINE){ 
						int [] corners={0, 2, 6, 8};
						int randPos = (int)(Math.random() * (corners.length));
						state.setO(corners[randPos]);
						button[corners[randPos]].setText("O");						
					}
					//Else if X has already played and this is O's first move of the game 
					//and the board size is nine
					else if(state.getCount('X')==1&&state.getCount('O')==0&&size==boardSize.NINE){
							//If X had made his move at a corner, then play in the centre
							if((state.getLastAdded()==0)||(state.getLastAdded()==2)||
									(state.getLastAdded()==6)||(state.getLastAdded()==8)){
								state.setO(4);
								button[4].setText("O");
							}
							else { //else play at a random corner
								int [] corners={0, 2, 6, 8};
								int randPos = (int)(Math.random() * (corners.length));
								state.setO(corners[randPos]);
								button[corners[randPos]].setText("O");
							}
					}
					
					else { //After the first 2 rounds O just checks if he can win. If he can't he checks if
						   //X can win. If neither condition is true, he chooses his move randomly.
						int winner = state.canWin499();
						int blocker = state.canBlock499();
						if(winner!=-1){ //If O can win in this round, then he chooses the winning slot.
							state.setO(winner);
							button[winner].setText(String.valueOf(state.getPosContent(winner)));
						}
						else if(blocker!=-1){ //else if X can win in next round, then O blocks X's win.
							state.setO(blocker);
							button[blocker].setText("O");
						}
						else { //else just choose a random move.
							int randPos=-1;
							int [] availableMoves = state.getAvailableMoves();
							if(availableMoves.length!=0){
								randPos = (int)(Math.random() * (availableMoves.length));
								state.setO(availableMoves[randPos]);
								button[availableMoves[randPos]].setText("O");
							}
						}
					}
				}
				else if(emu==difficulty.UNBEATABLE){
					//If this is the first move of the game and the board size is nine,
					//O makes his move at a corner.
					if(state.getCount('X')==0&&state.getCount('O')==0&&size==boardSize.NINE){
						int [] corners={0, 2, 6, 8};
						int randPos = (int)(Math.random() * (corners.length));
						state.setO(corners[randPos]);
						button[corners[randPos]].setText("O");						
					}
					//else if board size is sixteen and X has less than 3 pieces on the board
					//(meaning he cannot yet win in the next move), then O makes his move randomly. 
					else if(size==boardSize.SIXTEEN&&state.getCount('X')<3){
						int j=0;				
						int randPos=-1;
						int [] availableMoves = state.getAvailableMoves();
						for(int x : availableMoves){
							availableMoves[j]=x;
							j++;
						}
						if(availableMoves.length!=0){
							randPos = (int)(Math.random() * (availableMoves.length));
							state.setO(availableMoves[randPos]);
							button[availableMoves[randPos]].setText("O");
						}
					}
					//Otherwise, use newminimax500 to determine your next move.
					else {
						int [] MMPos=state.newminimax500(Integer.MIN_VALUE, Integer.MAX_VALUE);
						System.out.println("Score: "+MMPos[0]+", Position: "+MMPos[1]);
						state.setO(MMPos[1]);
						button[MMPos[1]].setText("O");
					}
				}
			}		
		}		
		gameState=state.isGameOver2();
		System.out.println(gameState);
		
		if(gameState!='N'){
			
			if(size==boardSize.NINE){
				if(gameState=='X'||gameState=='O'){
					for(int x:StringtoPattern3by3.get(state.winningPattern())){
						if(gameState=='X'){
							button[x].setBackground(Color.GREEN);
							msgBox.setText("You won!");
						}
						else if(gameState=='O'){
							button[x].setBackground(Color.RED);
							msgBox.setText("You lost!");
						}
					}
				}
				else {
					msgBox.setText("Draw!");
					for(int i=0; i<button.length; i++){
						if(button[i].getText()=="X")
							button[i].setBackground(Color.MAGENTA);
						else 
							button[i].setBackground(Color.BLUE);
					}
				}
			}
			else if(size==boardSize.SIXTEEN){
				if(gameState=='X'||gameState=='O'){
					for(int x:StringtoPattern4by4.get(state.winningPattern())){
						if(gameState=='X'){
							button[x].setBackground(Color.GREEN);
							msgBox.setText("You won!");
						}
						else if(gameState=='O'){
							button[x].setBackground(Color.RED);
							msgBox.setText("You lost!");
						}
					}
				}
				else {
					msgBox.setText("Draw!");
					for(int i=0; i<button.length; i++){
						if(button[i].getText()=="X")
							button[i].setBackground(Color.MAGENTA);
						else 
							button[i].setBackground(Color.BLUE);
					}
				}
			}
		}
		this.validate();
		this.repaint();
	}
}
		


