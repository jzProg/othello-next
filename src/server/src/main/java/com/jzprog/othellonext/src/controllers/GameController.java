package com.jzprog.othellonext.src.controllers;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jzprog.othellonext.src.advices.ControllerAdvice;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.GameDTO;
import com.jzprog.othellonext.src.services.GameService;

import static com.jzprog.othellonext.src.utils.SystemMessages.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
	
    Logger log = Logger.getLogger(GameController.class.getName());
    
	@Autowired
	private GameService gameService;

	@ControllerAdvice
	@GetMapping(INIT_ENDPOINT)
	public ResponseEntity<?> initGame() {
		int gameId = gameService.init();
		if (!gameService.isCompleted().isSuccess()) {
			return new ResponseEntity<>(gameService.isCompleted().getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		GameDTO gameDTO = new GameDTO();
		gameDTO.setGameId(gameId);
		gameDTO.setGameMessage(INIT_GAME_SUCCESS);
		gameDTO.setBoard(gameService.getBoard());
		gameDTO.setPlayerToMove(gameService.getInfo().getPlayerToMove());
		return new ResponseEntity<>(gameDTO, HttpStatus.OK);
	}
	
	@ControllerAdvice
    @GetMapping(PLAY_MOVE_ENDPOINT)
	public ResponseEntity<?> play(@RequestParam("gameId") String gameId, GameDTO stateDTO) {
    	gameService.setCurrentMove(new Action(stateDTO.getMoveX(), stateDTO.getMoveY(), gameService.getPlayer()));
    	if (gameService.isCompleted().isSuccess()) {
    		gameService.play();
    		gameService.nextState();
    		stateDTO.setGameMessage(String.format(MoveResults.VALID_MOVE.getText(), stateDTO.getMoveX(), stateDTO.getMoveY()));
    		stateDTO.setPlayerToMove(gameService.getInfo().getPlayerToMove());
    		stateDTO.setBoard(gameService.getBoard());
    		return new ResponseEntity<>(stateDTO, HttpStatus.OK);
    	}
		return new ResponseEntity<>(gameService.isCompleted().getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ControllerAdvice
    @GetMapping(AI_MOVE_ENDPOINT)
	public ResponseEntity<?> getAIMove(@RequestParam("gameId") String gameId, GameDTO stateDTO) {
		gameService.play(); // AI move
		Action AIMove = gameService.getCurrentMove();
		stateDTO.setMoveX(AIMove.getX());
		stateDTO.setMoveY(AIMove.getY());
		stateDTO.setPlayerToMove(gameService.getInfo().getPlayerToMove());
		stateDTO.setBoard(gameService.getBoard());
		return new ResponseEntity<>(gameService.isCompleted().getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    

	@ControllerAdvice
    @GetMapping(SELECT_TURN_ENDPOINT)
	public ResponseEntity<?> choose(@RequestParam("gameId") String gameId, GameDTO stateDTO) {
		TileStates color = TileStates.valueOf(stateDTO.getPlayerColor());
    	gameService.setPlayer(color);
    	if (gameService.isCompleted().isSuccess()) {
    		gameService.nextState();
    		stateDTO.setGameMessage(color.equals(TileStates.BLACK) ? PLAY_FIRST_MESSAGE : PLAY_AI_MESSAGE);
    		return new ResponseEntity<>(stateDTO, HttpStatus.OK);
    	}
    	return new ResponseEntity<>(gameService.isCompleted().getErrorMessage(), HttpStatus.FORBIDDEN);	
	}
}
