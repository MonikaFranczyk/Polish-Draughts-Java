package com.codecool.polishDraughts;

import java.util.ArrayList;

public class Pawn {
    private final String color;
    private Coordinates position;
    private boolean isCrowned;

    public Pawn(String color, Coordinates position){
        this.color = color;
        this.position = position;
        this.isCrowned = false;
    }

    public String getColor(){ return color; }

    public void setPosition(Coordinates position) { this.position = position; }

    public boolean isCrowned() { return isCrowned; }

    public void setCrowned(boolean crowned) { isCrowned = crowned; }

    public boolean isMoveValid(Board board, Coordinates[] positions){
        if (isCrowned) {
            return isKingsMoveValid(board, positions);
        }
        else {
            return isPawnMoveValid(board, positions);
        }
    }

    public boolean isPawnMoveValid(Board board, Coordinates[] positions) {
        String color = board.getPawn(positions[0]).getColor();
        for (int i = 1; i < positions.length; i++) {
            int vectorX = positions[i].getX() - positions[i - 1].getX();
            int vectorY = positions[i].getY() - positions[i - 1].getY();
            if (board.getPawn(positions[i]) != null) {
                return false;
            }
            if (positions.length == 2 && Math.abs(vectorX) == 1) {
                return color.equals("blue") && vectorY == -1
                        || color.equals("red") && vectorY == 1;
            }
            ArrayList<Coordinates> nonEmptyPositionsInBetween =
                    board.getPositionsOfPawnsBetween(positions[i - 1], positions[i]);
            if (Math.abs(vectorX) != 2
                    || Math.abs(vectorY) != 2
                    || nonEmptyPositionsInBetween.size() != 1
                    || board.getPawn(nonEmptyPositionsInBetween.get(0)).getColor().equals(color)) {
                return false;
            }
        }
        return true;
    }


    public boolean isKingsMoveValid(Board board, Coordinates[] positions){
        for (int i = 1; i < positions.length; i++){
            ArrayList<Coordinates> nonEmptyPositionsInBetween =
                    board.getPositionsOfPawnsBetween(positions[i-1], positions[i]);
            if (board.getPawn(positions[i]) != null
                    || Math.abs(positions[i-1].getX() - positions[i].getX())
                    != Math.abs(positions[i-1].getY() - positions[i].getY())
                    || nonEmptyPositionsInBetween.size() > 1
                    || nonEmptyPositionsInBetween.size() == 1
                    && board.getPawn(nonEmptyPositionsInBetween.get(0)).getColor().equals(color)) {
                return false;
            }
        }
        return true;
    }

    public boolean haveToBeCrowned(int size){
        return color.equals("blue") && position.getY() == 0
                || color.equals("red") && position.getY() == size - 1;
    }
}