import java.util.List;
import java.util.Set;

public class LottoPruefer {

    public boolean checkIsAlleScheineGleich(List<Set<Integer>> lottoScheine) {
        return lottoScheine.parallelStream().allMatch(integers -> integers.equals(lottoScheine.get(0)));
    }

}
