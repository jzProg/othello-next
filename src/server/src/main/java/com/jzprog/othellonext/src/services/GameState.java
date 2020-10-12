package com.jzprog.othellonext.src.services;

public interface GameState {
	void makeMove(GameService game);
    void next(GameService game);
}
