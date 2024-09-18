package com.darkona.toolset.app;

import com.darkona.toolset.logging.Logged;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {

    private final List<Pokemon> firstGen = List.of(
            new Pokemon(1, "Bulbasaur"),
                new Pokemon(2, "Ivysaur"),
                new Pokemon(3, "Venusaur"),
                new Pokemon(4, "Charmander"),
                new Pokemon(5, "Charmeleon"),
                new Pokemon(6, "Charizard"),
                new Pokemon(7, "Squirtle"),
                new Pokemon(8, "Wartortle"),
                new Pokemon(9, "Blastoise"),
                new Pokemon(10, "Caterpie"),
                new Pokemon(11, "Metapod"),
                new Pokemon(12, "Butterfree"),
                new Pokemon(13, "Weedle"),
                new Pokemon(14, "Kakuna"),
                new Pokemon(15, "Beedrill"),
                new Pokemon(16, "Pidgey"),
                new Pokemon(17, "Pidgeotto"),
                new Pokemon(18, "Pidgeot"),
                new Pokemon(19, "Rattata"),
                new Pokemon(20, "Raticate"),
                new Pokemon(21, "Spearow"),
                new Pokemon(22, "Fearow"),
                new Pokemon(23, "Ekans"),
                new Pokemon(24, "Arbok"),
                new Pokemon(25, "Pikachu"),
                new Pokemon(26, "Raichu"),
                new Pokemon(27, "Sandshrew"),
                new Pokemon(28, "Sandslash"),
                new Pokemon(29, "Nidoran♀"),
                new Pokemon(30, "Nidorina"),
                new Pokemon(31, "Nidoqueen"),
                new Pokemon(32, "Nidoran♂"),
                new Pokemon(33, "Nidorino"),
                new Pokemon(34, "Nidoking"),
                new Pokemon(35, "Clefairy"),
                new Pokemon(36, "Clefable"),
                new Pokemon(37, "Vulpix"),
                new Pokemon(38, "Ninetales"),
                new Pokemon(39, "Jigglypuff"),
                new Pokemon(40, "Wigglytuff"),
                new Pokemon(41, "Zubat"),
                new Pokemon(42, "Golbat"),
                new Pokemon(43, "Oddish"),
                new Pokemon(44, "Gloom"),
                new Pokemon(45, "Vileplume"),
                new Pokemon(46, "Paras"),
                new Pokemon(47, "Parasect"),
                new Pokemon(48, "Venonat"),
                new Pokemon(49, "Venomoth"),
                new Pokemon(50, "Diglett"),
                new Pokemon(51, "Dugtrio"),
                new Pokemon(52, "Meowth"),
                new Pokemon(53, "Persian"),
                new Pokemon(54, "Psyduck"),
                new Pokemon(55, "Golduck"),
                new Pokemon(56, "Mankey"),
                new Pokemon(57, "Primeape"),
                new Pokemon(58, "Growlithe"),
                new Pokemon(59, "Arcanine"),
                new Pokemon(60, "Poliwag"),
                new Pokemon(61, "Poliwhirl"),
                new Pokemon(62, "Poliwrath"),
                new Pokemon(63, "Abra"),
                new Pokemon(64, "Kadabra"),
                new Pokemon(65, "Alakazam"),
                new Pokemon(66, "Machop"),
                new Pokemon(67, "Machoke"),
                new Pokemon(68, "Machamp"),
                new Pokemon(69, "Bellsprout"),
                new Pokemon(70, "Weepinbell"),
                new Pokemon(71, "Victreebel"),
                new Pokemon(72, "Tentacool"),
                new Pokemon(73, "Tentacruel"),
                new Pokemon(74, "Geodude"),
                new Pokemon(75, "Graveler"),
                new Pokemon(76, "Golem"),
                new Pokemon(77, "Ponyta"),
                new Pokemon(78, "Rapidash"),
                new Pokemon(79, "Slowpoke"),
                new Pokemon(80, "Slowbro"),
                new Pokemon(81, "Magnemite"),
                new Pokemon(82, "Magneton"),
                new Pokemon(83, "Farfetch'd"),
                new Pokemon(84, "Doduo"),
                new Pokemon(85, "Dodrio"),
                new Pokemon(86, "Seel"),
                new Pokemon(87, "Dewgong"),
                new Pokemon(88, "Grimer"),
                new Pokemon(89, "Muk"),
                new Pokemon(90, "Shellder"),
                new Pokemon(91, "Cloyster"),
                new Pokemon(92, "Gastly"),
                new Pokemon(93, "Haunter"),
                new Pokemon(94, "Gengar"),
                new Pokemon(95, "Onix"),
                new Pokemon(96, "Drowzee"),
                new Pokemon(97, "Hypno"),
                new Pokemon(98, "Krabby"),
                new Pokemon(99, "Kingler"),
                new Pokemon(100, "Voltorb"),
                new Pokemon(101, "Electrode"),
                new Pokemon(102, "Exeggcute"),
                new Pokemon(103, "Exeggutor"),
                new Pokemon(104, "Cubone"),
                new Pokemon(105, "Marowak"),
                new Pokemon(106, "Hitmonlee"),
                new Pokemon(107, "Hitmonchan"),
                new Pokemon(108, "Lickitung"),
                new Pokemon(109, "Koffing"),
                new Pokemon(110, "Weezing"),
                new Pokemon(111, "Rhyhorn"),
                new Pokemon(112, "Rhydon"),
                new Pokemon(113, "Chansey"),
                new Pokemon(114, "Tangela"),
                new Pokemon(115, "Kangaskhan"),
                new Pokemon(116, "Horsea"),
                new Pokemon(117, "Seadra"),
                new Pokemon(118, "Goldeen"),
                new Pokemon(119, "Seaking"),
                new Pokemon(120, "Staryu"),
                new Pokemon(121, "Starmie"),
                new Pokemon(122, "Mr. Mime"),
                new Pokemon(123, "Scyther"),
                new Pokemon(124, "Jynx"),
                new Pokemon(125, "Electabuzz"),
                new Pokemon(126, "Magmar"),
                new Pokemon(127, "Pinsir"),
                new Pokemon(128, "Tauros"),
                new Pokemon(129, "Magikarp"),
                new Pokemon(130, "Gyarados"),
                new Pokemon(131, "Lapras"),
                new Pokemon(132, "Ditto"),
                new Pokemon(133, "Eevee"),
                new Pokemon(134, "Vaporeon"),
                new Pokemon(135, "Jolteon"),
                new Pokemon(136, "Flareon"),
                new Pokemon(137, "Porygon"),
                new Pokemon(138, "Omanyte"),
                new Pokemon(139, "Omastar"),
                new Pokemon(140, "Kabuto"),
                new Pokemon(141, "Kabutops"),
                new Pokemon(142, "Aerodactyl"),
                new Pokemon(143, "Snorlax"),
                new Pokemon(144, "Articuno"),
                new Pokemon(145, "Zapdos"),
                new Pokemon(146, "Moltres"),
                new Pokemon(147, "Dratini"),
                new Pokemon(148, "Dragonair"),
                new Pokemon(149, "Dragonite"),
                new Pokemon(150, "Mewtwo"),
                new Pokemon(151, "Mew")
        );


    public String attachName(String name){

        return "Hello, Trainer " + name;
    }


    public List<Pokemon> getFirstGenPokemon(){
        return firstGen;
    }

    public Pokemon getPokemonById(int id){
        return firstGen.stream()
                       .filter(pokemon -> pokemon.id() == id)
                       .findFirst()
                       .orElseThrow(() -> new NullPointerException("Pokemon not found by id: " + id));
    }

    @Logged(argValues = Logged.Values.ALL)
    public Pokemon getPokemonByName(String name)
    throws InterruptedException {
        Thread.sleep(1000);
        return firstGen.stream()
                       .filter(pokemon -> pokemon.name().equalsIgnoreCase(name))
                       .findFirst()
                       .orElseThrow(() -> new NullPointerException("Pokemon not found by name: " + name));
    }

    public record Pokemon(int id, String name) {}


}
