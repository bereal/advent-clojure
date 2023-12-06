(ns advent.year2023.day06
  (:require [clojure.string :refer [split]])
  (:require [advent.parser :refer [parse-lines]]))

(defn parse-line [line]
  (let [[_ & nums] (split line #" +")] (mapv #(Integer/parseInt %) nums)))

(defn parse [input]
  (parse-lines parse-line input))

(defn calc [time record]
  (let [d (- (* time time) (* 4 record))
        sd (Math/sqrt d)
        low (/ (- 0 time sd) 2)
        high (+ low sd)]
    (long (inc (- (Math/floor high) (Math/ceil low))))))

(defn join-ints [ints]
  (Long/parseLong (apply str ints)))

(defn solve-1 [input] (reduce * (apply (partial map calc) input)))

(defn solve-2 [[times records]]
  (calc (join-ints times) (join-ints records)))