(ns tic-tac-toe-cljs.core
  (:require [reagent.core :as reagent :refer [atom]]))

(def init-board {0 " " 1 " " 2 " " 3 " " 4 " " 5 " " 6 " " 7 " " 8 " "})
(def winning-moves [[0 1 2], [3 4 5], [6 7 8], [0 3 6], [1 4 7], [2 5 8], [0 4 8], [2 4 6]])
(def symbols ["X" "0"])

(def board (atom init-board))
(def current-player-index (atom 0))
(def game-status (atom ""))

(defn is-subset [set1 set2] (every? #(contains? (set set1) %) set2))

(defn toggle [index] (- 1 index))

(defn update-board [index symbol] (if (= " " (get @board index)) (assoc @board index symbol) @board))

(defn get-winning-message [] (str "Congrats " (get symbols @current-player-index)  " has WON"))

(defn get-player-moves [symbol] (keys (filter #(= (second %) symbol) @board)))

(defn check-game-status []
  (reset! game-status (cond
                        (some #(is-subset (get-player-moves (get symbols @current-player-index)) %) winning-moves) (get-winning-message)
                        (every? #(not= " " (second %)) @board) "Game Drawn"
                        :else "")))

(defn reset-game []
  (reset! board init-board)
  (reset! current-player-index 0)
  (reset! game-status ""))

(defn move [event]
  (let [index (int event.target.id)]
    (when (and (= (get @board index) " ") (= @game-status ""))
      (swap! board (partial update-board index (get symbols @current-player-index)))
      (check-game-status)
      (reset! current-player-index (toggle @current-player-index)))))

(defn tic-tac-toe []
  [:div
   [:h1.title (:text :value "Welcome to Tic Tac Toe")]
   [:div.board (map (fn [element] [:div {:class ["cell" (second element)]
                                         :id (first element)
                                         :key (first element)
                                         :on-click move} (second element)]) @board)]
   [:h1.game-status (:text :value @game-status)]
   [:div.reset-section
    [:button.reset {:on-click reset-game} "PLAY AGAIN"]]])


(reagent/render-component [tic-tac-toe]
                          (. js/document (getElementById "app")))

