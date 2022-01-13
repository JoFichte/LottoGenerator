import java.util.*;
import java.util.concurrent.CountDownLatch;

public class LottoGenerator {

    private List<Set<Integer>> partialResults = Collections.synchronizedList(new ArrayList<>());

    public List<Set<Integer>> generateLottoSchein() throws InterruptedException {
        int numberOfWorkers = Integer.parseInt(System.getenv("AnzahlGleicherScheine"));

        CountDownLatch countDownLatch = new CountDownLatch(numberOfWorkers);

        for (int i = 0; i < numberOfWorkers; i++) {
            Thread worker = new Thread(() -> {
                partialResults.add(generateLottoNumbers());
                countDownLatch.countDown();
            });

            worker.start();
        }

        countDownLatch.await();
        return partialResults;
    }

    private Set<Integer> generateLottoNumbers() {
        Set<Integer> listWithGeneratedLottoNumbers = new TreeSet<>();
        Random rn = new Random();
        int lottoSize = Integer.parseInt(System.getenv("Scheingroesse"));
        int maxNumbers = Integer.parseInt(System.getenv("MaxNummern"));

        while (listWithGeneratedLottoNumbers.size() < lottoSize) {
            listWithGeneratedLottoNumbers.add(rn.nextInt(maxNumbers) + 1);
        }

        return listWithGeneratedLottoNumbers;
    }

}
