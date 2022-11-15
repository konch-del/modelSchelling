import java.util.Objects;

public class Positions {

    private int row;
    private int column;

    public Positions(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Positions positions = (Positions) o;
        return row == positions.row && column == positions.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "[" + row + "," + column + "]";
    }
}
