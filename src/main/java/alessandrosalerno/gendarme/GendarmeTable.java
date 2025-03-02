package alessandrosalerno.gendarme;

import java.util.ArrayList;
import java.util.HashMap;

public class GendarmeTable {
    private final ArrayList<HashMap<String, Object>> table;
    private final String[] columns;

    public GendarmeTable(String... columns) {
        this.columns = columns;
        this.table = new ArrayList<>();
    }

    public GendarmeTable addRow(Object... cells) {
        if (cells.length != this.columns.length) {
            throw new IllegalArgumentException("Expected " + this.columns.length + " cells, got " + cells.length);
        }

        HashMap<String, Object> row = new HashMap<>();

        for (int i = 0; i < cells.length; i++) {
            row.put(this.columns[i], cells[i]);
        }

        this.table.add(row);
        return this;
    }

    public int getColumnCount() {
        return this.columns.length;
    }

    public String[] getColumns() {
        return this.columns;
    }

    public int getRowCount() {
        return this.table.size();
    }

    public Object getCell(String column, int row) {
        return this.table.get(row).get(column);
    }

    @Override
    public String toString() {
        if (0 == this.getRowCount()) {
            return "";
        }

        int[] columnWidth = new int[this.columns.length];

        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.columns.length; j++) {
                Object cell = this.getCell(this.columns[j], i);
                int len = cell.toString().length();
                columnWidth[j] = Math.max(columnWidth[j], len);
            }
        }

        for (int i = 0; i < this.columns.length; i++) {
            columnWidth[i] = Math.max(columnWidth[i], this.columns[i].length());
        }

        StringBuilder fmt = new StringBuilder();
        fmt.append(this.makeRowSeparator(columnWidth));
        fmt.append(this.makeHeading(columnWidth));
        fmt.append(this.makeRowSeparator(columnWidth));

        for (int row = 0; row < this.getRowCount(); row++) {
            fmt.append(this.makeRow(columnWidth, row));
        }

        fmt.append(this.makeRowSeparator(columnWidth));
        return fmt.toString();
    }

    private String makeRowSeparator(int[] columnWidth) {
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < columnWidth.length; i++) {
            ret.append("+").append("-".repeat(columnWidth[i] + 2));
        }

        return ret + "+\n";
    }

    private String makeRow(int[] columnWidth, int row) {
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < this.columns.length; i++) {
            Object cellObj = this.getCell(this.columns[i], row);
            String cell = cellObj.toString();
            int rem = columnWidth[i] - cell.length();
            if (cell.matches("-?\\d+(\\.\\d+)?")) {
                ret.append("| ").append(" ".repeat(rem)).append(cell).append(" ");
            } else {
                ret.append("| ").append(cell).append(" ".repeat(rem)).append(" ");
            }

        }

        return ret + "|\n";
    }

    private String makeHeading(int[] columnWidth) {
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < this.columns.length; i++) {
            String cell = this.columns[i];
            int rem = columnWidth[i] - cell.length();
            int leftRem = rem / 2;
            int rightRem = rem - leftRem;
            ret.append("| ").append(" ".repeat(leftRem)).append(cell).append(" ".repeat(rightRem)).append(" ");
        }

        return ret + "|\n";
    }
}
