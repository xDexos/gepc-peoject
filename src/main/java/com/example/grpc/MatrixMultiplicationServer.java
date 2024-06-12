package com.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.stream.DoubleStream;
import java.util.stream.Collectors;

import com.example.grpc.MatrixMultiplicationProto.Matrix;
import com.example.grpc.MatrixMultiplicationProto.MatrixRequest;
import com.example.grpc.MatrixMultiplicationProto.MatrixResponse;

public class MatrixMultiplicationServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new MatrixMultiplicationServiceImpl())
                .build();

        server.start();
        System.out.println("[SERVER] Serwer wystartował na porcie 8080");
        server.awaitTermination();
    }
}

class MatrixMultiplicationServiceImpl extends MatrixMultiplicationServiceGrpc.MatrixMultiplicationServiceImplBase {
    @Override
    public void multiply(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        Matrix matrixA = request.getMatrixA();
        Matrix matrixB = request.getMatrixB();

        if (matrixA.getCols() != matrixB.getRows()) {
            responseObserver.onError(new IllegalArgumentException("Ilość kolumn w MacierzA musi być taka sama jak ilość wierszy w MacierzB"));
            return;
        }

        int rowsA = matrixA.getRows();
        int colsA = matrixA.getCols();
        int colsB = matrixB.getCols();
        double[] resultElements = new double[rowsA * colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    resultElements[i * colsB + j] += matrixA.getElements(i * colsA + k) * matrixB.getElements(k * colsB + j);
                }
            }
        }

        Matrix resultMatrix = Matrix.newBuilder()
                .addAllElements(DoubleStream.of(resultElements).boxed().collect(Collectors.toList()))
                .setRows(rowsA)
                .setCols(colsB)
                .build();

        MatrixResponse response = MatrixResponse.newBuilder()
                .setResult(resultMatrix)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
