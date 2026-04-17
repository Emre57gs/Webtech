package htw.webtech.Webtech.service;

import htw.webtech.Webtech.model.CardDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    public List<CardDTO> getAllCards() {
        return List.of(
                new CardDTO("Webtech")
        );
    }
}
