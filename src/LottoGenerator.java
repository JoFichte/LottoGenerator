import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class LottoGenerator {

    private CyclicBarrier cyclicBarrier;
    private List<TreeSet<Integer>> partialResults = Collections.synchronizedList(new ArrayList<>());
    private static int cycleCounter = 0;

    private void runThreads(int numberOfWorkers) {
        cyclicBarrier = new CyclicBarrier(numberOfWorkers, new ValidatorThread());
        //System.out.println("Creating " + numberOfWorkers + " worker threads to generate lotto numbers");
        for (int i = 0; i < numberOfWorkers; i++) {
            Thread worker = new Thread(new NumberGeneratorThread());
            worker.setName("Thread " + i);
            worker.start();
        }
    }

    class NumberGeneratorThread implements Runnable {

        @Override
        public void run() {
            String thisThreadName =  Thread.currentThread().getName();
            TreeSet<Integer> partialResult = new TreeSet<>();

            partialResult = generateLottoNumbers();
            partialResults.add(partialResult);

            try {
                //System.out.println(thisThreadName + " waiting for other threads to reach barrier.");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private TreeSet<Integer> generateLottoNumbers() {
            TreeSet<Integer> listWithGeneratedLottoNumbers = new TreeSet<>();
            Random rn = new Random();

            while (listWithGeneratedLottoNumbers.size() < 2) {
                int randomNumberBetween1And49 = rn.nextInt(49) + 1;
                listWithGeneratedLottoNumbers.add(randomNumberBetween1And49);
            }
            return listWithGeneratedLottoNumbers;
        }
    }

    class ValidatorThread implements Runnable {

        private boolean allLottoNumbersAreEqual;
        @Override
        public void run() {
            String thisThreadName =  Thread.currentThread().getName();
            //System.out.println(thisThreadName + " : Analysing and printing generated numbers");

            allLottoNumbersAreEqual = partialResults.stream().allMatch(element -> element.equals(partialResults.get(0)));
            System.out.println("All numbers are equal: " + allLottoNumbersAreEqual);
            System.out.println("Cycle counter: " + cycleCounter++ + System.lineSeparator());

            if (!allLottoNumbersAreEqual) {
                LottoGenerator run = new LottoGenerator();
                run.runThreads(Integer.parseInt(System.getenv("NUMBER_OF_THREADS")));
            }

            if (allLottoNumbersAreEqual) {
                System.out.println("Match found:");
                partialResults.stream().forEach(System.out::println);
            }
        }
    }

    public static void main(String[] args) {
        LottoGenerator run = new LottoGenerator();
        run.runThreads(Integer.parseInt(System.getenv("NUMBER_OF_THREADS")));
    }
}
