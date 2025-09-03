package com.example.TP2B;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CookieController {

    @GetMapping("/get-cookie")
    public ResponseEntity<String> getCookie() {
        // Déclaration du nom et de la valeur du cookie
        String cookieName = "myCookie";
        String cookieValue = "12345";

        // Création du cookie
        ResponseCookie tokenCookie = ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)       // optionnel, rend le cookie inaccessible en JS
                .secure(false)        // true si HTTPS
                .path("/")            // le cookie sera dispo sur toute l’appli
                .maxAge(60 * 60)      // durée de vie (ici 1 heure)
                .build();

        // Retour de la réponse avec le cookie dans le header
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body("cookie posé avec succès !");
    }
}
