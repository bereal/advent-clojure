(ns advent.year2023.day02
  (:require [clojure.string :refer [split]])
  (:require [advent.parser :refer [parse-lines parse-sep]]))

(defn add-rounds [[r1 g1 b1] [r2 g2 b2]]
  [(+ r1 r2) (+ g1 g2) (+ b1 b2)])

(defn max-round [[r1 g1 b1] [r2 g2 b2]]
  [(max r1 r2) (max g1 g2) (max b1 b2)])

(defn parse-cube [cube]
  (let [[sn col] (split cube #" ")
        n (Integer/parseInt sn)]
    (case col
      "red" [n 0 0]
      "green" [0 n 0]
      "blue" [0 0 n])))

(defn parse-round [input]
  (->> input
       (parse-sep parse-cube #", ")
       (reduce add-rounds)))

(defn parse-game [line]
  (let [[_ sn srounds] (re-find #"Game (\d+): (.*)" line)
        rounds (parse-sep parse-round #"; " srounds)]
    [(Integer/parseInt sn) rounds]))

(def parse (partial parse-lines parse-game))

(defn required-cubes [[n rounds]] [n (reduce max-round rounds)])

(defn solve-1 [games]
  (->> games
       (map required-cubes)
       (filter (fn [[_ [r g b]]] (and (<= r 12) (<= g 13) (<= b 14))))
       (map first)
       (reduce +)))

(defn solve-2 [games]
  (->> games
       (map (comp #(apply * %) second required-cubes))
       (reduce +)))
