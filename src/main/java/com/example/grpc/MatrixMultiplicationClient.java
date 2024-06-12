package com.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Arrays;
import java.util.Scanner;

import com.example.grpc.MatrixMultiplicationProto.Matrix;
import com.example.grpc.MatrixMultiplicationProto.MatrixRequest;
import com.example.grpc.MatrixMultiplicationProto.MatrixResponse;

public class MatrixMultiplicationClient {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Witaj w aplikacji gRPC z serwisem mnożenia macierzy 🤯");
        System.out.println("/--------------------------------------\\");
        System.out.println("|                POMOC                 |");
        System.out.println("| 1.Podaj liczby po przecinku (double) |");
        System.out.println("|        np. 1.0, 2.0, 3.0, 4.0        |");
        System.out.println("|     2.Podaj ilość wierszy, np. 2     |");
        System.out.println("|     3.Podaj ilość kolumn, np. 2      |");
        System.out.println("|                                      |");
        System.out.println("|           Wynik przykładu:           |");
        System.out.println("|              [1.0, 2.0]              |");
        System.out.println("|              [3.0, 4.0]              |");
        System.out.println("\\--------------------------------------/");

//        ArrayList<Double> clientMatrixA = new ArrayList<Double>();
//        ArrayList<Double> clientMatrixB = new ArrayList<Double>();
//        int rowsA;
//        int colsA;
//
//        System.out.println("Podaj pierwszą macierzA: ");
//        String inputMacierzA = scan.nextLine();
//
//        // Krok 1: Podziel ciąg na części za pomocą przecinka jako separatora
//        String[] stringNumbersA = inputMacierzA.split(",");
//
//        // Krok 2: Utwórz tablicę double o odpowiedniej długości
//        double[] numbersA = new double[stringNumbersA.length];
//
//        // Krok 3: Usuń białe znaki i przekonwertuj na typ double
//        for (int i = 0; i < stringNumbersA.length; i++) {
//            numbersA[i] = Double.parseDouble(stringNumbersA[i].trim());
//        }
//
//        // Wyświetl wyniki (opcjonalnie)
//        for (double numberA : numbersA) {
//            System.out.println(numberA);
//        }
//
//        System.out.print("Podaj ilość wierszy: ");
//        rowsA = scan.nextInt();
//
//        System.out.print("Podaj ilość kolumn: ");
//        colsA = scan.nextInt();
//
//        int rowsB;
//        int colsB;
//
//        System.out.println("Podaj pierwszą macierzB: ");
//        String inputMacierzB = scan.nextLine();
//
//        // Krok 1: Podziel ciąg na części za pomocą przecinka jako separatora
//        String[] stringNumbersB = inputMacierzB.split(",");
//
//        // Krok 2: Utwórz tablicę double o odpowiedniej długości
//        double[] numbersB = new double[stringNumbersB.length];
//
//        // Krok 3: Usuń białe znaki i przekonwertuj na typ double
//        for (int i = 0; i < stringNumbersB.length; i++) {
//            numbersB[i] = Double.parseDouble(stringNumbersB[i].trim());
//        }
//
//        // Wyświetl wyniki (opcjonalnie)
//        for (double numberB : numbersB) {
//            System.out.println(numberB);
//        }
//
//        System.out.print("Podaj ilość wierszy: ");
//        rowsB = scan.nextInt();
//
//        System.out.print("Podaj ilość kolumn: ");
//        colsB = scan.nextInt();

        scan.close();

//        ==================================================
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        MatrixMultiplicationServiceGrpc.MatrixMultiplicationServiceBlockingStub stub =
                MatrixMultiplicationServiceGrpc.newBlockingStub(channel);

        Double[] clientMatrixA = {1.0, 2.0, 3.0, 4.0};
        Double[] clientMatrixB = {5.0, 6.0, 7.0, 8.0};

        Matrix matrixA = Matrix.newBuilder()
                .addAllElements(Arrays.asList(clientMatrixA))
                .setRows(2)
                .setCols(2)
                .build();

        Matrix matrixB = Matrix.newBuilder()
                .addAllElements(Arrays.asList(clientMatrixB))
                .setRows(2)
                .setCols(2)
                .build();

        MatrixRequest request = MatrixRequest.newBuilder()
                .setMatrixA(matrixA)
                .setMatrixB(matrixB)
                .build();

        MatrixResponse response = stub.multiply(request);

        Matrix resultMatrix = response.getResult();

        System.out.println("Wynik mnożenia macierzy: ");
        for (int i = 0; i < resultMatrix.getRows(); i++) {
            for (int j = 0; j < resultMatrix.getCols(); j++) {
                System.out.print(resultMatrix.getElements(i * resultMatrix.getCols() + j) + " ");
            }
            System.out.println();
        }

        channel.shutdown();
    }
}
