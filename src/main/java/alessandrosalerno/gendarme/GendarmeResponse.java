package alessandrosalerno.gendarme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GendarmeResponse {
    private final GendarmeTable table;
    private String message;

    public GendarmeResponse(GendarmeTable table, String message) {
        this.table = table;
        this.message = message;
    }

    public GendarmeResponse(String message, String... tableColumns) {
        this.table = new GendarmeTable(tableColumns);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public GendarmeResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public GendarmeResponse addTableRow(Object... cells) {
        this.table.addRow(cells);
        return this;
    }

    public GendarmeTable getTable() {
        return this.table;
    }
}
