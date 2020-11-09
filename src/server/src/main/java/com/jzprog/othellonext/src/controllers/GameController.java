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
import com.jzprog.othellonext.src.model.StateDTO;
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
		return new ResponseEntity<>(gameId, HttpStatus.OK);
	}
	
	@ControllerAdvice
    @GetMapping(PLAY_MOVE_ENDPOINT)
	public ResponseEntity<?> play(@RequestParam("gameId") String gameId, StateDTO stateDTO) {
    	gameService.setCurrentMove(new Action(stateDTO.getMoveX(), stateDTO.getMoveY()));
    	if (gameService.isCompleted().isSuccess()) {
    		gameService.play();
    		gameService.nextState();
    		return new ResponseEntity<>(MoveResults.VALID_MOVE, HttpStatus.OK);
    	}
		return new ResponseEntity<>(gameService.isCompleted().getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    

	@ControllerAdvice
    @GetMapping(SELECT_TURN_ENDPOINT)
	public ResponseEntity<?> choose(@RequestParam("gameId") String gameId, StateDTO stateDTO) {
    	gameService.setPlayer(TileStates.valueOf(stateDTO.getPlayerColor()));
    	if (gameService.isCompleted().isSuccess()) {
    		gameService.nextState();
    		return new ResponseEntity<>(INIT_GAME_SUCCESS, HttpStatus.OK);
    	}
    	return new ResponseEntity<>(gameService.isCompleted().getErrorMessage(), HttpStatus.FORBIDDEN);	
	}
}
