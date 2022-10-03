import com.application.service.store.IPurchaseService;
import com.application.service.store.PurchaseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PurchaseTest {


    @Test
    public void shouldReturnCorrectBrandBasedPurchases() {
        IPurchaseService purchaseService = new PurchaseService();

        purchaseService.getRevenueBrandBased();

    }
}
