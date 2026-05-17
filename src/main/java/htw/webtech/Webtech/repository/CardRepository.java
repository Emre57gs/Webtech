package htw.webtech.Webtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import htw.webtech.Webtech.model.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByDeckId(Long deckId);
}
