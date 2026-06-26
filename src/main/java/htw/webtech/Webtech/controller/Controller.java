package htw.webtech.Webtech.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import htw.webtech.Webtech.model.CardDTO;
import htw.webtech.Webtech.model.CardRequest;
import htw.webtech.Webtech.model.DeckDTO;
import htw.webtech.Webtech.model.DeckRequest;
import htw.webtech.Webtech.service.CardService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class Controller {

    private final CardService cardService;
    // decks/{id}/cards Pavel Kom
    public Controller(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/decks")
    public List<DeckDTO> getAllDecks() {
        return cardService.getAllDecks();
    }

    @PostMapping("/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckDTO createDeck(@Valid @RequestBody DeckRequest request) {
        // logger.info("Ein Deckmit den Ttitel " + request.title()  "wurde erstellt") Pavel Kom
        return cardService.createDeck(request);
    }

    @DeleteMapping("/decks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeck(@PathVariable Long id) {
        cardService.deleteDeck(id);
    }

    @GetMapping("/decks/{id}/cards")
    public List<CardDTO> getCards(@PathVariable Long id) {
        return cardService.getCardsByDeck(id);
    }

    @PostMapping("/decks/{id}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@PathVariable Long id, @Valid @RequestBody CardRequest request) {
        return cardService.createCard(id, request);
    }
}
