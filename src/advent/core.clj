(ns advent.core
  (:require [advent.util :as util]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))
(defmacro measure
  [expr]
  `(let [start# (. System (currentTimeMillis))
         ret# ~expr
         time# (- (. System (currentTimeMillis)) start#)]
     [time# ret#]))

(defn run [year day input-path]
  (let [ns-name (str "advent.year" year ".day" (format "%02d" day))
        resolve (fn [sym] (find-var (symbol (str ns-name "/" sym))))]
    (require (symbol ns-name))
    (let [parse (resolve "parse")
          solve-1 (resolve "solve-1")
          solve-2 (resolve "solve-2")
          input (if (nil? input-path)
                  (util/load-input year day)
                  (slurp input-path))
          [tp parsed] (measure (parse input))
          [t1 solution-1] (measure (solve-1 parsed))
          [t2 solution-2] (measure (solve-2 parsed))]
      (println (str "Parsed in: " tp "ms"))
      (println (str "Part 1: " solution-1 " (" t1 "ms)"))
      (println (str "Part 2: " solution-2 " (" t2 "ms)")))))

(def cli-opts
  [["-y" "--year YEAR" "Year to run"
    :parse-fn #(Integer/parseInt %)]
   ["-d" "--day DAY" "Day to run"
    :parse-fn #(Integer/parseInt %)]
   ["-i" "--input PATH" "Input file"]])

(defn validate-args [args]
    (let [{:keys [options errors summary]} (parse-opts args cli-opts)]
     (cond
       (:help options)
       {:exit-message (string/join \newline summary) :ok? true}
       errors
       {:exit-message (string/join \newline errors)}
       :else
       {:options options})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [exit-message options ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (let [{:keys [year day input]} options]
        (run year day input)))))
