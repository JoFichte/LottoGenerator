package LottoGeneratorFutures;

import java.util.List;
import java.util.TreeSet;

public class Validator {

    public boolean allLottoTicketsAreEqual(List<TreeSet<Integer>> lottoTickets) {
        return lottoTickets.stream().allMatch(element -> element.equals(lottoTickets.get(0)));
    }

}
