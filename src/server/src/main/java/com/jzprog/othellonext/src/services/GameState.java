package com.jzprog.othellonext.src.services;

import com.jzprog.othellonext.src.model.Validatable;

public interface GameState extends Validatable {
	void makeMove(GameService game);
    void next(GameService game);
}
