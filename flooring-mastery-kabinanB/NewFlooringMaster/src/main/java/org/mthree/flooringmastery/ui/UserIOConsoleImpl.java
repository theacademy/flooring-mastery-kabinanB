package org.mthree.flooringmastery.ui;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class UserIOConsoleImpl implements UserIO{

    final private Scanner console = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public int readInt(String prompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                // print the message msgPrompt (ex: asking for the # of cats!)
                String stringValue = this.readString(prompt);
                // Get the input line, and try and parse
                num = Integer.parseInt(stringValue); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        do {
            result = readInt(prompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return console.nextLine();
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        LocalDate date = null;
        boolean inputValue = true;

        while(inputValue)
        {
            try
            {
                String stringValue = this.readString(prompt);
                //From String to date
                date = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("MM-dd-yyyy"));

                //Future only

                inputValue = false;



            } catch (DateTimeParseException e)
            {
                this.print("Input error. Please try again.");
            }
        }

        return date;
    }
}
