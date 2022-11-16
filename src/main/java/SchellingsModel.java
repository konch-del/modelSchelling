import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SchellingsModel {

    private Section[][] table;
    private int size;
    private int numberOfNeighbors;
    private int numberOfHappy = 0;
    private int numberOfUnhappy = 0;
    private ArrayList<Positions> unhappyPositions = new ArrayList<>();
    private ArrayList<Positions> emptySpace = new ArrayList<>();

    public SchellingsModel(int size, int blue, int red, int numberOfNeighbors){
        this.size = size - 1;
        this.table = new Section[size][size];
        this.numberOfNeighbors = numberOfNeighbors;

        int numberOfBlue = size * size * blue / 100;
        int numberOfRed = size * size * red / 100;
        int k = 0;
        while(k < numberOfBlue){
            int row = (int) (Math.random() * size);
            int column = (int) (Math.random() * size);
            if(table[row][column] == null){
                table[row][column] = new Section(Colors.Blue);
                k++;
            }
        }
        k = 0;
        while(k < numberOfRed){
            int row = (int) (Math.random() * size);
            int column = (int) (Math.random() * size);
            if(table[row][column] == null){
                table[row][column] = new Section(Colors.Red);
                k++;
            }
        }

        for (int i = 0; i <= this.size; i++) {
            for (int j = 0; j <= this.size; j++) {
                if(table[i][j] == null){
                    emptySpace.add(new Positions(i, j));
                }
            }
        }
        checkHappinessForAll();
    }
    public void printTable() throws IOException {
        printTable(new File("C:\\Users\\igor-\\IdeaProjects\\modelSchelling\\model.xlsx"));
    }

    public void printTable(File file) throws IOException {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("SchellingsModel");
        for (int i = 0; i <= size; i++){
            Row row = sheet.createRow(i);
            for(int j = 0; j <= size; j++){
                Cell cell = row.createCell(j);
                cell.setCellValue(table[i][j] != null ? table[i][j].toString() : "0");
                if(table[i][j] != null) {
                    CellStyle cellStyle = book.createCellStyle();
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(cellStyle);
                    if (table[i][j].getColor() == Colors.Blue) {
                        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    } else {
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.RED.getIndex());
                    }
                }
            }
        }

        Row rowHappy = sheet.createRow(size + 1);
        Cell cellHappy = rowHappy.createCell(0);
        cellHappy.setCellValue("Количество счастливых: " + numberOfHappy);
        Row rowUnhappy = sheet.createRow(size + 2);
        Cell cellUnhappy = rowUnhappy.createCell(0);
        cellUnhappy.setCellValue("Количество несчастных: " + numberOfUnhappy);
        Row positionsUnhappy = sheet.createRow(size + 3);
        Cell unHappy = positionsUnhappy.createCell(0);
        unHappy.setCellValue("Адреса несчатных: " + unhappyPositions.stream().map(Positions::toString).collect(Collectors.joining(",")));
        book.write(new FileOutputStream(file));
        book.close();
    }

    private void checkHappinessForCell(int i, int j){
        if (table[i][j] != null) {
            if (i == 0) {
                if (j == 0) {
                    checkUpperLeftCorner();
                } else {
                    if (j == size) {
                        checkUpperRightCorner();
                    } else {
                        checkTop(i, j);
                    }
                }
            } else {
                if (i == size) {
                    if (j == 0) {
                        checkLowerLeftCorner();
                    } else {
                        if (j == size) {
                            checkLowerRightCorner();
                        } else {
                            checkBottom(i, j);
                        }
                    }
                }
            }
            if (j == 0 && i != 0 && i != size) {
                checkLeft(i, j);
            }
            if (j == size && i != 0 && i != size) {
                checkRight(i, j);
            }
            if (i != 0 && i != size && j != 0 && j != size) {
                checkCell(i, j);
            }
            if (table[i][j].isHappy()) {
                numberOfHappy++;
            } else {
                numberOfUnhappy++;
            }
        }
    }

    private void checkHappinessForAll(){
        numberOfHappy = 0;
        numberOfUnhappy = 0;
        for (int i = 0; i <= size; i++){
            for(int j = 0; j <= size; j++) {
                checkHappinessForCell(i,j);
            }
        }
    }

    private void checkUpperLeftCorner(){
        int happinessLevel = 0;
        if (table[1][0] != null && table[0][0].getColor() == table[1][0].getColor()){
            happinessLevel++;
        }
        if (table[0][1] != null && table[0][0].getColor() == table[0][1].getColor()){
            happinessLevel++;
        }
        if (table[1][1] != null && table[0][0].getColor() == table[1][1].getColor()){
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[0][0].setHappy(true);
        }else{
            table[0][0].setHappy(false);
            unhappyPositions.add(new Positions(0, 0));
        }
    }

    private void checkUpperRightCorner(){
        int happinessLevel = 0;
        if (table[1][size] != null && table[0][size].getColor() == table[1][size].getColor()) {
            happinessLevel++;
        }
        if (table[0][size - 1] != null && table[0][size].getColor() == table[0][size - 1].getColor()) {
            happinessLevel++;
        }
        if (table[1][size - 1] != null && table[0][size].getColor() == table[1][size - 1].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[0][size].setHappy(true);
        }else{
            table[0][size].setHappy(false);
            unhappyPositions.add(new Positions(0, size));
        }
    }

    private void checkLowerLeftCorner(){
        int happinessLevel = 0;
        if (table[size][1] != null && table[size][0].getColor() == table[size][1].getColor()){
            happinessLevel++;
        }
        if (table[size - 1][ 0] != null && table[size][0].getColor() == table[size - 1][ 0].getColor()){
            happinessLevel++;
        }
        if (table[size - 1][1] != null && table[size][0].getColor() == table[size - 1][1].getColor()){
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[size][0].setHappy(true);
        }else{
            table[size][0].setHappy(false);
            unhappyPositions.add(new Positions(size, 0));
        }
    }

    private void checkLowerRightCorner(){
        int happinessLevel = 0;
        if (table[size - 1][size] != null && table[size][size].getColor() == table[size - 1][size].getColor()) {
            happinessLevel++;
        }
        if (table[size][size - 1] != null && table[size][size].getColor() == table[size][size - 1].getColor()) {
            happinessLevel++;
        }
        if (table[size - 1][size - 1] != null && table[size][size].getColor() == table[size - 1][size - 1].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[size][size].setHappy(true);
        }else{
            table[size][size].setHappy(false);
            unhappyPositions.add(new Positions(size, size));
        }
    }

    private void checkTop(int i, int j){
        int happinessLevel = 0;
        if (table[i + 1][j] != null && table[i][j].getColor() == table[i + 1][j].getColor()) {
            happinessLevel++;
        }
        if (table[i][j + 1] != null && table[i][j].getColor() == table[i][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j + 1] != null && table[i][j].getColor() == table[i + 1][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j - 1] != null && table[i][j].getColor() == table[i + 1][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i][j - 1] != null && table[i][j].getColor() == table[i][j - 1].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[i][j].setHappy(true);
        }else{
            table[i][j].setHappy(false);
            unhappyPositions.add(new Positions(i, j));
        }
    }

    private void checkBottom(int i, int j){
        int happinessLevel = 0;
        if (table[i - 1][j] != null && table[i][j].getColor() == table[i - 1][j].getColor()) {
            happinessLevel++;
        }
        if (table[i][j + 1] != null && table[i][j].getColor() == table[i][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i - 1][j - 1] != null && table[i][j].getColor() == table[i - 1][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i - 1][j + 1] != null && table[i][j].getColor() == table[i - 1][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i][j - 1] != null && table[i][j].getColor() == table[i][j - 1].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[i][j].setHappy(true);
        }else{
            table[i][j].setHappy(false);
            unhappyPositions.add(new Positions(i, j));
        }
    }

    private void checkRight(int i, int j){
        int happinessLevel = 0;
        if (table[i - 1][j] != null && table[i][j].getColor() == table[i - 1][j].getColor()) {
            happinessLevel++;
        }
        if (table[i - 1][j - 1] != null && table[i][j].getColor() == table[i - 1][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i][j - 1] != null && table[i][j].getColor() == table[i][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j - 1] != null && table[i][j].getColor() == table[i + 1][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j] != null && table[i][j].getColor() == table[i + 1][j].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[i][j].setHappy(true);
        }else{
            table[i][j].setHappy(false);
            unhappyPositions.add(new Positions(i, j));
        }
    }

    private void checkLeft(int i, int j){
        int happinessLevel = 0;
        if (table[i - 1][j] != null && table[i][j].getColor() == table[i - 1][j].getColor()) {
            happinessLevel++;
        }
        if (table[i - 1][j + 1] != null && table[i][j].getColor() == table[i - 1][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i][j + 1] != null && table[i][j].getColor() == table[i][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j + 1] != null && table[i][j].getColor() == table[i + 1][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j] != null && table[i][j].getColor() == table[i + 1][j].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[i][j].setHappy(true);
        }else{
            table[i][j].setHappy(false);
            unhappyPositions.add(new Positions(i, j));
        }
    }

    private void checkCell(int i, int j){
        int happinessLevel = 0;
        if (table[i - 1][j - 1] != null && table[i][j].getColor() == table[i - 1][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i - 1][j] != null && table[i][j].getColor() == table[i - 1][j].getColor()) {
            happinessLevel++;
        }
        if (table[i - 1][j + 1] != null && table[i][j].getColor() == table[i - 1][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i][j + 1] != null && table[i][j].getColor() == table[i][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j + 1] != null && table[i][j].getColor() == table[i + 1][j + 1].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j] != null && table[i][j].getColor() == table[i + 1][j].getColor()) {
            happinessLevel++;
        }
        if (table[i + 1][j - 1] != null && table[i][j].getColor() == table[i + 1][j - 1].getColor()) {
            happinessLevel++;
        }
        if (table[i][j - 1] != null && table[i][j].getColor() == table[i][j - 1].getColor()) {
            happinessLevel++;
        }
        if(happinessLevel >= numberOfNeighbors){
            table[i][j].setHappy(true);
        }else{
            table[i][j].setHappy(false);
            unhappyPositions.add(new Positions(i, j));
        }
    }

    public void simulate(int max, int step) throws IOException {
        printTable(new File("C:\\Users\\igor-\\IdeaProjects\\modelSchelling\\" + "k0.xlsx"));

        int k = 0;
        while(unhappyPositions.size() > 0 && k <= max){
            Positions unhappyPosition = unhappyPositions.get((int) (Math.random() * unhappyPositions.size()));
            Positions emptyPositions = emptySpace.get((int) (Math.random() * emptySpace.size()));
            table[emptyPositions.getRow()][emptyPositions.getColumn()] = table[unhappyPosition.getRow()][unhappyPosition.getColumn()];
            table[unhappyPosition.getRow()][unhappyPosition.getColumn()] = null;

            unhappyPositions.clear();
            emptySpace.remove(emptyPositions);
            emptySpace.add(unhappyPosition);
            checkHappinessForAll();

            k++;
            if(k % step == 0){
                printTable(new File("C:\\Users\\igor-\\IdeaProjects\\modelSchelling\\k" + k + ".xlsx"));
            }
        }

        printTable(new File("C:\\Users\\igor-\\IdeaProjects\\modelSchelling\\k" + k + ".xlsx"));
    }

}
