package org.mthree.flooringmastery.ui;

import org.springframework.stereotype.Component;

import java.time.LocalDate;


public interface UserIO {

    //print
    void print(String msg);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    String readString(String prompt);

    LocalDate readLocalDate(String prompt);
}
