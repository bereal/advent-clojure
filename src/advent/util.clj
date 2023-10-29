(ns advent.util
  (:require [clj-http.client :as client]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn -mkdir [f]
  (when (not (.exists f)) (.mkdir f))
  f)

(defn -at-home [& path]
  (apply io/file (System/getProperty "user.home") ".aoc" path))

(defn -safe-spit [path content]
  (-mkdir (.getParentFile path))
  (spit path content))

(defn save-config [config]
  (-safe-spit (-at-home "config.edn") (pr-str config)))

(defn read-config []
  (let [config-file (-at-home "config.edn")]
    (if (.exists config-file)
      (edn/read-string (slurp config-file))
      {})))

(defn get-session-id []
  (let [config (read-config)]
    (if (nil? (:session-id config))
      (do
        (println "Please enter your session id:")
        (let [session-id (read-line)]
          (save-config (assoc config :session-id session-id))
          session-id))
      (:session-id config))))

(defn -download-input [year day]
  (let [url (str "https://adventofcode.com/" year "/day/" day "/input")]
    (:body (client/get url {:headers {:cookie (str "session=" (get-session-id))}}))))

(defn load-input [year day]
  (let [path (-at-home (str year "/" day ".txt"))]
    (when (not (.exists path)) (-safe-spit path (-download-input year day)))
    (slurp path)))
