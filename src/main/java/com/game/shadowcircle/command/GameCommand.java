package com.game.shadowcircle.command;

public interface GameCommand {

  void execute();

  void undo();

  boolean canExecute();
}