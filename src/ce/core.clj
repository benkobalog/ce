(ns ce.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as s]))

(def body
  (json/read-str
    ((client/get "http://api.fixer.io/latest")
      :body )))

(def baseCurrency (body "base"))
(def date (body "date"))
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
  ;; parse parameters
  (let [quantity (read-string (nth args 0))
        from (s/upper-case (nth args 1))
        to (s/upper-case (nth args 2))]
    (println (convert quantity from to )))
)



