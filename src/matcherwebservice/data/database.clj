(ns matcherwebservice.data.database
  (:require [clojure.java.jdbc :as j]))
(def db-host "localhost")
(def db-port 5432)
(def db-name "peoplematcher")
(def db {:classname "org.postgresql.Driver"
         :subprotocol "postgresql"
         :subname (str "//" db-host ":" db-port "/" db-name)
         :user "postgres"
         :password "postgres"})
(defn vectorize [query params]
  (reduce (fn [r n] (conj r n))
          [query] params))
(defn read-from-db [query params]
  (j/query
   db
   (vectorize query params)))
(defn update-db! [query params]
  (j/execute! ))
;returns an integer that specifies how many rows were modified
(defn write-to-db! [table map]
  (j/insert!
   db
   table
   map))
(defn delete! [query params]
  (j/execute!
   db
   (vectorize query params)))
