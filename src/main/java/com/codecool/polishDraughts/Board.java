package com.codecool.polishDraughts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board {
    private static final List<String> ALPHABET = Arrays.asList("𝐀","𝐁","𝐂","𝐃","𝐄","𝐅"," 𝐆","𝐇","𝐈"," 𝐉","𝐊","𝐋","𝐌","𝐍","𝐎","𝐏","𝐐"," 𝐑","𝐒","𝐓","𝐔","𝐕","𝐖","𝐗","𝐘","𝐙");
    private static final int OCCUPIED_ROWS_NUMBER = 8;
    private final Pawn[][] fields;
    private final int size;

    public Board(int size){
        this.size = size;
        fields = new Pawn[size][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if ((j + i + size + 1) % 2 == 0 && i < OCCUPIED_ROWS_NUMBER/2) {
                    fields[i][j] = new Pawn("red", new Coordinates(j, i));
                } else if ((j + i + size + 1) % 2 == 0 && i >= size - OCCUPIED_ROWS_NUMBER/2) {
                    fields[i][j] = new Pawn("blue", new Coordinates(j, i));
                } else {
                    fields[i][j] = null;
                }
            }
        }
    }

    public void printBoard() {
        StringBuilder output = new StringBuilder();
        String tableHeader = createTableHeader(size);
        for (int i = 0; i < size; i++) {
            output.append(i + 1);
            if (i + 1 < 10) output.append(" ");
            for (int j = 0; j < size; j++) {
                if (fields[i][j] == null) {
                    if ((i + j + size + 1) % 2 == 1) {
                        output.append(" ").append("⬜");
                    }
                    else {
                        output.append(" ").append("⬛");
                    }
                }
                else if (!fields[i][j].isCrowned()){
                    if (fields[i][j].getColor().equals("red")) {
                        output.append(" ").append("\uD83D\uDD34");
                    } else {
                        output.append(" ").append("\uD83D\uDD35");
                    }
                }else {
                    if (fields[i][j].getColor().equals("red")) {
                        output.append(" ").append("⭕");
                    } else {
                        output.append(" ").append("\uD83E\uDDFF");
                    }
                }
            }
            output.append("\n");
        }
        System.out.println(tableHeader);
        System.out.println(output);
    }

    public String createTableHeader(int boardSize) {
        StringBuilder tableHeader = new StringBuilder();
        tableHeader.append("    ");
        for (int index = 0; index < boardSize; index++) {
            tableHeader.append(ALPHABET.get(index)).append("  ");
        }
        return tableHeader.toString();
    }


    public void movePawn(Coordinates[] positions){
        Coordinates startCoords = positions[0];
        Coordinates endCoords = positions[positions.length - 1];
        Pawn pawn = getPawn(startCoords);
        pawn.setPosition(endCoords);
        removePawn(startCoords);
        fields[endCoords.getY()][endCoords.getX()] = pawn;
        for (int i = 1; i < positions.length; i++){
            ArrayList<Coordinates> opponentPositions
                    = getPositionsOfPawnsBetween(positions[i-1], positions[i]);
            if (opponentPositions.size() == 1) {
                Coordinates opponentPosition = opponentPositions.get(0);
                removePawn(opponentPosition);
            }
        }
        if(!pawn.isCrowned() && pawn.haveToBeCrowned(size)){
            pawn.setCrowned(true);
        }
    }

    public ArrayList<Coordinates> getPositionsOfPawnsBetween(Coordinates position1, Coordinates position2){
        int x1 = position1.getX();
        int x2 = position2.getX();
        int y1 = position1.getY();
        int y2 = position2.getY();
        int xi, yi;
        ArrayList<Coordinates> pawnsPositions = new ArrayList<>();
        for(int i = 1; i < Math.abs(x1 - x2); i++){
            xi = x1 + i * (int)Math.signum(x2 - x1);
            yi = y1 + i * (int)Math.signum(y2 - y1);
            if (fields[yi][xi] != null){
                pawnsPositions.add(new Coordinates(xi, yi));
            }
        }
        return pawnsPositions;
    }

    public void removePawn(Coordinates position){
        fields[position.getY()][position.getX()] = null;
    }

    public int getSize() {
        return size;
    }

    public Pawn[][] getFields() {
        return fields;
    }

    public Pawn getPawn(Coordinates position){
        return fields[position.getY()][position.getX()];
    }
}