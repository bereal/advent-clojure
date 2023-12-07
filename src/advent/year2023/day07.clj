(ns advent.year2023.day07
  (:require [clojure.string :refer [split replace]])
  (:require [clojure.core.match :refer [match]])
  (:require [advent.parser :refer [parse-lines]]))

(defn parse-hand [hs]
  (mapv #(Integer/parseInt %)
        (-> hs
            (replace #"((?<!^).)" " $1")
            (replace #"[TJQKA]" {"T" "10" "J" "11" "Q" "12" "K" "13" "A" "14"})
            (.split " "))))

(defn parse-line [line]
  (let [[hand bid] (split line #" ")]
    [(parse-hand hand) (Integer/parseInt bid)]))

(defn parse [input] (parse-lines parse-line input))

(defn group-sorted-hand [hand]
  (->> hand
       (partition-by identity)
       (map count)
       (sort-by -)
       (vec)))

(defn group-hand [hand] (-> hand sort group-sorted-hand))

;; jokers are zeros, add them to the most numerous group
(defn group-hand-with-jokers [hand]
  (let [[jokers rest] (split-with zero? (sort hand))]
    (if (empty? rest) [5]
        (let [[h & t] (group-sorted-hand rest)]
          (vec (cons (+ h (count jokers)) t))))))

(defn jacks-to-jokers [hand] (mapv #(if (= % 11) 0 %) hand))

(defn eval-grouped-hand [hand]
  (match [hand]
    [[5]] 6
    [[4 1]] 5
    [[3 2]] 4
    [[3 1 1]] 3
    [[2 2 1]] 2
    [[2 1 1 1]] 1
    :else 0))

(defn solve [group-fn input]
  (letfn [(add-eval [[hand bid]] [(-> hand group-fn eval-grouped-hand) hand bid])]
    (->> input
         (map add-eval)
         (sort)
         (map last)
         (map-indexed #(* (inc %1) %2))
         (reduce +))))

(defn solve-1 [input] (solve group-hand input))

(defn solve-2 [input]
  (->> input
       (map (fn [[hand bid]] [(jacks-to-jokers hand) bid]))
       (solve (comp group-hand-with-jokers jacks-to-jokers))))
