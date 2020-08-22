(ns blackjack.core
  (:require [blackjack.simple-hand :refer [deal new-hand up-card add-card total]]))

(declare play-game)

(defn stupid-strategy [hand opponent-up-card]
  (> opponent-up-card 5))

(defn test-strategy
  ([player-strategy house-strategy]
     (test-strategy player-strategy house-strategy 100))
  ([player-strategy house-strategy n]
     "plays n games and returns how many times the player won"
   (reduce + (repeatedly n #(play-game player-strategy house-strategy)))
     ))

(defn stop-at-17 [hand opponent-up-card]
  (< (total hand) 17))

(defn stop-at [n]
  "Returns a strategy that twists until the total is n"
  (fn [hand opponent-up-card] (< (+ (total hand) opponent-up-card) n))
  )

(defn watched [strategy]
  (fn [hand opponent-up-card]
    (println "hand: " hand "  " "up-card: " opponent-up-card "==>"
             (if (strategy hand opponent-up-card) "Hit" "Don't Hit"))
    (strategy hand opponent-up-card)))

(defn smart-strategy [hand opponent-up-card]
  ;; COMPLETE
  )

(defn play-hand [strategy hand opponent-up-card]
  (cond (> (total hand) 21)
        hand

        ((watched strategy) hand opponent-up-card) ; Asks 'should I hit?'
        (recur strategy
               (add-card hand (deal)) ; Recurs, adding a card
               opponent-up-card)

        :else
        hand))

(defn play-game [player-strategy house-strategy]
  (let [house-initial-hand (new-hand)
        player-hand (play-hand player-strategy
                               (new-hand)
                               (up-card house-initial-hand))]
    (if (> (total player-hand) 21)
      0 ; Player bust
      (let [house-hand (play-hand house-strategy
                                  house-initial-hand
                                  (up-card player-hand))]
        (cond (> (total house-hand) 21)
              1 ; House bust

              (> (total player-hand) (total house-hand))
              1 ; House lost

              :else
              0 ; Player lost
              )))))
