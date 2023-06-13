package com.codecool.polishDraughts;

import java.util.Scanner;

public class Draughts {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int size;
        do {
            System.out.print("Enter a board size [10-20]: ");
            size = scanner.nextInt();
        } while (size < 10 || size > 20);

        Board board = new Board(size);
        Game game = new Game(board);

        board.printBoard();

        while(true){
            game.playRound();
            if(game.checkForWinner()){
                game.printResult();
                break;
            }
            game.changePlayer();
        }
    }
}
