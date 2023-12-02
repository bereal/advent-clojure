(ns advent.year2023.day01
  (:require clojure.string))

(def parse clojure.string/split-lines)

(def digit-names ["one", "two", "three", "four", "five", "six", "seven", "eight", "nine"])
(def digit-chars (mapv str (range 1 10)))

(def digit-map (apply hash-map (concat
                                (interleave digit-names (range 1 10))
                                (interleave digit-chars (range 1 10)))))

(defn find-all-digits [s patterns]
  (let [m (re-matcher (re-pattern (clojure.string/join \| patterns)) s)]
    (loop [digits []
           offset 0]
      (if (.find m offset)
        (recur (conj digits (digit-map (.group m))) (inc (.start m)))
        digits))))

(defn digits-to-num [digits]
  (let [d1 (first digits)
        d2 (peek digits)]
    (+ (* 10 d1) d2)))

(defn solve [input patterns]
  (->> input
       (map #(find-all-digits % patterns))
       (map digits-to-num)
       (reduce +)))

(defn solve-1 [input]
  (solve input digit-chars))

(defn solve-2 [input]
  (solve input (concat digit-names digit-chars)))
