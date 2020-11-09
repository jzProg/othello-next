package com.jzprog.othellonext.src.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.StateInfo;


@Service
public class MinMaxSearch  {
	
	@Value("${min.max.search.depth}")
	private int depth;

	private String computerPlayer; //computer player's color

	@LogMethodInfo
	public Action makeDecision(StateInfo state){
		  int temp_depth = depth;
		  Action result = null;
		  double resultValue = Double.NEGATIVE_INFINITY;
		  String player = getPlayer(state);
		  for (Action action : getActions(state,player)){ // for every available move
			double value = minValue(getResult(state, action),Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (value > resultValue) { // find the max of children's values
			  result = action;
			  resultValue = value;
			}
			depth = temp_depth;
		   }
		   return result;
    }
	
	//returns player's valid available moves starting from the given state 
	  List<Action> getActions(StateInfo state,String player){ 
	    List<Action> list = new ArrayList<Action>();
		for(Action a:state.getEmptySquares()){
		  state.setSquareValue(a.getX(), a.getY(),player);
		  if(state.checkForMatches(a.getX(), a.getY())){
			list.add(a);
		  }
		  state.setSquareValue(a.getX(), a.getY(),"Empty");
		}
		return list;
	  }
	  
	//returns the player who is about to move
	  public String getPlayer(StateInfo state){
	    return state.getPlayerToMove();
	  }

	  //returns the state-result after performing the given action on the given state
	  public StateInfo getResult(StateInfo state, Action action){
	    StateInfo result = state.clone();
		result.putDisc(action);
		return result;
	  }
	  
	  //checks if the given state is a terminal state of the game
	  public boolean isTerminal(StateInfo state){
		return state.getUtility() != -1;
	  }
	
	//finds the maximum value of given state's children values
	private double maxValue(StateInfo state,double alpha,double beta){ 
	  depth--;
	  String player = getPlayer(state);
	  if (isTerminal(state) || depth <= 0) return heuristicFunctionValue(state);
	  double value = Double.NEGATIVE_INFINITY;
	  for (Action  action :getActions(state,player)){
		value = Math.max(value,minValue(getResult(state, action),alpha,beta));
		if (value >= beta) return value;
		alpha = Math.max(alpha, value);
	  }
	  return value;
	 }

	//finds the minimum value of given state's children values
	private double minValue(StateInfo state,double alpha,double beta){ 
	  depth--;
	  String player = getPlayer(state);	
	  if (isTerminal(state) || depth <= 0)	return heuristicFunctionValue(state);
	  double value = Double.POSITIVE_INFINITY;
	  for (Action action : getActions(state,player)){
		value = Math.min(value,maxValue(getResult(state, action),alpha,beta));
		if (value <= alpha) return value;
		beta = Math.min(beta, value);
	   }
	   return value;
	 }

	//produce a value for the given state depending on various factors such as disc parity and mobility
	private double heuristicFunctionValue(StateInfo state){
		  double value = 0;
		  if (isTerminal(state)){
		    value = state.getUtility();
			if (value == 1){ // if winner is black player
			  if (computerPlayer.compareTo("Black") == 0) value = 14000;
			  else value = -14000;
			}
			else if (value == 0){ // if winner is white player
			  if (computerPlayer.compareTo("White") == 0) value = 14000;
			  else value = -14000;
			}
			else if (value == 0.5) value = 10; // if draw
		   }
		   else{
			 //discs' parity function
		     String playerColor = (computerPlayer.compareTo("Black") == 0?"White":"Black");
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
			 for (Action a : getActions(state, computerPlayer)) mob1 ++;
			 for (Action a : getActions(state, playerColor)) mob2 ++;
			 value += 8*(100*(mob1 - mob2)/(mob1 + mob2));
			}
		    return value;
		 }
}