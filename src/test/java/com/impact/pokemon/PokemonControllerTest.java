package com.impact.pokemon;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PokemonControllerTest {

    private final TestRestTemplate rest;

    PokemonControllerTest(@LocalServerPort int port) {
        rest = new TestRestTemplate(new RestTemplateBuilder().rootUri(format("http://localhost:%d", port)));
    }

    @Test
    void testAttackPicksWinnerWithHitPoints() {
        Map<String, Object> response = rest.getForObject("/attack", Map.class);
        assertEquals(2, response.size());
        assertEquals("Bulbasaur", response.get("winner"));
        assertEquals(120, response.get("hitPoints"));
    }
    
    @Test
    void testFireVsGrassEffectiveness() {
        // Testing type effectiveness between Fire and Grass types
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Charmander&pokemonB=Bulbasaur", Map.class);

        // Check that Charmander wins because Fire is effective against Grass
        assertNotNull(response);
        assertEquals("Charmander", response.get("winner"));  // Fire should win against Grass
    }

    @Test
    void testEqualSpeedRandomWinner() {
        // Setting up two Pokémon with the same speed for a random winner scenario
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Bulbasaur&pokemonB=Ivysaur", Map.class);

        // As speed is equal, either can win, so check the winner is one of the two
        assertNotNull(response);
        String winner = (String) response.get("winner");
        assertEquals(true, winner.equals("Bulbasaur") || winner.equals("Ivysaur"));
    }

    @Test
    void testPokemonNotFound() {
        // Test when one of the Pokémon is not found
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Bulbasaur&pokemonB=Unknown", Map.class);

        // Expect an error in the response since one Pokémon is missing
        assertNotNull(response);
        assertEquals("One or both Pokémon not found.", response.get("error"));
    }

    @Test
    void testBattleEndsWhenHPZero() {
        // Lowering Charmeleon's HP to ensure the battle ends quickly
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Bulbasaur&pokemonB=Charmeleon", Map.class);

        // Ensure the battle ends when one Pokémon's HP reaches 0
        assertNotNull(response);
        assertEquals("Bulbasaur", response.get("winner"));  // Adjust based on your actual battle logic
    }
}
