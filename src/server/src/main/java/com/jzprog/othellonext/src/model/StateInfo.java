package com.jzprog.othellonext.src.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

@Component
public class StateInfo implements Cloneable {
	
	private TileStates playerToMove; // player who is about to move
	private TileStates[][] othelloBoard;
	private double utility; //value depending on the winner(1:win for black,0:win for white,0.5:draw,-1:non terminal state)
	
	public StateInfo(){}
	
	public StateInfo(TileStates[][] board, TileStates playerToMove, double utility) {
		othelloBoard = board;
		this.utility = utility;
		this.playerToMove = playerToMove;
	}
	
	public TileStates[][] getOthelloBoard() {
		return othelloBoard;
	}

	public void setOthelloBoard(TileStates[][] othelloBoard) {
		this.othelloBoard = othelloBoard;
	}

	public void setPlayerToMove(TileStates playerToMove) {
		this.playerToMove = playerToMove;
	}

	//checks if the square in r,c position is empty
	public boolean isEmptySquare(int r,int c){
	  return othelloBoard[r][c].equals(TileStates.EMPTY);
	}
	
	//returns the value of the square in r,c position
	public String getSquareValue(int r, int c) {
	  return othelloBoard[r][c].name();
	}

	public List<Action> getEmptySquares() {
	  List<Action> list = new ArrayList<Action>();
	  for(int i=0;i<8;i++){
		for(int j=0;j<8;j++){
		  if (isEmptySquare(i,j)){
		    list.add(new Action(i,j));
		  }
		 }
	    }
		return  list;
	}
	
	public void setSquareValue(int x, int y, String player) {
		othelloBoard[x][y] = TileStates.valueOf(player.toUpperCase());
	}
	
	public double getUtility() {
		return utility;
	}
	
	public String getPlayerToMove() {
		return playerToMove.name();
	}
	
	//creates a copy of the state
	public StateInfo clone(){
	  StateInfo cp = null;
	  try {
		cp = (StateInfo) super.clone();
		cp.othelloBoard = new TileStates[8][8];
		for(int i=0;i<cp.othelloBoard.length;i++){
		  for(int j=0;j<cp.othelloBoard[i].length;j++){
		    cp.othelloBoard[i][j] = othelloBoard[i][j];
		   }
		 }	
		cp.playerToMove = playerToMove;
	   } catch (CloneNotSupportedException e){
		   e.printStackTrace();
	   }
	   return cp;
	}
		
	//checks if a move in r,c position flips at least one disc
	public boolean checkForMatches(int r , int c){
		boolean flag = false;
		flag = !DiagonalMatches(r,c).isEmpty() || !RowMatches(r,c).isEmpty() || !ColumnMatches(r,c).isEmpty();	
		return flag;
	}
		
