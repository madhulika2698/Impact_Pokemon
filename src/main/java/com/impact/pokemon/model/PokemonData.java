package com.impact.pokemon.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.impact.pokemon.dto.Pokemon;

/**
 * !! Feel free to change everything about this !! This could be a class to hold
 * all the Pokemon objects loaded from CSV, but there are many ways to do it.
 */
@Component
public class PokemonData {
	private final File file;

	PokemonData() throws IOException {
		file = new ClassPathResource("data/pokemon.csv").getFile();
	}

	File getFile() {
		return file;
	}

	public List<Pokemon> loadPokemonData() throws IOException {
		List<Pokemon> pokemonList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			br.readLine(); // Skip the header
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				// Assuming the CSV columns are ordered as: Name, Type, HP, Attack, Defense,
				// Speed
				Pokemon pokemon = new Pokemon(
	                    Integer.parseInt(data[0]),   // ID
	                    data[1],                    // Name
	                    data[2],                    // Type
	                    Integer.parseInt(data[3]),   // Total
	                    Integer.parseInt(data[4]),   // HitPoints
	                    Integer.parseInt(data[5]),   // Attack
	                    Integer.parseInt(data[6]),   // Defense
	                    Integer.parseInt(data[7]),   // SpecialAttack
	                    Integer.parseInt(data[8]),   // SpecialDefense
	                    Integer.parseInt(data[9]),   // Speed
	                    Integer.parseInt(data[10]),  // Generation
	                    Boolean.parseBoolean(data[11]) // Legendary
	                );
				pokemonList.add(pokemon);
			}
		}
		return pokemonList;
	}
}
