/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixjavathread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giulia
 */
public class MatrixJavaThread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<FutureTask<Integer>> future = new ArrayList<>();
        ExecutorService exec = Executors.newFixedThreadPool(3);
        int[][] matrix = new int[3][5];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = j + i;
            }
        }
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }

        for (int i = 0; i < matrix.length; i++) {
            future.add(new FutureTask(new InnerThread(matrix[i])));
            exec.execute(future.get(i));
        }

        exec.shutdown();
        try {
            int result = 0;
            for (int i = 0; i < matrix.length; i++) {
                if (result < future.get(i).get()) {
                    result = future.get(i).get();
                }
            }
            System.out.println("Il massimo è: " + result);
        } catch (ExecutionException ex) {
            System.out.println("Cozze e mongole");
        } catch (InterruptedException ex) {
            System.out.println("Cozze e mongole");
        }
    }

    static class InnerThread implements Callable<Integer> {

        private int max;
        private final int[] raw;

        public InnerThread(int[] raw) {
            this.max = 0;
            this.raw = raw;
        }

        @Override
        public Integer call() throws Exception {
            for (int i = 0; i < this.raw.length; i++) {
                if (this.raw[i] > this.max) {
                    this.max = this.raw[i];
                }
            }
            return this.max;
        }

    }
}
