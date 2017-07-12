(ns ce.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [hickory.select :as s])
  (:use hickory.core))

(def content (-> (client/get "https://www.currenciesdirect.com/en/currency-tools/today-s-rate") :body parse as-hickory))

(def getCurrencyPairsRaw
  (s/select
      (s/descendant
        (s/class  "js-todaysrates-rows")
        (s/tag :strong))
        content))

(def getRatesRaw
  (s/select
    (s/descendant
      (s/class "js-todaysrates-rows")
      (s/class "rates"))
    content))


(def ratesList
  (map read-string
     (map first
        (map :content getRatesRaw))))

(def currencyPairList
  (partition 2 2
     (map first
        (map :content getCurrencyPairsRaw))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  ;; parse parameters

  ;; do conversion
  (clojure.pprint/pprint  ratesList)
  (clojure.pprint/pprint  currencyPairList)
  (clojure.pprint/pprint  (+ (nth ratesList 3) (nth ratesList 4)))


  ;;(clojure.pprint/pprint  (map vector currencyPairList ratesList))


  (println "End of program"))


