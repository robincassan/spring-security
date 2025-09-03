package org.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Liste de prénoms
        List<String> prenoms = List.of("Robin", "Tommy", "Alexei", "Nuno", "Sandrine",
                "Cyril", "Laurence", "Dimitry", "Julien", "Matthieu","Sarah", "Daris",
                "Angeline", "Olivier");

        System.out.println("Hash SHA-256 des prénoms :\n");
        for (String prenom : prenoms) {
            String hash = getHash(prenom);
            System.out.println(prenom + " → " + hash);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String chaine = "toto";

        // Premier hash
        String hash1 = encoder.encode(chaine);

        // Deuxième hash
        String hash2 = encoder.encode(chaine);

        System.out.println("Mot : " + chaine);
        System.out.println("Hash 1 : " + hash1);
        System.out.println("Hash 2 : " + hash2);

        // Vérification : est-ce que hash1 == hash2 ?
        System.out.println("\nLes deux hash sont-ils identiques ? " + hash1.equals(hash2));

        // Vérification correcte avec BCrypt (match)
        System.out.println("hash1 correspond-il à 'toto' ? " + encoder.matches("toto", hash1));
        System.out.println("hash2 correspond-il à 'toto' ? " + encoder.matches("toto", hash2));

        String totohash = encoder.encode("toto");
        String newtotohash = encoder.encode("toto");

        System.out.println("Hash 1 : " + totohash);
        System.out.println("Hash 2 : " + newtotohash);

// Vérification
        System.out.println("matches(toto, totohash) → " + encoder.matches("toto", totohash));
        System.out.println("matches(toto, newtotohash) → " + encoder.matches("toto", newtotohash));
    }

    public static String getHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }


}