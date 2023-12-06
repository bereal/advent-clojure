(ns advent.year2023.day05
  (:require [advent.parser :refer [parse-sep]]
            [clojure.string :refer [split]]))

(defn parse-seeds [line]
  (mapv #(Long/parseLong %)
        (-> line (split #": ") (second) (split #" "))))

(defn parse-conversion [group]
  (let [lines (split group #"\n")
        parse-line (partial parse-sep #(Long/parseLong %) #" ")]
    (mapv parse-line (rest lines))))

(defn parse [input]
  (let [[seed & groups] (split input #"\n\n")]
    [(parse-seeds seed) (mapv parse-conversion groups)]))

(defn create-lookup-table [conversions]
  (let [sorted (vec (sort-by second conversions))
        index (into-array (map second sorted))]
    [index sorted]))

(defn lookup [val [index data]]
  (letfn [(convert [idx]
                   (let [[target source size] (data idx)]
                     (if (>= val (+ source size)) val (+ target (- val source)))))]
    (let [i (java.util.Arrays/binarySearch index val)]
      (cond (= -1 i) val
            (>= i 0) (convert i)
            :else (convert (- -2 i))))))

(defn convert-seed [chain seed]
  (reduce (partial lookup) seed chain))

(defn solve-1 [[seeds conversions]]
  (let [chain (map create-lookup-table conversions)
        results (mapv #(convert-seed chain %) seeds)]
    (reduce min results)))

(defn solve-2 [_] "TODO")
