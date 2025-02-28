package alessandrosalerno.gendarme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GendarmeCommandResult {
    private final List<Map<String, Object>> table;
    private String message;

    public GendarmeCommandResult(List<Map<String, Object>> table, String message) {
        this.table = table;
        this.message = message;
    }

    public GendarmeCommandResult(String message) {
        this.table = new ArrayList<>();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<Map<String, Object>> getTable() {
        return this.table;
    }

    public GendarmeCommandResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public GendarmeCommandResult addTableRow(Map<String, Object> row) {
        this.table.add(row);
        return this;
    }
}
