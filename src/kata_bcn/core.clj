(ns kata-bcn.core)
(use 'clojure.test)


(def all-zeros-game (take 20 (repeat 0)))

(def all-ones-game (take 20 (repeat 1)))

(def all-twos-game (take 20 (repeat 2)))

(def a-game-with-a-strike-game (concat [10 "X"] (take 18 (repeat 2))))

(def a-game-with-different-rolls (flatten (repeat 2 (range 10))))

(def an-almost-perfect-game (flatten (concat (take 9 (repeat [10 "X"])) [8 2])))

(def a-perfect-game (flatten (concat (take 9 (repeat [10 "X"])) [10 10 10])))

(defn sum [x]
  (apply + x))

(defn nth-frame [n game]
  (let [[first-part second-part] (split-at (+ 2 (* 2 n)) game)]
    [(drop (- (count first-part) 2) first-part)
     (take 2 (remove #(= % "X") second-part))]))

(defn game->frames-with-accompanying-rolls
  [game]
  (for [x (range 10)]
    (nth-frame x game)))

(defn score-for-frame-with-accompanying-rolls
  [[[frame0-0 frame0-1] [frame1-0 frame1-1]]]
  (if (= 10 frame0-0)
    (if (not= "X" frame0-1)
      (+ frame0-0 frame0-1 frame1-0)
      (+ frame0-0 frame1-0 frame1-1))
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

(are [x y] (= x y)
     (score-for-frame-with-accompanying-frame [[1 2] [3 4]]) 3
     (score-for-frame-with-accompanying-frame [[10 0] [3 4]]) 17
     (score-for-frame-with-accompanying-frame [[6 4] [3 4]]) 13)

(is (= '((10 0) (2 2) (2 2) (2 2) (2 2) (2 2) (2 2) (2 2) (2 2) (2 2))
       (game->frames a-game-with-a-strike-game)))

(are [x y] (= x y)
     0 (score all-zeros-game)
     20 (score all-ones-game)
     40 (score all-twos-game)
     50 (score a-game-with-a-strike-game))
