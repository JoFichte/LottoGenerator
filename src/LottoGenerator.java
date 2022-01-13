import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class LottoGenerator {

    private CyclicBarrier cyclicBarrier;
    private List<List<Integer>> partialResults = Collections.synchronizedList(new ArrayList<>());
    private boolean allLottoNumbersAreEqual;
    final static int numberOfThreads = 2;
    private static int cycleCounter = 0;

    private void runThreads(int numberOfWorkers) {
        cyclicBarrier = new CyclicBarrier(numberOfWorkers, new ValidatorThread());
        System.out.println("Creating " + numberOfWorkers + " worker threads to generate lotto numbers");
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
            List<Integer> partialResult = new ArrayList<>();

            partialResult = generateLottoNumbers();
            partialResults.add(partialResult);

            try {
                System.out.println(thisThreadName + " waiting for other threads to reach barrier.");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    class ValidatorThread implements Runnable {
        @Override
        public void run() {
            String thisThreadName =  Thread.currentThread().getName();
            System.out.println(thisThreadName + " : Analysing and printing generated numbers");

            partialResults.stream().forEach(System.out::println);

            allLottoNumbersAreEqual = partialResults.stream().allMatch(element -> element.contains(partialResults.get(0)));

            System.out.println("Cycle counter: " + cycleCounter++ + System.lineSeparator());

            System.out.println("All numbers are equal: " + allLottoNumbersAreEqual);

            if (!allLottoNumbersAreEqual) {
                LottoGenerator run = new LottoGenerator();
                run.runThreads(numberOfThreads);
            }

        }
    }

    private List<Integer> generateLottoNumbers() {
        List<Integer> listWithGeneratedLottoNumbers = new ArrayList<>();
        Random rn = new Random();

        while (listWithGeneratedLottoNumbers.size() < 7) {
            int randomNumberBetween1And49 = rn.nextInt(49);
            if (randomNumberBetween1And49 <= 0) {
                randomNumberBetween1And49 = 1;
            }

            if (!listWithGeneratedLottoNumbers.contains(randomNumberBetween1And49)) {
                listWithGeneratedLottoNumbers.add(randomNumberBetween1And49);
            }
        }

        listWithGeneratedLottoNumbers.sort(Comparator.naturalOrder());

        return listWithGeneratedLottoNumbers;
    }

    public static void main(String[] args) {
        LottoGenerator run = new LottoGenerator();
        run.runThreads(numberOfThreads);

    }

}
