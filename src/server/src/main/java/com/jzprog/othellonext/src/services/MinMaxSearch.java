package com.jzprog.othellonext.src.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.StateInfo;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;


@Service
public class MinMaxSearch  {
	
	@Value("${min.max.search.depth}")
	private int depth;

	private String computerPlayer; // computer player's color
	
	public void updateDepth(int customDepth) {
		this.depth = customDepth;
	}

	@LogMethodInfo
	public Action makeDecision(StateInfo state){
	  computerPlayer = state.getPlayerToMove();
	  int temp_depth = depth;
	  Action result = null;
	  double resultValue = Double.NEGATIVE_INFINITY;
	  for (Action action : state.getAvailableMoves()){ // for every available move
		double value = minValue(getResult(state, action),Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		if (value > resultValue) { // find the max of children's values
		  result = action;
		  resultValue = value;
		}
		depth = temp_depth;
	   }
	   if (result != null) result.setPlayerToMove(TileStates.valueOf(computerPlayer));
	   return result;
    }
	
	// returns the state-result after performing the given action on the given state
	private StateInfo getResult(StateInfo state, Action action){
	  StateInfo result = state.clone();
      result.putDisc(action);
	  return result;
	}
	  
	// checks if the given state is a terminal state of the game
	private boolean isTerminal(StateInfo state){
	  return state.getUtility() != -1;
	}
	
	// finds the maximum value of given state's children values
	private double maxValue(StateInfo state,double alpha,double beta){ 
	  depth--;
	  if (isTerminal(state) || depth <= 0) return heuristicFunctionValue(state);
	  double value = Double.NEGATIVE_INFINITY;
	  for (Action  action : state.getAvailableMoves()){
		value = Math.max(value,minValue(getResult(state, action),alpha,beta));
		if (value >= beta) return value;
		alpha = Math.max(alpha, value);
	  }
	  return value;
	 }

	// finds the minimum value of given state's children values
	private double minValue(StateInfo state,double alpha,double beta){ 
	  depth--;
	  if (isTerminal(state) || depth <= 0)	return heuristicFunctionValue(state);
	  double value = Double.POSITIVE_INFINITY;
	  for (Action action : state.getAvailableMoves()){
		value = Math.min(value,maxValue(getResult(state, action),alpha,beta));
		if (value <= alpha) return value;
		beta = Math.min(beta, value);
	   }
	   return value;
	 }

	// produce a value for the given state depending on various factors such as disc parity and mobility
	private double heuristicFunctionValue(StateInfo state) {
	  double value = 0;
	  if (isTerminal(state)) {
	     value = state.getUtility();
		 String winnerColor = value == 1 ? TileStates.BLACK.name() : value == 0 ? TileStates.WHITE.name() : "";
		 if (winnerColor.equals("")) { // draw
			value = 10;
		 } else if (computerPlayer.equals(winnerColor)) { // if computer won
			value = 14000;	
		 } else { // player won
			value = -14000;
		 }
	   }
	   else {
		 value = applyDiscParity(state, value);
	   }
	   return value;
	 }
	
	 // discs' parity function
	 private double applyDiscParity(StateInfo state, double value) {
		 String playerColor = state.getOpponentColor(TileStates.valueOf(computerPlayer)).name();
		 double num1 = state.getNumberOfDiscs(computerPlayer);
		 double num2 = state.getNumberOfDiscs(playerColor);
		 value = 100*(num1 - num2)/(num1 + num2);
		 //number of corners captured by players function
		 double corn1 = state.getNumberOfCorners(computerPlayer);
		 double corn2 = state.getNumberOfCorners(playerColor);	
		 value += 80*(25*(corn1-corn2));
		 //number of discs close to corners captured function
		 double clcorn1 = state.getNumberOfDiscsCloseToCorners(computerPlayer);
		 double clcorn2 = state.getNumberOfDiscsCloseToCorners(playerColor);
		 value += 30*(-12.5*(clcorn1 - clcorn2));
		 //discs' mobility function
		 double mob1 = 0;
		 double mob2 = 0;
		 if (playerColor.equals(state.getPlayerToMove())) {
			 mob1 = state.getAvailableMoves().size();
			 state.setPlayerToMove(TileStates.valueOf(computerPlayer));
			 state.calculateAvailableMoves();
			 mob2 = state.getAvailableMoves().size();
		 } else {
			 mob2 = state.getAvailableMoves().size();
			 state.setPlayerToMove(TileStates.valueOf(playerColor));
			 state.calculateAvailableMoves();
			 mob1 = state.getAvailableMoves().size();
		 }
		 value += 8*(100*(mob1 - mob2)/(mob1 + mob2));
		 return value;
	 }
}