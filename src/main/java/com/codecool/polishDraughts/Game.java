package com.codecool.polishDraughts;

import java.util.Scanner;

public class Game {
    private final Board board;
    private int player;

    public Game(Board board){
        this.board = board;
        this.player = 1;
    }

    public void playRound(){
        Coordinates[] stepsCoords = getMove();
        board.movePawn(stepsCoords);
        board.printBoard();
    }

    public void changePlayer(){
        player = player % 2 + 1;
    }

    public boolean isPawnValid(Coordinates position){
        /*
        This method checks whether the coordinates point to the correct pawn,
        i.e. if it belongs to the player
         */
        Pawn[][] fields = board.getFields();
        if (fields[position.getY()][position.getX()] != null) {
            Pawn pawn = fields[position.getY()][position.getX()];
            return pawn.getColor().equals("blue") && player == 1
                    || pawn.getColor().equals("red") && player == 2;
        }
        return false;
    }

    public boolean areCoordsOnBoard(Coordinates position){
        return position.getX() >= 0
                && position.getY() >= 0
                && position.getX() < board.getSize()
                && position.getY() < board.getSize();
    }

    public boolean areAllCoordsOnBoard(Coordinates[] positions){
        for(Coordinates coords: positions){
            if(!areCoordsOnBoard(coords)){
                return false;
            }
        }
        return true;
    }

    public boolean areAllCoordsOfCorrectFormat(String[] allCoords){
        for(String movePart: allCoords){
            if(!isCoordsFormatCorrect(movePart)){
                return false;
            }
        }
        return true;
    }

    private Boolean isCoordsFormatCorrect(String coords) {
        coords = coords.toUpperCase();
        if (coords.length() <= 1){
            return false;
        }
        char row = coords.charAt(0);
        if ( (int)row < (int)'A' || (int)row >= (int)'Z') {
            return false;
        }
        try{
            Integer.parseInt(coords.substring(1));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean checkForWinner() {
        boolean blues = false;
        boolean reds = false;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getFields()[i][j] == null){
                    continue;
                }
                if (board.getFields()[i][j].getColor().equals("red")) {
                    reds = true;
                } else if (board.getFields()[i][j].getColor().equals("blue")) {
                    blues = true;
                }
            }
        }
        return (!blues || !reds);
    }

    public void printResult() {
        System.out.printf("Player %d has won!%n", player);
    }

    public Coordinates[] getMove(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.printf("PLAYER %d enter your move (for example a1-b2 or a1-c3-a5) : ", player);
            String move = scanner.nextLine();
            String[] moveParts = move.split("-");
            if(!areAllCoordsOfCorrectFormat(moveParts)) {
                System.out.println("wrong format");
                continue;
            }
            Coordinates[] positions = convertAllCoords(moveParts);
            if(!areAllCoordsOnBoard(positions)){
                System.out.println("Coordinates out of board");
                continue;
            }
            if(!isPawnValid(positions[0])){
                System.out.println("First coordinate doesn't match your pawn");
                continue;
            }
            Pawn pawn = board.getPawn(positions[0]);
            if(!pawn.isMoveValid(board, positions)){
                System.out.println("The move is against the rules of the game");
                continue;
            }
            return positions;
        }
    }

    public Coordinates convertCoords(String coords){
        coords = coords.toUpperCase();
        Integer x = (int)coords.charAt(0) - (int)'A';
        Integer y = Integer.parseInt(coords.substring(1)) - 1;
        return new Coordinates(x, y);
    }

    public Coordinates[] convertAllCoords(String[] moveParts){
        Coordinates[]  positions = new Coordinates[moveParts.length];
        for(int i = 0; i < moveParts.length; i++){
            positions[i] = convertCoords(moveParts[i]);
        }
        return positions;
    }
}

