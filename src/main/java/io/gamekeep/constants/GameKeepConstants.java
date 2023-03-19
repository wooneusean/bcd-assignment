package io.gamekeep.constants;

import io.gamekeep.components.Seller;

import java.util.Arrays;
import java.util.List;

public class GameKeepConstants {
    public static final String GAMEKEEP_SALT = "k2UQr^P5YN9Z3T5bzyjiY%Rfy%Trq%#5G5Yk@r$VSsD6B$9V8T!zv5@!Ed&p$b9EbA2wUaS^KNBwpN*&a3o7oFfJUSD6b7hAG2XjPC9XES4Em@p2bkrLiEfg4*y!Pb33";
    public static final List<Seller> SELLERS = Arrays.asList(
            new Seller("steam"),
            new Seller("epic-games"),
            new Seller("gog"),
            new Seller("battle-net"),
            new Seller("humble-bundle"),
            new Seller("origin")
    );
}
