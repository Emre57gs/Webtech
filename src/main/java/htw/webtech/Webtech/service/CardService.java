package htw.webtech.Webtech.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import htw.webtech.Webtech.model.Card;
import htw.webtech.Webtech.model.CardDTO;
import htw.webtech.Webtech.model.CardRequest;
import htw.webtech.Webtech.model.Deck;
import htw.webtech.Webtech.model.DeckDTO;
import htw.webtech.Webtech.model.DeckRequest;
import htw.webtech.Webtech.repository.CardRepository;
import htw.webtech.Webtech.repository.DeckRepository;

@Service
public class CardService {

    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;

    public CardService(DeckRepository deckRepository, CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
    }

    public List<DeckDTO> getAllDecks() {
        return deckRepository.findAll().stream()
                .map(d -> new DeckDTO(d.getId(), d.getTitle(), d.getCategory()))
                .toList();
    }

    public DeckDTO createDeck(DeckRequest request) {
        Deck deck = new Deck();
        deck.setTitle(request.title());
        deck.setCategory(request.category());
        Deck saved = deckRepository.save(deck);
        return new DeckDTO(saved.getId(), saved.getTitle(), saved.getCategory());
    }

    public void deleteDeck(Long id) {
        deckRepository.deleteById(id);
    }

    public List<CardDTO> getCardsByDeck(Long deckId) {
        return cardRepository.findByDeckId(deckId).stream()
                .map(c -> new CardDTO(c.getId(), c.getQuestion(), c.getAnswer()))
                .toList();
    }

    public CardDTO createCard(Long deckId, CardRequest request) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck nicht gefunden"));
        Card card = new Card();
        card.setQuestion(request.question());
        card.setAnswer(request.answer());
        card.setDeck(deck);
        Card saved = cardRepository.save(card);
        return new CardDTO(saved.getId(), saved.getQuestion(), saved.getAnswer());
    }
}
