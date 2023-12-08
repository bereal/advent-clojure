(ns advent.year2023.day08
  (:require [clojure.string :refer [split ends-with?]]))

(defn parse-path [p] (map {\L 0 \R 1} p))

(defn parse-node [line]
  (let [[src left right] (re-seq #"[A-Z]{3}" line)]
    [src [left right]]))

(defn parse [input]
  (let [[path _ & nodes] (split input #"\n")
        node-map (->> nodes
         (map parse-node)
         (apply concat)
         (apply hash-map))]
    [(parse-path path) node-map]))

(defn follow-path [path nodes start end-fn]
  (loop [cur start steps 0 p path]
    (if (end-fn cur) steps
        (let [[head & tail] p
              next ((nodes cur) head)]
          (recur next (inc steps) tail)))))

(defn lcm [a b] (.divide (.multiply a b) (.gcd a b)))

(defn solve-1 [[path nodes]] (follow-path (cycle path) nodes "AAA" #(= "ZZZ" %)))

;; it doesn't work in general case,
;; but it seems that everyone's input constructed to make it work
(defn solve-2 [[path nodes]]
  (let [end? #(ends-with? % "Z")]
    (->> (keys nodes)
         (filter #(ends-with? % "A"))
         (map #(follow-path (cycle path) nodes % end?))
         (map biginteger)
         (reduce lcm))))