	//returns the number of discs the given player has played
	public int getNumberOfDiscs(String player) {
		int num = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getSquareValue(i,j).compareTo(player.toUpperCase()) == 0){
					num++;
				}
			}
		}
		return num;
	}
		
	//returns the number of corners the given player has captured
	public int getNumberOfCorners(String player){
		int num = 0;
		if (othelloBoard[0][0].name().compareTo(player.toUpperCase()) == 0) num++;
		if (othelloBoard[7][0].name().compareTo(player.toUpperCase()) == 0) num++;
		if (othelloBoard[7][7].name().compareTo(player.toUpperCase()) == 0) num++;
		if (othelloBoard[0][7].name().compareTo(player.toUpperCase()) == 0) num++;
		return num;
	}
	
	//returns the number of discs close to corners the given player has played
	public int getNumberOfDiscsCloseToCorners(String player){
		int num = 0;
		if (othelloBoard[0][0].equals(TileStates.EMPTY)){
			if (othelloBoard[0][1].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[1][1].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[1][0].name().compareTo(player.toUpperCase()) == 0) num++;
		}
		
		if (othelloBoard[0][7].equals(TileStates.EMPTY)){
			if (othelloBoard[0][6].name().compareTo(player.toUpperCase()) == 0) num++;				
			if (othelloBoard[1][6].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[1][7].name().compareTo(player.toUpperCase()) == 0) num++;
		}
		if (othelloBoard[7][7].equals(TileStates.EMPTY)){
			if (othelloBoard[6][7].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[6][6].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[7][6].name().compareTo(player.toUpperCase()) == 0) num++;
		}
		if (othelloBoard[7][0].equals(TileStates.EMPTY)){
			if (othelloBoard[7][1].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[6][1].name().compareTo(player.toUpperCase()) == 0) num++;
			if (othelloBoard[6][0].name().compareTo(player.toUpperCase()) == 0) num++;
		}
		return num;
	}
	
	public void putDisc(Action action){
		putDisc(action.getX(),action.getY());
	}
	
	public void resetMove(Action action) {
	  othelloBoard[action.getX()][action.getY()] = TileStates.EMPTY;
	}
	
	
	// puts a disc in the square in r,c position
	@LogMethodInfo
	public void putDisc(int r,int c){
	  othelloBoard[r][c] = playerToMove;
	  analyzeUtility(); //update utility
	  checkForFlip(r, c);
	  playerToMove = (playerToMove.equals(TileStates.BLACK) ? TileStates.WHITE : TileStates.BLACK); // next player's turn
	}

	//checks diagonal,vertical and horizontal if there is at least one disc for flipping and flips it
	public boolean checkForFlip(int r,int c){
		boolean flag = false;
		for (Action d :DiagonalMatches(r,c)) {
			if (getSquareValue(d.getX(),d.getY()).compareTo(getSquareValue(r,c)) != 0){
				flip(d.getX(),d.getY());
				flag = true;
			}
		}
		for (Action d :RowMatches(r,c)) {
			if (getSquareValue(d.getX(),d.getY()).compareTo(getSquareValue(r,c)) != 0){
				flip(d.getX(),d.getY());
				flag = true;
			}
		}
		for (Action d :ColumnMatches(r,c)) {
			if (getSquareValue(d.getX(),d.getY()).compareTo(getSquareValue(r,c)) != 0){
				flip(d.getX(),d.getY());
				flag = true;
			}
		}
		return flag;
	}
		
	// flips the disc in r,c position
	private void flip(int r,int c){
		 if (othelloBoard[r][c].equals(TileStates.BLACK)){
			 othelloBoard[r][c] = TileStates.WHITE;
		 }
		 else if (othelloBoard[r][c].equals(TileStates.WHITE)){
			 othelloBoard[r][c] = TileStates.BLACK;
		 }
	}
	
	//checks the 2 diagonals of the board and returns a list with all the discs which are about to be flipped
	private List<Action> DiagonalMatches(int r,int c){
		List<Action> list = new ArrayList<Action>();
		List<Action> list1 = new ArrayList<Action>();
		int i;
		int g = r-1;
		if (g >= 0){
		for( i = c+1;i<8;i++){
			if (g < 0){
				break;
			}
			if(othelloBoard[g][i].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[g][i].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list1.add(new Action(g,i));
			}
			g--;
		}
		if (i == 8 || g == -1) list1.clear();
		if (i != 8 && g != -1 && othelloBoard[g][i].equals(TileStates.EMPTY)) list1.clear();
		}
		List<Action> list2 = new ArrayList<Action>();
		int j;
		int g2 = r+1;
		if (g2 <= 8){
		for(j = c-1;j>=0;j--){
			if (g2 >= 8){
				break;
			}
			if(othelloBoard[g2][j].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[g2][j].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list2.add(new Action(g2,j));
			}
			g2++;
		}
		if (j == -1 || g2 >= 8) list2.clear();
		if (j != -1 && g2 != 8 && othelloBoard[g2][j].equals(TileStates.EMPTY)) list2.clear();
		}
		List<Action> list3 = new ArrayList<Action>();
		int k;
		int s = c+1;
		if (s <= 8){
		for( k = r+1;k<8;k++){
			if (s >= 8){
				break;
			}
			if(othelloBoard[k][s].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[k][s].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list3.add(new Action(k,s));
			}
			s++;
		}
		if (k == 8 || s == 8)  list3.clear();
		if (k != 8 && s != 8 && othelloBoard[k][s].equals(TileStates.EMPTY)) list3.clear();
		}
		List<Action> list4 = new ArrayList<Action>();
		int l;
		int s2 = c-1;
		if (s2 >= 0){
		for(l = r-1;l>=0;l--){
			if (s2 < 0){
				break;
			}
			if(othelloBoard[l][s2].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[l][s2].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list4.add(new Action(l,s2));
			}
			s2--;
		}
		if (l == -1 || s2 ==-1) list4.clear();
		if (l != -1 && s2 != -1 && othelloBoard[l][s2].equals(TileStates.EMPTY)) list4.clear();
		
		}
		for (Action a : list1) list.add(a);
		for (Action a : list2) list.add(a);
		for (Action a : list3) list.add(a);
		for (Action a : list4) list.add(a);
		return list;	
	}
	
	//checks the given column of the board and returns a list with all the discs which are about to be flipped
	private List<Action> ColumnMatches(int r,int c){
		List<Action> list = new ArrayList<Action>();
		List<Action> list1 = new ArrayList<Action>();
		int i;
		for( i = r+1;i<8;i++){
			if(othelloBoard[i][c].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[i][c].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list1.add(new Action(i,c));
			}
		}
		if (i == 8 && !othelloBoard[i-1][c].equals(othelloBoard[r][c])) list1.clear();
		if (i != 8 && othelloBoard[i][c].equals(TileStates.EMPTY)) list1.clear();
		List<Action> list2 = new ArrayList<Action>();
		int j;
		for(j = r-1;j>=0;j--){
			if(othelloBoard[j][c].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[j][c].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list2.add(new Action(j,c));
			}
		}
		if (j == -1 && !othelloBoard[0][c].equals(othelloBoard[r][c])) list2.clear();
		if (j != -1 && othelloBoard[j][c].equals(TileStates.EMPTY)) list2.clear();
		for (Action a : list1) list.add(a);
		for (Action a : list2) list.add(a);
		return list;	
	}
	
	//checks the given row of the board and returns a list with all the discs which are about to be flipped
	private List<Action> RowMatches(int r,int c){
		List<Action> list = new ArrayList<Action>();
		List<Action> list1 = new ArrayList<Action>();
		int i;
		for( i = c+1;i<8;i++){
			if(othelloBoard[r][i].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[r][i].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list1.add(new Action(r,i));
			}
		}
		if (i == 8 && !othelloBoard[r][i-1].equals(othelloBoard[r][c])) list1.clear();
		if (i != 8 && othelloBoard[r][i].equals(TileStates.EMPTY)) list1.clear();
		List<Action> list2 = new ArrayList<Action>();
		int j;
		for(j = c-1;j>=0;j--){
			if(othelloBoard[r][j].equals(TileStates.EMPTY)){
				break;
			}
			if(othelloBoard[r][j].equals(othelloBoard[r][c])){
				break;
			}
			else {
				list2.add(new Action(r,j));
			}
		}
		if (j == -1 && !othelloBoard[r][0].equals(othelloBoard[r][c])) list2.clear();
		if (j != -1 && othelloBoard[r][j].equals(TileStates.EMPTY)) list2.clear();
		for (Action a : list1) list.add(a);
		for (Action a : list2) list.add(a);
		return list;		
	}
	
	//updates the utility of the state
	public void analyzeUtility(){
		int player1Num = getNumberOfDiscs(TileStates.BLACK.name());
		int player2Num = getNumberOfDiscs(TileStates.WHITE.name());
		if ((player1Num + player2Num == 64) || (!canPlay(TileStates.BLACK.name())) && !canPlay(TileStates.WHITE.name())){
			if (player1Num > player2Num){
				utility = 1;
			}
			else if (player1Num < player2Num){
				utility = 0;
			}
			else {
				utility = 0.5;
			}
		}
		
	}
	
	//checks if the given player has moves to play
	public boolean canPlay(String player){
		boolean flag = false;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(othelloBoard[i][j].equals(TileStates.EMPTY)){
					othelloBoard[i][j] = TileStates.valueOf(player.toUpperCase());
					if(checkForMatches(i,j)){
						flag = true;
						othelloBoard[i][j] = TileStates.EMPTY;
						break;
					}
					othelloBoard[i][j] = TileStates.EMPTY;
				}
			}
		}
		return flag;
	}

	@Override
	public String toString() {
		return "StateInfo [playerToMove=" + playerToMove + ", othelloBoard=" + Arrays.toString(othelloBoard)
				+ ", utility=" + utility + "]";
	}
	
	
}
