package ui;

import java.util.Scanner;

/**
 * ENCAPSULATION: wraps a Scanner and exposes only type-safe, validated reads.
 * The rest of the application never reads from System.in directly.
 */
public final class InputReader {

    private final Scanner sc;

    public InputReader() { this.sc = new Scanner(System.in); }

    /** Reads a raw trimmed line. */
    public String readLine() { return sc.nextLine().trim(); }

    /**
     * Prompts until the user enters an integer in [min, max].
     */
    public int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                ConsoleUI.error("Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                ConsoleUI.error("Invalid input – enter a whole number.");
            }
        }
    }

    /**
     * Reads a 1-based list index and returns it (1 … size).
     */
    public int readListIndex(int size) {
        ConsoleUI.prompt("Enter number (1 to " + size + ")");
        return readInt(1, size);
    }

    /**
     * Prompts until the user enters a positive double ≤ maxValue.
     */
    public double readPositiveDouble(double maxValue) {
        while (true) {
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val > 0 && val <= maxValue) return val;
                ConsoleUI.error("Value must be > 0 and ≤ " + maxValue + ".");
            } catch (NumberFormatException e) {
                ConsoleUI.error("Enter a valid decimal number (e.g. 9.99).");
            }
        }
    }

    /** Prompts until a non-empty string is entered. */
    public String readNonBlank(String fieldName) {
        while (true) {
            String val = sc.nextLine().trim();
            if (!val.isEmpty()) return val;
            ConsoleUI.error(fieldName + " cannot be empty.");
        }
    }

    /** Reads yes/no – returns true for "yes". */
    public boolean readConfirm() {
        return sc.nextLine().trim().equalsIgnoreCase("yes");
    }
}
