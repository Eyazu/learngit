package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {
    private final static int FILE_QUEUE_SIZE = 10;
    private final static int SEARCH_THREAD = 100;
    private final static File DUMMY = new File("");
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)){
            System.out.println("Enter the directory");
            String directory = in.nextLine();
            System.out.println("Enter the keyword");
            String keyword = in.nextLine();

            Runnable enumrator = () -> {

                enumrate(new File(directory));
                try {
                    queue.put(DUMMY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
            new Thread(enumrator).start();
            for(int i = 0; i < SEARCH_THREAD; ++i) {
                Runnable searcher = () -> {
                    boolean done = true;
                    while(done) {
                        try {
                            File file = queue.take();
                            if(file == DUMMY) {
                                queue.put(file);
                                done = false;
                            } else {
                                search(file, keyword);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                new Thread(searcher).start();
            }
        }
    }

    public static void enumrate(File directory) {
        File[] files = directory.listFiles();
        for(File file: files) {
            if(file.isDirectory()) enumrate(file);
            else {
                try {
                    queue.put(file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void search(File file, String word) {

        try {
            Scanner in = new Scanner(file, "UTF-8");
            boolean found = false;
            int lineNumber = 0;
            while(in.hasNext()) {
                String line = in.nextLine();
                if(line.contains(word)) {
                    System.out.printf("%s : %d : %s %n", file.getPath(), ++lineNumber, line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
