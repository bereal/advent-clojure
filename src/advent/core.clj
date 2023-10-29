(ns advent.core
  (:require [advent.util :as util]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))

(defn run [year day]
  (let [ns-name (str "advent.year" year ".day" (format "%02d" day))
        resolve (fn [sym] (find-var (symbol (str ns-name "/" sym))))]
    (require (symbol ns-name))
    (let [parse (resolve "parse")
          solve-1 (resolve "solve-1")
          solve-2 (resolve "solve-2")
          input (util/load-input year day)
          parsed (parse input)]
        (println (str "Part 1: " (solve-1 parsed)))
        (println (str "Part 2: " (solve-2 parsed))))))

(def cli-opts
  [["-y" "--year YEAR" "Year to run"
    :parse-fn #(Integer/parseInt %)]
   ["-d" "--day DAY" "Day to run"
    :parse-fn #(Integer/parseInt %)]])

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
      (let [{:keys [year day]} options]
        (run year day)))))
