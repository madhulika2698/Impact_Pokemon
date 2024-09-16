package com.impact.pokemon.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impact.pokemon.dto.Pokemon;
import com.impact.pokemon.model.PokemonData;

@RestController
public class PokemonController {

	private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

	@Resource
	private PokemonData data;

	@GetMapping("/Health")
	public String healthCheck() {
		return "Working fine";
	}
	
	
	@GetMapping("/pokemonList")
	public List<String> getPokemonList() throws IOException {
	    List<Pokemon> pokemonList = data.loadPokemonData();
	    return pokemonList.stream()
	            .map(Pokemon::getName)
	            .toList();
	}

	@GetMapping("attack")
	public Map<String, Object> attack(String pokemonA, String pokemonB) throws IOException {
		logger.info("Requested pokemonA: {}, pokemonB: {}", pokemonA, pokemonB);

		// This is just an example of how to read the file contents into a List. Change
		// or refactor as needed
		// Load the list of Pokémon from the CSV file
		List<Pokemon> pokemonList = data.loadPokemonData();

		// Find the two Pokémon from the list by their names
		Optional<Pokemon> p1 = pokemonList.stream().filter(pokemon -> pokemon.getName().equalsIgnoreCase(pokemonA))
				.findFirst();

		Optional<Pokemon> p2 = pokemonList.stream().filter(pokemon -> pokemon.getName().equalsIgnoreCase(pokemonB))
				.findFirst();

		// Check if both Pokémon were found
		if (p1.isEmpty() || p2.isEmpty()) {
			return Map.of("error", "One or both Pokémon not found.");
		}

		Pokemon pokemon1 = p1.get();
		Pokemon pokemon2 = p2.get();

		// Determine which Pokémon attacks first based on speed
		Pokemon attacker, defender;
		if (pokemon1.getSpeed() > pokemon2.getSpeed()) {
			attacker = pokemon1;
			defender = pokemon2;
		} else if (pokemon2.getSpeed() > pokemon1.getSpeed()) {
			attacker = pokemon2;
			defender = pokemon1;
		} else {
			// If both have the same speed, randomly select one
			attacker = Math.random() < 0.5 ? pokemon1 : pokemon2;
			defender = (attacker == pokemon1) ? pokemon2 : pokemon1;
		}

		// Battle loop - continues until one Pokémon's HP drops to 0 or below
		while (pokemon1.getHitPoints() > 0 && pokemon2.getHitPoints() > 0) {
			int damage = calculateDamage(attacker, defender);
			defender.setHitPoints(defender.getHitPoints() - damage);

			// Check if the defender is out of hit points
			if (defender.getHitPoints() <= 0) {
				break;
			}

			// Swap attacker and defender
			Pokemon temp = attacker;
			attacker = defender;
			defender = temp;
		}

		// Determine the winner
		Pokemon winner = (pokemon1.getHitPoints() > 0) ? pokemon1 : pokemon2;
		return Map.of("winner", winner.getName(), "hitPoints", winner.getHitPoints());
	}

	private int calculateDamage(Pokemon attacker, Pokemon defender) {
		// Example damage calculation based on attack/defense and type effectiveness
		double effectiveness = calculateEffectiveness(attacker.getType(), defender.getType());
		return (int) (50 * ((double) attacker.getAttack() / defender.getDefense()) * effectiveness);
	}

	private double calculateEffectiveness(String attackerType, String defenderType) {
		// Example effectiveness calculation based on types (expand as needed)
		if (attackerType.equals("Fire") && defenderType.equals("Grass"))
			return 2.0;
		if (attackerType.equals("Fire") && defenderType.equals("Water"))
			return 0.5;
		if (attackerType.equals("Water") && defenderType.equals("Fire"))
			return 2.0;
		if (attackerType.equals("Water") && defenderType.equals("Electric"))
			return 0.5;
		if (attackerType.equals("Grass") && defenderType.equals("Electric"))
			return 2.0;
		if (attackerType.equals("Grass") && defenderType.equals("Fire"))
			return 0.5;
		if (attackerType.equals("Electric") && defenderType.equals("Water"))
			return 2.0;
		if (attackerType.equals("Electric") && defenderType.equals("Grass"))
			return 0.5;
		return 1.0; // Neutral effectiveness
	}
}
