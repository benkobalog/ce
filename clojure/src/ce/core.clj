(ns ce.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(def dataFile "/tmp/ce_daily_values")

(def getRatesJson
  ((client/get "http://api.fixer.io/latest")
    :body ))

(defn saveTodaysData
  []
  (spit dataFile getRatesJson))

(defn readTodaysData
  []
  (if
    (.exists (io/file dataFile))
    (slurp dataFile)
    (do
      (saveTodaysData)
      getRatesJson)))

(def body
  (json/read-str (readTodaysData)))

(def baseCurrency (body "base"))
;(def date (body "date"))
(def rates
  (assoc
    (body "rates")
    baseCurrency 1.0))



(defn convert
  [quantity from to]
  (* (/ (rates to) (rates from)) quantity))

(defn -main
  "--"
  [& args]
  (let [quantity (read-string (nth args 0))
        from (s/upper-case (nth args 1))
        to (s/upper-case (nth args 2))]
    (println (convert quantity from to ))))



