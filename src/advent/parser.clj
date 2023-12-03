(ns advent.parser
  (:require [clojure.string :refer [split-lines split]]))

(defn parse-lines [parse-fn input]
  (let [lines (split-lines input)]
    (map parse-fn lines)))

(defn parse-sep [parse-fn sep input]
  (let [parts (split input sep)]
    (map parse-fn parts)))

(defn find-groups [pattern input]
  (let [m (re-matcher pattern input)]
    (loop [res []]
      (if (.find m)
        (recur (cons [(.group m) (.start m) (.end m)] res))
        (reverse res)))))