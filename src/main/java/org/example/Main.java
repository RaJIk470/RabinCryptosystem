package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("file");
        System.out.println(Arrays.toString(fileInputStream.readAllBytes()));

        fileInputStream = new FileInputStream("file_result_result");
        System.out.println(Arrays.toString(fileInputStream.readAllBytes()));
    }
}

