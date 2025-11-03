package com.game.shadowcircle.service.impl;

import com.game.shadowcircle.service.DialogueService;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsoleDialogueService implements DialogueService {

  private final Scanner scanner = new Scanner(System.in);

  @Override
  public void print(String text) {
    System.out.println(text);
  }

  @Override
  public String prompt(String question) {
    System.out.print(question + " ");
    return scanner.nextLine();
  }
}

