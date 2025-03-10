package com.company;


/**
 * @author seank
 * This Java application allows for the experimentation with Threads and Parallel Programming
 */

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.DoubleStream;

public class Main {

    private static double sum1;
    private static double sum2;
    private static double sum3;
    private static double sum4;

    public static void main(String[] args) throws InterruptedException {

        // The variables for recording the total of reciprocals in the large array
        // for the SEQUENTIAL, PARALLEL, QUADRUPLE, and STREAM approaches
        double grandTotalSeq = 0.0;
        double grandTotalPar = 0.0;
        double grandTotalQuad = 0.0;
        double grandTotalStream = 0.0;
        double grandToTalUserDefined =0.0;


        int rnNumber;

        // Set up our array to the size that suits your system
        // Here it is 100,000,000 - 1 hundred million
        int arraySize = 100000000;
        double[] testArray = new double[arraySize];

        Random rn = new Random();

        // Fill our array with random numbers
        for (int i = 0; i < testArray.length; i++) {
            rnNumber = rn.nextInt(1000000 - 100000 + 1) + 100000;
            testArray[i] = rnNumber;
        }


        grandTotalSeq = arraySumSequential(testArray);
        grandTotalPar = arraySumParallel(testArray);
        grandTotalQuad = arraySumQuadruple(testArray);
        grandTotalStream = arraySumStream(testArray);
        grandToTalUserDefined = userChoice(testArray,16);

        //jump a line on print
        System.out.println();

        //call the method with menu options
        menu(testArray);
    }



    private static double arraySumQuadruple(double[] arr) throws InterruptedException {

        // START our 'stopwatch' to record the duration of the PARALLEL calculation
        long startPoint = System.nanoTime();

        // Set the four independent array sum variables
        //sum initialization
        sum1 = 0.0; sum2 = 0.0; sum3 = 0.0; sum4 = 0.0;


        // Create a 'for...loop' to calculate the sum of the reciprocals in
        // the top 1/4 of the array (from zero to first quarter)
        //create a thread to run loop
        Thread thread1 = new Thread( ()-> {
            for (int i = 0; i < arr.length / 4; i++) {
                sum1 += 1 / arr[i];
            }
        });

        //start thread
        thread1.start();

        // Create a 'for...loop' to calculate the sum of the reciprocals in
        // 2/4 of the array (from first to second quarter)
        //create a thread to run loop
        Thread thread2 = new Thread( ()-> {
                for (int i = arr.length / 4; i < arr.length / 4 * 2; i++) {
                sum2 += 1 / arr[i];
            }
        });

        //start thread
        thread2.start();

        // Create a 'for...loop' to calculate the sum of the reciprocals in
        // 3/4 of the array (from second to third quarter)
        //create a thread to run loop
        Thread thread3 = new Thread( ()-> {
            for (int i = arr.length / 4 * 2; i < arr.length / 4 * 3; i++) {
                sum3 += 1 / arr[i];
            }
        });

        //start thread 3
        thread3.start();

        // Create a 'for...loop' to calculate the sum of the reciprocals in
        // the BOTTOM half of the array (from third to full size
       // create a thread to run loop
            Thread thread4 = new Thread( ()-> {
                for (int i = arr.length / 4 * 3; i < arr.length; i++) {
                    sum4 += 1 / arr[i];
                }
            });
        //start thread 4
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();


        // Calculate the total sum from the result of each for...loop
        double finalSum = sum1 + sum2 + sum3 + sum4;

        // STOP our 'stopwatch' to record the duration of the calculation
        long nanoRunTime = System.nanoTime() - startPoint;

        // Use the method to print the results for the SEQUENTIAL calculation
        printOutcome("QuadrupleL", nanoRunTime, finalSum);
        return finalSum;
    }

