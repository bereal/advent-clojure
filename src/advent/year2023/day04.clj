(ns advent.year2023.day04
  (:require [clojure.string :refer [split]])
  (:require [advent.parser :refer [parse-sep parse-lines]])
  (:require [clojure.edn :as edn]))

(defn parse-card [line]
  (let [[_ nums] (split line #": *")
        parts (split nums #" *\| *")]
    (mapv #(parse-sep edn/read-string #" +" %) parts)))

(defn parse [input]
  (parse-lines parse-card input))

(defn count-winning-nums [[left right]]
  (count (filter (set right) left)))

(defn score [n]
    (if (zero? n) 0 (bit-shift-left 1 (dec n))))

(defn inc-prefix [vals len n]
  (let [[prefix rest] (split-at len vals)
        new (map + prefix (repeat n))]
    (concat new rest)))

(defn solve-1 [cards]
  (->> cards
       (map (comp score count-winning-nums))
       (reduce +)))

(defn solve-2 [cards]
  (loop [cs cards
         counts (repeat (count cards) 1)
         total 0]
    (if (empty? cs) total
        (let [[cur-card & rest-cards] cs
              [cur-count & rest-counts] counts
              wc (count-winning-nums cur-card)
              new-counts (inc-prefix rest-counts wc cur-count)]
          (recur rest-cards new-counts (+ total cur-count))))))
