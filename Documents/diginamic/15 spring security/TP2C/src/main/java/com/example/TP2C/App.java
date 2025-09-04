package com.example.TP2C;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class App {
    public static void main(String[] args) {

        // Le JWT à tester
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKZSBuZSBzYWlzIHBhcyIsIm1lc3NhZ2UiOiLDoCBtb2kgbcOqbWUiLCJtZXNzYWdlLWNhY2jDqSI6InRyYXZhaWxsZSwgw6dhIGZpbml0IHRvdWpvdXJzIHBhciBwYXllciIsImxodW1vdXIiOiJjJ2VzdCBpbXBvcnRhbnQiLCJpYXQiOjE3NDQ4MzE5MTV9.wdaFguIzdkNKgVaYmSg5jHgYCDenufwjlJEL7T42fLA";

        // Les clés à tester
        String[] keys = {
                "essayeDoncCetteChouetteCleSecreteOnVerraSiCaMarche",
                "maToutAutantChouetteCleSecreteQueJeChoisiCommeJeVeux"
        };

        for (String key : keys) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(key.getBytes())
                        .parseClaimsJws(jwt)
                        .getBody();

                System.out.println("La clé fonctionne !");
                System.out.println("Clé : " + key);
                System.out.println("Subject : " + claims.getSubject());
                System.out.println("Message : " + claims.get("message"));
                System.out.println("Message caché : " + claims.get("message-caché"));
                System.out.println("Humour : " + claims.get("lhumour"));
                System.out.println("-------------------------");

            } catch (SignatureException e) {
                System.out.println("Clé incorrecte : " + key);
                System.out.println("-------------------------");
            } catch (Exception e) {
                System.out.println("Erreur avec la clé : " + key + " -> " + e.getMessage());
                System.out.println("-------------------------");
            }
        }
    }
}
