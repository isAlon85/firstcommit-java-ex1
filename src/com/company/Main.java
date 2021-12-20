package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    //Elegimos el separador del csv, en mi caso ;
    public static final String SEPARATOR = ";";

    public static void main(String[] args) {
        ArrayList<User> usersSaved;

        usersSaved = addUsersFromCSV("nombres.csv");
        System.out.println("Saved array data:\n" + usersSaved);
    }

    public static ArrayList<User> addUsersFromCSV(String file){
        ArrayList<User> users = new ArrayList<>();
        int processedLines = 0;
        int errorLines = 0;
        int actualLine = 1;

        BufferedReader readBuffer = null;
        try {
            // Abrir el .csv en buffer de lectura
            readBuffer = new BufferedReader(new FileReader(file));

            // Leer una línea del archivo
            String line = readBuffer.readLine();

            while (line != null) {
                // Separar la línea leída con el separador definido previamente
                String[] fields = line.split(SEPARATOR);
                if (Arrays.stream(fields).count() != 3 ||
                        fields[0].isBlank() || fields[1].isBlank() || fields[2].isBlank()) {
                    System.err.println("Line " + actualLine + " is wrong");
                    errorLines++;
                } else {
                    User user = new User(fields[0], fields[1], fields[2]);
                    // Filtar emails duplicados y líneas con campos en blanco
                    boolean duplicatedMail = false;
                    for (User value : users) {
                        if (Objects.equals(value.getEmail(), user.getEmail())) {
                            duplicatedMail = true;
                            break;
                        }
                    }
                    if (!validateMail(fields[0])) {
                        System.err.println("email " + fields[0] + " in line " + actualLine + " is not a valid email");
                        errorLines++;
                    } else if (duplicatedMail) {
                        System.err.println("email " + fields[0] + " in line " + actualLine + " is duplicated");
                        errorLines++;
                    } else {
                        users.add(user);
                        processedLines++;
                    }
                }

                // Volver a leer otra línea del fichero
                line = readBuffer.readLine();
                actualLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cierro el buffer de lectura
            if (readBuffer != null) {
                try {
                    readBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\nProcessed lines :" + processedLines);
        System.out.println("Error lines :" + errorLines + "\n");
        return users;
    }

    public static boolean validateMail(String email) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

}