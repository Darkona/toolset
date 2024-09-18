package com.darkona.toolset.app;

import com.darkona.toolset.objects.ObjectUtilities;
import com.darkona.toolset.objects.TestObject;
import com.darkona.toolset.logging.Logged;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController("/")
@RequiredArgsConstructor
public class Controller {

    private final PokemonService pokemonService;

    @GetMapping("pokemon")
    @Logged
    public Map<String, String> allPokemon(@PathVariable String name) {
        return pokemonService.getFirstGenPokemon().stream()
                             .collect(Collectors.toMap(p -> String.valueOf(p.id()), PokemonService.Pokemon::name));

    }

    @GetMapping("/pokemon/name/{name}")
    @Logged(argValues = Logged.Values.NULL)
    public PokemonService.Pokemon getPokemon(@PathVariable String name)
    throws InterruptedException {
//        try {
            return pokemonService.getPokemonByName(name);
//        }catch (Exception e){
//            return new PokemonService.Pokemon(-1, "MissingNo.");
//        }
    }

    @GetMapping("/pokemon/{number}")
    @Logged(argValues = Logged.Values.ALL)
    public PokemonService.Pokemon getPokemon(@PathVariable Integer number) {
        return pokemonService.getPokemonById(number);
    }

    @GetMapping("explode")
    public Map<String, String> explode() {

        var test = new TestObject();

        try {
            return Map.of("message", "Exploded!", "pokemon", test.explode());
        } catch (IllegalAccessException e) {
            return Map.of("message", "Exploded!", "error", e.getMessage());
        }
    }

    @GetMapping("/dehydration")
    public Map<String, String> dehydration() {

        var test = new TestObject();
        test.make();

        return ObjectUtilities.dehydrate(test);
    }

    @GetMapping("/rehydration")
    public TestObject rehydration() {

        var test = new TestObject();
        test.make();

        return ObjectUtilities.rehydrate(ObjectUtilities.dehydrate(test), TestObject.class);

    }
}
