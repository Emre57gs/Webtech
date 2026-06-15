package htw.webtech.Webtech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import htw.webtech.Webtech.model.*;
import htw.webtech.Webtech.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CardService cardService;

    @Test
    void getAllDecks_returnsJsonList() throws Exception {
        when(cardService.getAllDecks()).thenReturn(List.of(
                new DeckDTO(1L, "Mathe", "Schule"),
                new DeckDTO(2L, "Vokabeln", "Sprachen")
        ));

        mockMvc.perform(get("/api/decks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Mathe"))
                .andExpect(jsonPath("$[1].category").value("Sprachen"));
    }

    @Test
    void createDeck_returns201WithCreatedDeck() throws Exception {
        DeckRequest request = new DeckRequest("Physik", "Naturwissenschaften");
        DeckDTO created = new DeckDTO(3L, "Physik", "Naturwissenschaften");
        when(cardService.createDeck(any(DeckRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Physik"));
    }

    @Test
    void deleteDeck_returns204() throws Exception {
        doNothing().when(cardService).deleteDeck(1L);

        mockMvc.perform(delete("/api/decks/1"))
                .andExpect(status().isNoContent());

        verify(cardService).deleteDeck(1L);
    }

    @Test
    void getCards_returnsCardsOfDeck() throws Exception {
        when(cardService.getCardsByDeck(1L)).thenReturn(List.of(
                new CardDTO(10L, "Was ist 2+2?", "4"),
                new CardDTO(11L, "Hauptstadt von Frankreich?", "Paris")
        ));

        mockMvc.perform(get("/api/decks/1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].question").value("Was ist 2+2?"))
                .andExpect(jsonPath("$[1].answer").value("Paris"));
    }

    @Test
    void createCard_returns201WithCreatedCard() throws Exception {
        CardRequest request = new CardRequest("Was ist Java?", "Eine Programmiersprache");
        CardDTO created = new CardDTO(20L, "Was ist Java?", "Eine Programmiersprache");
        when(cardService.createCard(eq(1L), any(CardRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/decks/1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.question").value("Was ist Java?"));
    }

    @Test
    void createCard_whenDeckNotFound_returns404() throws Exception {
        when(cardService.createCard(eq(99L), any(CardRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck nicht gefunden"));

        mockMvc.perform(post("/api/decks/99/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CardRequest("Frage", "Antwort"))))
                .andExpect(status().isNotFound());
    }
}
