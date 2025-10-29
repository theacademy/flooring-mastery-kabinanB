//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {
    private final Scanner console;

    public UserIOConsoleImpl() {
        this.console = new Scanner(System.in);
    }

    public void print(String msg) {
        System.out.println(msg);
    }

    public int readInt(String prompt) {
        boolean invalidInput = true;
        int num = 0;

        while(invalidInput) {
            try {
                String stringValue = this.readString(prompt);
                num = Integer.parseInt(stringValue);
                invalidInput = false;
            } catch (NumberFormatException var5) {
                this.print("Input error. Please try again.");
            }
        }

        return num;
    }

    public int readInt(String prompt, int min, int max) {
        int result;
        do {
            result = this.readInt(prompt);
        } while(result < min || result > max);

        return result;
    }

    public String readString(String prompt) {
        System.out.println(prompt);
        return this.console.nextLine();
    }

    public LocalDate readLocalDate(String prompt) {
        LocalDate date = null;
        boolean inputValue = true;

        while(inputValue) {
            try {
                String stringValue = this.readString(prompt);
                date = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                inputValue = false;
            } catch (DateTimeParseException var5) {
                this.print("Input error. Please try again.");
            }
        }

        return date;
    }
}
