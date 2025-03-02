package alessandrosalerno.gendarme;

import alessandrosalerno.gendarme.exceptions.GendarmeInvalidSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class GendarmeParser {
    private int index;
    private final String command;

    public GendarmeParser(String command) {
        this.command = command;
    }

    public Object[] parse() throws GendarmeInvalidSyntaxException {
        List<Object> objs = new ArrayList<>();

        for (; this.index < this.command.length(); this.advance()) {
            objs.add(this.parseOne());
        }

        return objs.toArray();
    }

    private Object parseOne() throws GendarmeInvalidSyntaxException {
        String token = this.collectOne();

        if (token.matches("-?\\d+(\\.\\d+)?")) {
            Double value = Double.parseDouble(token);

            if (0 == value % 1 && !token.contains(".")) {
                return value.longValue();
            }

            return value;
        }

        return token;
    }

    private String collectOne() throws GendarmeInvalidSyntaxException {
        StringBuilder builder = new StringBuilder();

        // Ignore leading spaces
        for (; this.index < this.command.length() && ' ' == this.current(); this.advance());

        for (; this.index < this.command.length(); this.advance()) {
            if (' ' == this.current()) {
                break;
            }

            if ('"' == this.current()) {
                if (!builder.isEmpty()) {
                    throw new GendarmeInvalidSyntaxException("Unexpected string", this.index);
                }

                return this.collectString();
            }

            builder.append(this.current());
        }

        return builder.toString();
    }

    private String collectString() throws GendarmeInvalidSyntaxException {
        this.advance();
        StringBuilder builder = new StringBuilder();
        for (; this.index < this.command.length(); this.advance()) {
            if ('"' == this.current()) {
                return builder.toString();
            }

            builder.append(this.current());
        }

        throw new GendarmeInvalidSyntaxException("Unterminated string", this.index);
    }

    private void advance(int amount) {
        this.index += amount;
    }

    private void advance() {
        this.advance(1);
    }

    private char current() {
        return this.command.charAt(this.index);
    }
}
