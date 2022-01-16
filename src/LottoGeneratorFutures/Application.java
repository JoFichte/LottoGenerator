package LottoGeneratorFutures;

import java.util.*;

public class Application {
    public static void main(String[] args) throws InterruptedException{
        List<TreeSet<Integer>> lottoTickets;
        LottoTicketGenerator lottoTicketGenerator = new LottoTicketGenerator();
        Validator validator = new Validator();

        do {
            lottoTickets = lottoTicketGenerator.generator(Integer.parseInt(System.getenv("NUMBER_OF_THREADS")));
        }
        while (!validator.allLottoTicketsAreEqual(lottoTickets));

        System.out.println("All lotto tickets are equal!");

        lottoTickets.forEach(System.out::println);

    }
}
