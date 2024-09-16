const app = Vue.createApp({
	data() {
		return {
			pokemonList: [], // List of Pokémon names
			selectedPokemonA: '', // Initialize selected Pokémon A
			selectedPokemonB: '', // Initialize selected Pokémon B
			pokemonName: '________' // Winner's Pokémon Name
		};
	},
	methods: {
		battle() {
			if (!this.selectedPokemonA || !this.selectedPokemonB) {
				alert("Please select both Pokémon before battling!");
				return;
			}

			// Trigger the battle between selected Pokémon A and B
			fetch(`/attack?pokemonA=${this.selectedPokemonA}&pokemonB=${this.selectedPokemonB}`)
				.then(response => response.json())
				.then(data => {
					this.pokemonName = data.winner;
					
				});
		}
	},
	mounted() {
		
		// Fetch the list of Pokémon when the component is mounted
		fetch('/pokemonList')
			.then(response => response.json())
			.then(data => {
				this.pokemonList = data; // Populate the dropdown with Pokémon names
				
			})
			.catch(error => {
				console.error("Error fetching Pokemon list:", error);
			});
	}
});

app.mount('#app');
