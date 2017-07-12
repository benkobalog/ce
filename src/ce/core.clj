(ns ce.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:use hickory.core))

(def body
  (json/read-str
    ((client/get "http://api.fixer.io/latest")
      :body )))

(def baseCurrency (body "base"))
(def date (body "date"))
(def rates
  (assoc
    (body "rates")
    "EUR" 1.0 ))

(defn convert
  [quantity from to]
  (* (/ (rates to) (rates from)) quantity))

(defn -main
  "--"
  [& args]

  ;; parse parameters

  ;; do conversion
  (println baseCurrency)
  (println date)
  (println (rates "HUF"))
  ;(println (* (rates "HUF") (rates "EUR") 123))
  (println (convert 1000 "USD" "HUF" ))
)



