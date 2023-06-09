package io.gamekeep;

import com.formdev.flatlaf.FlatLightLaf;
import io.gamekeep.components.Blockchain;
import io.gamekeep.ui.DashboardFrame;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        Security.addProvider(new BouncyCastleProvider());

        Blockchain.setFilePath("blockchain/data.bin");
        Blockchain.load();

//        // Sample of how shit will go down
//
//        // Will create new key pairs when initiated
//        // and if no existing key pairs found
//        Seller steam = new Seller("steam");
//
//        Transaction txn = new Transaction(
//                UUID.randomUUID().toString(),
//                "john.smith",
//                "god-of-war",
//                "sony-interactive",
//                UUID.randomUUID().toString(),
//                new BigDecimal("59.9"),
//                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
//                steam.getSellerId()
//        );
//
//        // Still iffy about the signing part, draft for now is like this
//        try {
//            steam.digitallySign(txn);
//            System.out.println(txn.verifySignature(steam.getKeyPair().getPublic()));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        // We don't add transactions to blocks directly, the blockchain will have a
//        // "current block" handle that will fill it to the MAX_TRANSACTIONS value,
//        // and it will automatically push it to the "blockchain" when its "full".
//        Blockchain.pushTransaction(txn);
//        System.out.println(Blockchain.getBlockchain().toString());

        DashboardFrame dashboardFrame = new DashboardFrame("Dashboard");
        dashboardFrame.setVisible(true);
    }
}
