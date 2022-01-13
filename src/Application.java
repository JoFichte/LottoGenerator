import java.util.List;
import java.util.Set;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        List<Set<Integer>> lottoScheine;

        LottoPruefer pruefer = new LottoPruefer();
        do {
            LottoGenerator generator = new LottoGenerator();
            lottoScheine = generator.generateLottoSchein();
        } while(!pruefer.checkIsAlleScheineGleich(lottoScheine));

        lottoScheine.forEach(schein ->  {
            System.out.println("\n---------------");
            schein.forEach(lottoZahl-> System.out.print(lottoZahl + " "));
        });
    }

}
