package com.jzprog.othellonext.src.model;

import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class StateInfo implements Cloneable {
	
	public List<Action> getEmptySquares() {
		return null;
	}
	
	public void setSquareValue(int x, int y, String player) {
		
	}
	
	public double getUtility() {
		return 0.0;
	}
	
	public String getPlayerToMove() {
		return null;
	}
	
	//creates a copy of the state
		public StateInfo clone(){
			StateInfo cp = null;
		  try {
			cp = (StateInfo) super.clone();
//			cp.OthelloBoard = new String[8][8];
//			for(int i=0;i<cp.OthelloBoard.length;i++){
//			  for(int j=0;j<cp.OthelloBoard[i].length;j++){
//			    cp.OthelloBoard[i][j] = OthelloBoard[i][j];
//			   }
//			  }	
		   } catch (CloneNotSupportedException e){
			   e.printStackTrace();
			 }
		   return cp;
		}
		
		//checks if a move in r,c position flips at least one disc
		public boolean checkForMatches(int r , int c){
			boolean flag = false;
			//flag = !DiagonalMatches(r,c).isEmpty() || !RowMatches(r,c).isEmpty() || !ColumnMatches(r,c).isEmpty();
			
			return flag;
		}
		
		//returns the number of discs the given player has played
		public int getNumberOfDiscs(String player) {
			int num = 0;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
//					if (getSquareValue(i,j).compareTo(player) == 0){
//						num++;
//					}
				}
			}
			return num;
		}
		
		//returns the number of corners the given player has captured
		public int getNumberOfCorners(String player){
			int num = 0;
//			if (OthelloBoard[0][0].compareTo(player) == 0) num++;
//			if (OthelloBoard[7][0].compareTo(player) == 0) num++;
//			if (OthelloBoard[7][7].compareTo(player) == 0) num++;
//			if (OthelloBoard[0][7].compareTo(player) == 0) num++;
			return num;
		}
		
		//returns the number of discs close to corners the given player has played
		public int getNumberOfDiscsCloseToCorners(String player){
			int num = 0;
//			if (OthelloBoard[0][0].compareTo("Empty") == 0){
//				if (OthelloBoard[0][1].compareTo(player) == 0) num++;
//				if (OthelloBoard[1][1].compareTo(player) == 0) num++;
//				if (OthelloBoard[1][0].compareTo(player) == 0) num++;
//			}
//			
//			if (OthelloBoard[0][7].compareTo("Empty") == 0){
//				if (OthelloBoard[0][6].compareTo(player) == 0) num++;
//				if (OthelloBoard[1][6].compareTo(player) == 0) num++;
//				if (OthelloBoard[1][7].compareTo(player) == 0) num++;
//			}
//			if (OthelloBoard[7][7].compareTo("Empty") == 0){
//				if (OthelloBoard[6][7].compareTo(player) == 0) num++;
//				if (OthelloBoard[6][6].compareTo(player) == 0) num++;
//				if (OthelloBoard[7][6].compareTo(player) == 0) num++;
//			}
//			if (OthelloBoard[7][0].compareTo("Empty") == 0){
//				if (OthelloBoard[7][1].compareTo(player) == 0) num++;
//				if (OthelloBoard[6][1].compareTo(player) == 0) num++;
//				if (OthelloBoard[6][0].compareTo(player) == 0) num++;
//			}
			return num;
		}
		
		public void putDisc(Action action){
			putDisc(action.getX(),action.getY());
		}
		
		// puts a disc in the square in r,c position
		public void putDisc(int r,int c){
//		  if(utility == -1 && !canPlay(playerToMove)){  // if current player is unable to play
//		    JOptionPane.showMessageDialog(null,playerToMove+" can't play!Next Player's Turn!","Player cannot play",JOptionPane.INFORMATION_MESSAGE);
//			playerToMove = (playerToMove.compareTo("Black") == 0 ? "White" : "Black");
//		   }
//		  else{ // if current player has moves to play
//			 if (utility == -1 && isEmptySquare(r, c)) {
//			   OthelloBoard[r][c] = playerToMove; //play the move
//			   boolean check = checkForFlip(r,c);
//			   if (check){ // if there is at least one flip
//				 analyzeUtility(); //update utility
//				 playerToMove = (playerToMove.compareTo("Black") == 0 ? "White" : "Black"); // next player's turn
//				}
//				else { // if there are no flips
//				  OthelloBoard[r][c] = "Empty";
//				  JOptionPane.showMessageDialog(null,"Invalid Move!Try Again!","Invalid Move",JOptionPane.ERROR_MESSAGE);
//				  analyzeUtility();
//				}  
//			   }
//		   }
		}

}
