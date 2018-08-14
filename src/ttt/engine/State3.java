/**
 * 
 */
package ttt.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Diese klasse enthaelt fast alle Methoden und Attribute, die zur Ausfruehrung eines lauffaehigen Spiels 
 * noetig sind. 
 * @author Omar Sharaki
 *
 */
public class State3 {
	/**
	 * Der aktuelle Zustand oder die Aussicht des Bretts, dargestellt als ein char Feld.
	 */
	private char [] state;	//Actual content of the board
	/**
	 * Ermittelt wer jetzt dran ist, 'X' oder 'O'.
	 */
	private char turn;	//Whose turn it is
	/**
	 * Steht fuer "1 dimensional to 2 dimensional". 
	 * Bildet alle eindimensionalen Zellennummer des Bretts auf zweidimensionalen Zellennummern. Es
	 * wird benutzt um den Umgang mit DDState zu erleichtern, z.B. die Zellennummer 0 bildet auf [0][0] 
	 * ab, 1 auf [0][1] ab usw..
	 * 
	 */
	private Map<Integer, int []> DtoDD;
	/**
	 * Steht fuer "2 dimensional state".
	 * Dieses Attribut stellt das char Feld state als ein zweidimensionales Feld. Wird zur 
	 * Erleichterung der Ueberpruefung des Brettzustands benutzt.
	 */
	private char [][] DDState;
	/**
	 * Die Groesse des Bretts. z.B. n=3 bedeutet ein Spiel auf einem Brett die neuen Felder Gross ist.
	 */
	private int n;	//Size of the board
	/**
	 * Wird zur Messung der Leistung von Methoden benutzt, indem es bei jedem durchlauf einer Schleife 
	 * incrementiert wird.
	 */
	private int stateID;
	/**
	 * Zaehlen ab wie viel X bzw. O Zeichen sich auf dem Spielbrett befinden.
	 */
	private int countX, countO;
	/**
	 * Enthaelt die Position des zuletzt besetzten Feldes. 
	 */
	private int lastAdded;
	
