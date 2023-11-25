import java.io.*;
import java.util.concurrent.*;

public class Main {
    private static final Object lock = new Object();
    public static void main(String[] args) {

        String filePath = "input.txt";
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Callable<Integer> task = new ReadLineFromFile(line);
                FutureTask<Integer> futureTask = new FutureTask<>(task) {
                    @Override
                    protected void done() {
                        try {
                            int charCount = get();
                            updateFile(filePath, charCount);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                };
                executor.submit(futureTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void updateFile(String filePath, int result) {
        synchronized (lock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                String updatedLine = ""+result;
                writer.write(updatedLine);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
