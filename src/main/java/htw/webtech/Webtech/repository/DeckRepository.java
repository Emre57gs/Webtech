package htw.webtech.Webtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import htw.webtech.Webtech.model.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long> {
}
