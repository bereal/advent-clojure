(ns advent.year2023.day09
  (:require [advent.parser :refer [parse-sep parse-lines]]))

(defn parse-line [line] (parse-sep #(Integer/parseInt %) #" " line))

(defn parse [input] (parse-lines parse-line input))

(defn guess [nums]
  (loop [cur nums res 0]
    (let [[head & tail] cur]
      (if (every? zero? cur) res
        (recur (map - cur tail) (+ head res))))))

(defn solve [input] (->> input (map guess) (reduce +)))

(defn solve-1 [input]
  (->> input (map reverse) solve))

(def solve-2 solve)
