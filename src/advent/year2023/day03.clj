(ns advent.year2023.day03
  (:require [clojure.string :refer [split]])
  (:require [advent.parser :refer [find-groups]]))

(defn find-line [f pattern n s]
  (->> s
       (find-groups pattern)
       (map (fn [[g start end]]
              (map #(vector [n %] [[n start] (f g)]) (range start end))))))

(defn find-all [f pattern input]
  (->> input
       (map-indexed (partial find-line f pattern))
       (apply concat)
       (apply concat)
       (apply concat)
       (apply hash-map)))

(defn find-syms [pattern input]
  (keys (find-all identity pattern input)))

(defn find-numbers [input]
  (find-all #(Integer/parseInt %) #"(\d+)" input))

(defn find-adjacent-nums [sym-coords numbers]
  (->> sym-coords
       (mapcat (fn [[x y]]
                 (for [dx [-1 0 1]
                       dy [-1 0 1]
                       :let [v (numbers [(+ x dx) (+ y dy)])]
                       :when v]
                   v)))
       (apply concat)
       (apply hash-map)
       vals))

(defn parse [input] (split input #"\n"))

(defn solve-1 [input]
  (let [sym-coords (find-syms #"[^0-9.]" input)
        nums (find-numbers input)
        found-nums (find-adjacent-nums sym-coords nums)]
    (reduce + found-nums)))

(defn solve-2 [input]
  (let [sym-coords (find-syms #"\*" input)
        nums (find-numbers input)
        powers (for [coord sym-coords
                     :let [adj (find-adjacent-nums [coord] nums)]
                     :when (= 2 (count adj))]
                 (reduce * adj))]
    (reduce + powers)))
