(ns kata-bcn.core)
(use 'clojure.test)

(defn sum [x]
  (apply + x))

(defn position-frame-cut [n game]
  (if (<= n 0)
    0
    (let [p (position-frame-cut (- n 1) game)]
      (+ p (if (= 10 (first (drop p game))) 1 2))
      )
    ))

(defn nth-frame [n game]
  (let [[first-part second-part] (split-at (position-frame-cut n game) game)]
    [(drop (position-frame-cut (- n 1) game) first-part)
     (take 2 second-part)]))

(defn game->frames-with-accompanying-rolls
  [game]
  (for [x (range 1 11)]
    (nth-frame x game)))

(defn score-for-frame-with-accompanying-rolls
  [[[frame0-0 frame0-1] [frame1-0 frame1-1]]]
  (if (= 10 frame0-0)
    (if (nil? frame0-1)
      (+ frame0-0 frame1-0 frame1-1)
      (+ frame0-0 frame0-1 frame1-0)
      )
    (if (= 10 (+ frame0-0 frame0-1))
           (+ 10 frame1-0)
           (+ frame0-0 frame0-1))))

(defn score
  [_game]
  (let [game (concat _game (take 10 (repeat 0)))]
    (sum (map score-for-frame-with-accompanying-rolls (game->frames-with-accompanying-rolls game)))))

;;;;;;;;;;;;;
;;; Tests
;;;;;;;;;;;;;

(def all-zeros-game (take 20 (repeat 0)))

(def all-ones-game (take 20 (repeat 1)))

(def all-twos-game (take 20 (repeat 2)))

(def a-game-with-a-strike-game (concat [10] (take 18 (repeat 2))))

(def a-game-with-different-rolls (flatten (repeat 2 (range 10))))

(def an-almost-perfect-game (flatten (concat (take 9 (repeat [10])) [8 2])))

(def a-perfect-game (flatten (concat (take 9 (repeat [10])) [10 10 10])))

(are [x y] (= x y)
     (score-for-frame-with-accompanying-rolls [[1 2] [3 4]]) 3
     (score-for-frame-with-accompanying-rolls [[10] [3 4]]) 17
     (score-for-frame-with-accompanying-rolls [[6 4] [3 4]]) 13)

(are [x y] (= x y)
     0 (score all-zeros-game)
     20 (score all-ones-game)
     40 (score all-twos-game)
     50 (score a-game-with-a-strike-game)
     300 (score a-perfect-game)
     268 (score an-almost-perfect-game))
