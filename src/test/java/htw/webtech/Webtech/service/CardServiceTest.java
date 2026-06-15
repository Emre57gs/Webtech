package htw.webtech.Webtech.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import htw.webtech.Webtech.model.Card;
import htw.webtech.Webtech.model.CardDTO;
import htw.webtech.Webtech.model.CardRequest;
import htw.webtech.Webtech.model.Deck;
import htw.webtech.Webtech.model.DeckDTO;
import htw.webtech.Webtech.model.DeckRequest;
import htw.webtech.Webtech.repository.CardRepository;
import htw.webtech.Webtech.repository.DeckRepository;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    DeckRepository deckRepository;

    @Mock
    CardRepository cardRepository;

    @InjectMocks
    CardService cardService;

    @Test
    void getAllDecks_returnsMappedDTOs() {
        Deck deck = new Deck();
        deck.setId(1L);
        deck.setTitle("Mathe");
        deck.setCategory("Schule");
        when(deckRepository.findAll()).thenReturn(List.of(deck));

        List<DeckDTO> result = cardService.getAllDecks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Mathe");
        assertThat(result.get(0).category()).isEqualTo("Schule");
    }

    @Test
    void createDeck_savesAndReturnsMappedDTO() {
        DeckRequest request = new DeckRequest("Physik", "Naturwissenschaften");
        Deck saved = new Deck();
        saved.setId(2L);
        saved.setTitle("Physik");
        saved.setCategory("Naturwissenschaften");
        when(deckRepository.save(any(Deck.class))).thenReturn(saved);

        DeckDTO result = cardService.createDeck(request);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.title()).isEqualTo("Physik");
    }

    @Test
    void deleteDeck_callsRepository() {
        cardService.deleteDeck(5L);
        verify(deckRepository).deleteById(5L);
    }

    @Test
    void createCard_throwsWhenDeckNotFound() {
        when(deckRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.createCard(99L, new CardRequest("Frage", "Antwort")))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(rse.getReason()).isEqualTo("Deck nicht gefunden");
                });
    }

    @Test
    void createCard_savesCardUnderCorrectDeck() {
        Deck deck = new Deck();
        deck.setId(1L);
        deck.setTitle("Mathe");
        deck.setCategory("Schule");

        Card saved = new Card();
        saved.setId(10L);
        saved.setQuestion("Was ist 2+2?");
        saved.setAnswer("4");
        saved.setDeck(deck);

        when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));
        when(cardRepository.save(any(Card.class))).thenReturn(saved);

        CardDTO result = cardService.createCard(1L, new CardRequest("Was ist 2+2?", "4"));

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.question()).isEqualTo("Was ist 2+2?");
        assertThat(result.answer()).isEqualTo("4");
    }

    @Test
    void getCardsByDeck_returnsMappedDTOs() {
        Deck deck = new Deck();
        deck.setId(1L);

        Card card1 = new Card();
        card1.setId(10L);
        card1.setQuestion("Was ist 2+2?");
        card1.setAnswer("4");
        card1.setDeck(deck);

        Card card2 = new Card();
        card2.setId(11L);
        card2.setQuestion("Hauptstadt von Frankreich?");
        card2.setAnswer("Paris");
        card2.setDeck(deck);

        when(cardRepository.findByDeckId(1L)).thenReturn(List.of(card1, card2));

        List<CardDTO> result = cardService.getCardsByDeck(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(10L);
        assertThat(result.get(0).question()).isEqualTo("Was ist 2+2?");
        assertThat(result.get(0).answer()).isEqualTo("4");
        assertThat(result.get(1).id()).isEqualTo(11L);
        assertThat(result.get(1).question()).isEqualTo("Hauptstadt von Frankreich?");
        assertThat(result.get(1).answer()).isEqualTo("Paris");
    }

    @Test
    void getAllDecks_returnsEmptyList_whenNoDecksExist() {
        when(deckRepository.findAll()).thenReturn(List.of());

        List<DeckDTO> result = cardService.getAllDecks();

        assertThat(result).isEmpty();
    }

    @Test
    void getCardsByDeck_returnsEmptyList_whenNoCardsExist() {
        when(cardRepository.findByDeckId(42L)).thenReturn(List.of());

        List<CardDTO> result = cardService.getCardsByDeck(42L);

        assertThat(result).isEmpty();
    }
}
