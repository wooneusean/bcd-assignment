package io.gamekeep;

import com.github.javafaker.Faker;
import io.gamekeep.components.Blockchain;
import io.gamekeep.components.Transaction;
import io.gamekeep.components.User;
import io.gamekeep.crypto.Encryptor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TestMain {
    // Run this once to populate the blockchain and keys
    // then run the Main configuration.
    public static void main(String[] args) {
        // Setup
        Security.addProvider(new BouncyCastleProvider());
        Blockchain.setFilePath("blockchain/data.bin");
        Blockchain.load();

        Faker faker = new Faker();

        List<User> marketplaces = Arrays.asList(
                new User("steam"),
                new User("epic-games"),
                new User("origin"),
                new User("humble-bundle"),
                new User("gog")
        );

        for (int i = 0; i < 100; i++) {
            User market = marketplaces.get((int) (Math.random() * (marketplaces.size())));
            User receiver = new User(
                    faker.leagueOfLegends()
                         .champion()
                         .replaceAll("\\W", "")
                         .toLowerCase()
            );
            Transaction txn = new Transaction(
                    UUID.randomUUID().toString(),
                    receiver.getUserId(),
                    faker.leagueOfLegends().summonerSpell(),
                    faker.idNumber().ssnValid(),
                    LocalDateTime.now().minus((long) (Math.random() * 365) + 1, ChronoUnit.DAYS).toString(),
                    market.getUserId()
            );

            try {
                txn.setLicenseCode(
                        Encryptor.encrypt(txn.getLicenseCode(),
                                          market.getKeyPair().getPrivate(),
                                          receiver.getKeyPair().getPublic())
                );
                market.digitallySign(txn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Blockchain.pushTransaction(txn);

        }
    }
}
