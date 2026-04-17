package htw.webtech.Webtech.controller;

import htw.webtech.Webtech.model.CardDTO;
import htw.webtech.Webtech.service.CardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")

public class Controller {
    private final CardService cardService;

    public Controller(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/test")
    public List<CardDTO> getCards() {

        return cardService.getAllCards();
    }
}