	/**
	 * Erstellt ein leeres Brett der Groesse n*n.
	 * @param n Groesse des Bretts.
	 */
	public State3(int n){
		int a=0;
		this.n=n;
		state=new char[n*n];
		DtoDD=new HashMap<Integer, int []>();
		countX=0;
		countO=0;
		
		for(int i = 0; i<state.length; i++){
			state[i]='-';
		}
		//Abbildung der Zellennummern
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				DtoDD.put(a, new int[]{i, j});
				a++;
			}
		}
		
		a=0;
		//Initialisiere DDState mit den Werten von state
		DDState=new char[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				DDState[i][j]=state[a];
				a++;
			}
		}
	}
	

	/**
	 * Gibt die Position des zuletzt besetzten Feldes zurueck.
	 * @return lastAdded
	 */
	public int getLastAdded(){
		return lastAdded;
	}
	
	/**
	 * Gibt entweder countX oder countO zurueck, abhaengig vom Parameter ab.
	 * @param s Gibt an ob countX oder countO zurueckgegeben werden soll.
	 * @return countX oder countO
	 */
	public int getCount(char s){
		return (s=='X') ? countX : countO;
	}
	
	/**
	 * Gibt die anzahl der Felder auf dem Brett zurueck.
	 * @return n*n
	 */
	public int getSize(){
		return n*n;
	}
	
	/**
	 * 
	 * @param i Die Zellennummer des Feldes, dessen Inhalt zurueckgegeben soll.
	 * @return state[i]
	 */
	public char getPosContent(int i){
		return state[i];
	}
	
	/**
	 * Gibt an wer jetzt dran ist.
	 * @return turn
	 */
	public char getTurn() {
		// TODO Auto-generated method stub
		return turn;
	}
	
	/**
	 * Gibt ein int Feld zurueck, das die Zellennummern aller noch nicht besetzten Felder zurueck enthaelt.
	 * @return int Feld mit Zellennummern der noch leeren Felder.
	 */
	public int[] getAvailableMoves(){
		int count=0;
		int i=0;
		for(int j=0; j<state.length; j++){
			if(state[j]=='-')
				count++;
		}
		int [] availableSlots = new int[count];
		for(int j=0; j<state.length; j++){
			if(state[j]=='-')
				availableSlots[i++]=j;		
		}
		return availableSlots;
	}
	
	/**
	 * Prueft ob, das Spiel zu Ende ist. Gibt entweder 'X', 'O', 'D' oder 'N' zurueck.
	 * 'X' deutet auf einen Spielersieg hin. 'O' auf einen Computersieg. 'D' auf ein unentschieden und
	 * 'N' deutet darauf hin, dass das Spiel noch nicht zu Ende ist.
	 * @return 'X', 'O', 'D' oder 'N'
	 */
	public char isGameOver2(){
		char turnOpp;
		int count;
		if(turn=='X'){
			count=countO;
			turnOpp='O';
		}
		else {
			count=countX;
			turnOpp='X';
		}
		if(count>=n){//Nobody can win if they have less than n pieces(X,O) on the board.
			
			//Check column for win
			for(int i=0; i<n; i++){
				if(DDState[i][DtoDD.get(lastAdded)[1]]!=turnOpp)
					break;
				if(i==(n-1)){
					return turnOpp;
				}
			}
			
			//Check row for win
			for(int i=0; i<n; i++){
				if(DDState[DtoDD.get(lastAdded)[0]][i]!=turnOpp)
					break;
				if(i==(n-1)){
					return turnOpp;
				}
			}
			
			//Check diagonal for win
			if(DtoDD.get(lastAdded)[0] == DtoDD.get(lastAdded)[1]){
	    		
				//we're on a diagonal
	    		for(int i = 0; i < n; i++){
	    			if(DDState[i][i] != turnOpp)
	    				break;
	    			if(i == n-1){
						return turnOpp;
	    			}
	    		}
	    	}
			
		    //check anti diagonal 
			for(int i = 0; i<n; i++){
	    		if(DDState[i][(n-1)-i] != turnOpp)
	    			break;
	    		if(i == n-1){
					return turnOpp;
	    		}
	    	}
	    	
	    	//check for draw
	    	if((countX+countO)==(n*n))
	    		return 'D';			
		}
		return 'N';
	}
	
	/**
	 * Gibt eine Zeichenkette zurueck, die zeigt an in welcher Spalte bzw. Reihe usw.
	 * sich das Gewinnmuster befindet. Zum Beispiel, wenn die Felder 0, 1 und 2(die erste Reihe) durch ein
	 * X besetzt sind, dann wird "r0"(steht fuer row 1) zurueckgegeben. Es gibt 8 moegliche Rueckgabewerte:
	 * r0-2 bzw. c0-2 stehen fuer row 1-3 bzw. column 1-3. "d" steht fuer diagonale und "ad" steht fuer
	 * anti-diagonal(die Gegendiagonale).  
	 * @return "r0", "r1", "r2", "c0", "c1", "c2", "d" oder "ad"
	 */
	public String winningPattern(){
		char turnOpp;
		if(turn=='X'){
			turnOpp='O';
		}
		else {
			turnOpp='X';
		}
			//Check column for win
			for(int i=0; i<n; i++){
				if(DDState[i][DtoDD.get(lastAdded)[1]]!=turnOpp)
					break;
				if(i==(n-1)){
					return "c"+DtoDD.get(lastAdded)[1];
				}
			}
			
			//Check row for win
			for(int i=0; i<n; i++){
				if(DDState[DtoDD.get(lastAdded)[0]][i]!=turnOpp)
					break;
				if(i==(n-1)){
					return "r"+DtoDD.get(lastAdded)[0];
				}
			}
			
			//Check diagonal for win
			if(DtoDD.get(lastAdded)[0] == DtoDD.get(lastAdded)[1]){
	    		
				//we're on a diagonal
	    		for(int i = 0; i < n; i++){
	    			if(DDState[i][i] != turnOpp)
	    				break;
	    			if(i == n-1){
						return "d";
	    			}
	    		}
	    	}
			
		    //check anti diagonal 
	    	
			for(int i = 0; i<n; i++){
	    		if(DDState[i][(n-1)-i] != turnOpp)
	    			break;
	    		if(i == n-1){
					return "ad";
	    		}
	    	}
	    	
	    	//If we reach this far then it's a draw
	    	return "draw";			
	}
	
	/**
	 * Zeigt das aktuelle Brett auf der konsole an
	 */
	public void boardShow(){
		if(n==3){
			System.out.println(stateID);
			for(int i=0; i<=6;i+=3)
				System.out.println("["+state[i]+"]"+" ["+state[i+1]+"]"+" ["+state[i+2]+"]");
			System.out.println("***********");
		}
		else {
			System.out.println(stateID);
			for(int i=0; i<=12;i+=4)
				System.out.println("["+state[i]+"]"+" ["+state[i+1]+"]"+" ["+state[i+2]+"]"+" ["+state[i+3]+"]");
			System.out.println("***********");
		}	
	}
	
	/**
	 * Eine heuristische Auswertungsmethode die einer von 3 Werte zurueck gibt je nachdem wie das Brett
	 * aussieht am Ende des Spiels. +10 bei einem O-Gewinn, -10 bei einem X-Gewinn und 0 bei einem
	 * Unentschieden.
	 * @return +10, -10, 0
	 */
	public int score(){
		if(isGameOver2()=='X')
			return -10;
		else if(isGameOver2()=='O')
			return +10;
		else 
			return 0;
	}
	
	/**
	 * Legt fest wer(Spieler(X) oder Computer(O)) jetzt dran ist. 
	 * @param t Kann entweder X bzw. O sein je nachdem, ob X bzw. O jetzt spielen soll.
	 */
	public void setTurn(char t){
		turn=t;
	}
	
	/**
	 * Setzt ein X an einer bestimmten Stelle ein. Dabei aendern sich einige Attribute 
	 * der Klasse State3. Die wichtigsten unter diesen Aenderungen sind, dass 
	 * die turn Variable auf 'O' gesetzt wird, die Variable countX um 1 erhoeht wird, und
	 * die Variable lastAdded entsprechend dem Wert des Parameters i geaendert wird. 
	 * @param i Eine Position auf dem Brett
	 */
	public void setX(int i){
		state[i]='X';
		DDState[DtoDD.get(i)[0]][DtoDD.get(i)[1]]='X';
		turn='O';
		countX++;
		lastAdded=i;
	}
	
	
	/**
	 * 
	 * Setzt ein O an einer bestimmten Stelle ein. Dabei aendern sich einige Attribute 
	 * der Klasse State3. Die wichtigsten unter diesen Aenderungen sind, dass 
	 * die turn Variable auf 'X' gesetzt wird, die Variable countO um 1 erhoeht wird, und
	 * die Variable lastAdded entsprechend dem Wert des Parameters i geaendert wird. 
	 * 
	 * @param i Eine Position auf dem Brett
	 */
	public void setO(int i){
		state[i]='O';
		DDState[DtoDD.get(i)[0]][DtoDD.get(i)[1]]='O';
		turn='X';
		countO++;
		lastAdded=i;
	}
	
	/**
	 * Setzt die Aenderungen die durch die Methoden setX bzw. setO gemacht wurden zurueck.
	 * @param i Position des zurueckzusetzenden Zuges
	 */
	public void revert(int i){
		state[i]='-';
		DDState[DtoDD.get(i)[0]][DtoDD.get(i)[1]]='-';
		lastAdded=i;
		if(turn=='X'){
			turn = 'O';
			countO--;
		}
		else {
			turn = 'X';
			countX--;
		}
	}
	
	/**
	 * Prueft, ob ein Spieler durch seinen naechsten Zug gewinnen kann und wenn ja liefert die Zellennummer
	 * der zum Gewinn fuehrenden Position. Ansonsten wird eine -1 zurueckgeliefert. 
	 * @return -1 wenn kein Gewinn beim naechsten Zug erzielt werden kann. Ansonsten die Zellennummer
	 * der gewinnenden Position. 
	 */
	public int canWin499(){
		int pot=-1;
		int count;
		if(turn=='O')
			count=countO;
		else
			count=countX;
		if(count>=(n-1)){ //No win available if each player has less than n-1 seeds on the board
			
			//Checking begins for X and O
			for(int x:getAvailableMoves()){
					DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]=turn;
					
					//Check column for win
					for(int i=0; i<n; i++){
						if(DDState[i][DtoDD.get(x)[1]]!=turn)
							break;
						if(i==(n-1)){
							DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
							return pot=x;
						}
					}
					
					//Check row for win
					for(int i=0; i<n; i++){
						if(DDState[DtoDD.get(x)[0]][i]!=turn)
							break;
						if(i==(n-1)){
							DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
							return pot=x;
						}
					}
					
					//Check diagonal for win
					if(DtoDD.get(x)[0] == DtoDD.get(x)[1]){
			    		
						//we're on a diagonal
			    		for(int i = 0; i < n; i++){
			    			if(DDState[i][i] != turn)
			    				break;
			    			if(i == n-1){
			    				DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
								return pot=x;
			    			}
			    		}
			    	}
					
				    //check anti diagonal 
			    	for(int i = 0; i<n; i++){
			    		if(DDState[i][(n-1)-i] != turn)
			    			break;
			    		if(i == n-1){
			    			DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
							return pot=x;
			    		}
			    	}
			    	DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
				}
		}
		return pot;
	}
	
	/**
	 * Prueft, ob der Gegner in seinem kommenden Zug einen Gewinn fuer sich erzielen kann
	 * und wenn dies stimmt, dann wird die Zellennummer der zum Gegnergewinn fuehrenden Position
	 * zurueckgegeben, damit sie von dem aktuellen Spieler blockiert werden kann. Ansonsten wird 
	 * eine -1 zurueckgegeben. 
	 * @return -1 wenn ein Gegnergewinn im naechsten Zug des Gegners nicht moeglich ist. Ansonsten die Zellennummer
	 * der zum Gegnergewinn fuehrenden Position.
	 */
	public int canBlock499(){
		int pot=-1;
		char turnOpp='X';
		if(countX>=n-1){ //No win available if each player has less than n-1 seeds on the board 
			
			//Checking begins for X and O
			for(int x:getAvailableMoves()){
					DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]=turnOpp;
					
					//Check column for win
					for(int i=0; i<n; i++){
						if(DDState[i][DtoDD.get(x)[1]]!=turnOpp)
							break;
						if(i==(n-1)){
							DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
							return pot=x;
						}
					}
					
					//Check row for win
					for(int i=0; i<n; i++){
						if(DDState[DtoDD.get(x)[0]][i]!=turnOpp)
							break;
						if(i==(n-1)){
							DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
							return pot=x;
						}
					}
					
					//Check diagonal for win
					if(DtoDD.get(x)[0] == DtoDD.get(x)[1]){
			    		
						//we're on a diagonal
			    		for(int i = 0; i < n; i++){
			    			if(DDState[i][i] != turnOpp)
			    				break;
			    			if(i == n-1){
			    				DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
								return pot=x;
			    			}
			    		}
			    		//DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
			    	}
					
				    //check anti diagonal 
			    	for(int i = 0; i<n; i++){
			    		if(DDState[i][(n-1)-i] != turnOpp)
			    			break;
			    		if(i == n-1){
			    			DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
							return pot=x;
			    		}
			    	}
			    	DDState[DtoDD.get(x)[0]][DtoDD.get(x)[1]]='-';
				}
		}
		return pot;
	}
	
	/**
	 * Diese rekursive Methode simuliert das Gehirn eines perfekten Spielers. Ein perfekter Spieler kann nicht 
	 * vierlieren. Er wird entweder gewinnen oder ein Unentschieden erzwingen. Der Algorithmus, der dahinter
	 * steckt ist Minimax mit Alpha-Beta pruning. Das grundlegende Prinzip dieses Algorithmus ist es den
	 * groesstmoeglichen Verlust eines Spielers zu verringern. Beim Aufruf der Methode fuer das 
	 * aktuelle Brett wird zuerst geprueft, ob das Spiel schon zu Ende ist. Wenn dies nicht stimmt,
	 * dann wird ein int Feld mit der Zellennummern aller leeren Felder erstellt. Danach wird geprueft
	 * welcher Spieler jetzt dran ist und dementsprechend wird setX bzw. setO mit dem ersten
	 * Element in der Liste der leeren Felder als Parameter aufgerufen. Um den schnellsten Gewinnpfad 
	 * aber zu waehlen, wird davor mittels der Methode canWinn499 geprueft, ob ein Gewinn sofort in dieser
	 * Runde erzielbar ist. Wenn dies stimmt, dann wird der Rueckgabewert von canWinn499 als Parameter
	 * fuer setX bzw. setO benutzt und eine von den Beiden wird aufgerufen. Wenn dies aber nicht der
	 * Fall ist, dann wird entweder setX oder setO wie zuvor beschrieben aufgerufen. Danach wird
	 * auf jeden Fall newminimax500 rekursiv aufgerufen mit den Parametern Alpha und Beta, die beim 
	 * allerersten Aufruf der Methode newminimax500 die Werte INTEGER.MIN_VALUE bzw. INTEGER.
	 * MAX_VALUE bekommen. Das ganze wiederholt sich bis ein Knoten(Brett/Spielzustand) erreicht wurde,
	 * wo das Spiel vorbei ist. Da wird eine Auswertung mittels der Methode score geliefert. Das Vorteil
	 * von Alpha-Beta pruning ist, dass einen Knoten nur dann erkundet wird, wenn sein Alpha-wert
	 * kleiner als sein Beta-wert. Wenn dies nicht der Fall ist, dann wird aufgehoert Kinderknoten fuer 
	 * diesen Knoten zu erzeugen bzw. erkunden, was die Anzahl der Knoten des Baums stark reduziert. Letztendlich gibt die Methode ein zweielementiges int Feld
	 * zurueck mit dem besten Naechstenzug und die Auswertung, die dadurch zu erzielen erwartet ist. 
	 * 
	 *   
	 * @param alpha Die Auswertung des bisher besten berechneten Zuges fuer den Computer(Der maximierender
	 * Spieler)
	 * @param beta Die Auswertung des bisher besten berechneten Zuges fuer den Spieler(Der minimierender
	 * Spieler)
	 * @return Zweielementiges int Feld mit dem besten Naechstenzug und dessen Auswertung
	 * 
	 */
	//Uses alpha beta pruning to increase performance(does not use memoization)
	//Also utilizes canWin499 which enables it to prioritize the fastest path leading to a win
	public int[] newminimax500(int alpha, int beta){
		int bestPos=-1; //Position des besten Zuges
		int currentScore; //Variable zur voruebergehenden Lagerung der Auswertung
		int winningPos=-1; //Variable zur Lagerung des von canWin499 zurueckgegebenen Wertes
		if(isGameOver2()!='N'){ //Pruefen ob Spiel vorbei ist
			int[] answer = {score(), bestPos};                                    
			return answer;
		}
		else{
			for(int x:getAvailableMoves()){ 
				if(turn=='O'){	//O is maximizer
					winningPos=canWin499(); //Pruefen ob ein Gewinn in dieser Runde erzielbar ist
					if(winningPos!=-1)
						setO(winningPos);
					else 
						setO(x);
					//System.out.println(stateID++);
					currentScore = newminimax500(alpha, beta)[0];
					if(currentScore>alpha){
						alpha=currentScore; 
						if(winningPos!=-1)
							bestPos=winningPos;
						else
							bestPos=x;
					}
				}
				else {	//X is minimizer
					winningPos=canWin499();
					if(winningPos!=-1)
						setX(winningPos);
					else 
						setX(x);
					//System.out.println(stateID++);
					currentScore = newminimax500(alpha, beta)[0];
					if(currentScore<beta){
						beta=currentScore;
						if(winningPos!=-1)
							bestPos=winningPos;
						else
							bestPos=x;
					}
				}
				if(winningPos!=-1){
					revert(winningPos);
				}
				else
					revert(x);
				if(alpha>=beta)
					break;
			}
			if(turn=='O'){ 
				int[] answer = {alpha, bestPos};                                    
				return answer;
			}
			else {
				int[] answer = {beta, bestPos};                                    
				return answer;
			}
		}
	}
}
