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
        (s/tag :strong)
        )
        content))

(def getRatesRaw
  (s/select
    (s/descendant
      (s/class "js-todaysrates-rows")
      (s/class "rates")
      )
    content))

(def ratesList (map first (map :content getRatesRaw)))
(def currencyPairList  (partition 2 2 (map first (map :content getCurrencyPairsRaw))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (clojure.pprint/pprint  (map vector currencyPairList ratesList))

  (println "End of program"))


