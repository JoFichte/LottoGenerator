package LottoGeneratorFutures;

import java.util.*;
import java.util.concurrent.*;

public class LottoTicketGenerator {


    public List<TreeSet<Integer>> generator(Integer numberOfTasks) throws InterruptedException{

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<TreeSet<Integer>>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(() -> generateLottoNumbers());
        }

        List<Future<TreeSet<Integer>>> futures = executor.invokeAll(tasks);

        List<TreeSet<Integer>> lottoTickets = futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();

        executor.shutdown();

        return lottoTickets;
    }

    private TreeSet<Integer> generateLottoNumbers() {
        TreeSet<Integer> listWithGeneratedLottoNumbers = new TreeSet<>();
        Random rn = new Random();

        while (listWithGeneratedLottoNumbers.size() < 4) {
            int randomNumberBetween1And49 = rn.nextInt(49) + 1;
            listWithGeneratedLottoNumbers.add(randomNumberBetween1And49);
        }
        return listWithGeneratedLottoNumbers;
    }

}