    private static double arraySumSequential(double[] arr) {

        // START our 'stopwatch' to record the duration of the PARALLEL calculation
        long startPoint = System.nanoTime();

        // Set the two independent array sum variables (top half and bottom half)
        // to zero
        sum1 = 0.0; sum2 = 0.0;

        // Create a 'for...loop' to calculate the sum of the reciprocals in
        // the top HALF of the array
        for (int i = 0; i < arr.length/2; i++) {
            sum1 += 1 / arr[i];        }

        // Create a 'for...loop' to calculate the sum of the reciprocals in
        // the BOTTOM half of the array
        for (int i = arr.length / 2; i < arr.length ; i++) {
            sum2 += 1 / arr[i];
        }


        // Calculate the total sum from the result of each for...loop
        double finalSum = sum1 + sum2;

        // STOP our 'stopwatch' to record the duration of the calculation
        long nanoRunTime = System.nanoTime() - startPoint;

        // Use the method to print the results for the SEQUENTIAL calculation
        printOutcome("SEQUENTIAL", nanoRunTime, finalSum);
        return finalSum;
    }

    private static double arraySumParallel(double[] arr) throws InterruptedException {

        // START our 'stopwatch' to record the duration of the PARALLEL calculation
        long startPoint = System.nanoTime();

        // Set the two independent array sum variables (top half and bottom half)
        // to zero
        sum1 = 0.0; sum2 = 0.0;

        // Create a thread to calculate the sum of the reciprocals
        // in the TOP half of the array - start the thread
        Thread topHalfOfArray = new Thread(
                () -> {
                    for (int i = 0; i < arr.length / 2; i++) {
                        sum1 += 1 / arr[i];
                    }
                });
        topHalfOfArray.start();

        // Create a thread to calculate the sum of the reciprocals
        // in the BOTTOM half of the array - start the thread
        Thread bottomHalfOfArray = new Thread(
                () -> {
                    for (int i = arr.length / 2; i < arr.length; i++) {
                        sum2 += 1 / arr[i];
                    }
                });
        bottomHalfOfArray.start();

        // Ensure that the threads complete
        topHalfOfArray.join();
        bottomHalfOfArray.join();

        // Calculate the total sum from the result of each thread
        double finalSum = sum1 + sum2;

        // STOP our 'stopwatch' to record the duration of the calculation
        long nanoRunTime = System.nanoTime() - startPoint;

        // Use the method to print the results for the PARALLEL calculation
        printOutcome("PARALLEL", nanoRunTime, finalSum);
        return finalSum;
    }


    private static double arraySumStream(double[] arr) {

          sum1 = 0.0;

       // START our 'stopwatch' to record the duration of the PARALLEL calculation
        long startPoint = System.nanoTime();

       //get final sum from stream by adding values from arr to sum1
        double finalSum = DoubleStream.of(arr).parallel().map(d-> 1/d).sum();

       // STOP our 'stopwatch' to record the duration of the calculation
       long nanoRunTime = System.nanoTime() - startPoint;


       printOutcome("STREAM", nanoRunTime, finalSum);

     return finalSum;

    }

    public static void menu(double[] testArray) throws InterruptedException {

       //scanner object to get user input
        Scanner sc =new Scanner(System.in);

        while(true){
            //print for user to select
            System.out.println(" select number of threads to run");
            System.out.println(" 0. To Exit");

            if(sc.nextInt() > 0){
                userChoice(testArray, sc.nextInt());
            }else if(sc.nextInt() == 0){
                System.exit(0);
            }else{
                System.out.println("oops! try again please.....");
            }

        }

    }

    public static double userChoice( double myArr[], int noOfThread) {

        double finalSum = 0.0;
        //forkJoin object to split tasks into smaller and independent sub-tasks into user input defined number
        ForkJoinPool forkJoinPool = new ForkJoinPool(noOfThread);

        // START our 'stopwatch' to record the duration of the PARALLEL calculation
        long startPoint = System.nanoTime();

        finalSum = (double) forkJoinPool.invoke(new Calculation(myArr));

        // STOP our 'stopwatch' to record the duration of the calculation
        long nanoRunTime = System.nanoTime() - startPoint;


        printOutcome("USER-PREDEFINED  ", nanoRunTime, finalSum);
        return finalSum ;
    }

    private static void printOutcome(String label, long runTime, double sum) {
        System.out.printf(" %s process runtime was %8.3f milliseconds with final sum as %8.5f \n", label, runTime / 1e6, sum);
    }
}