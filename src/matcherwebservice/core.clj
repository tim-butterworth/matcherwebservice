(ns matcherwebservice.core
  (:use ring.middleware.json
        ring.util.response
        ring.middleware.params)
  (:require [matcherwebservice.router :as router]
            [cheshire.core :as cjson]))
(defn to-json [body]
  (cjson/generate-string body))
(def response-type-mp
  {:json (fn [n]
           (-> (response (to-json n))
               (content-type "application/json")))
   :html (fn [n]
           (-> (response n)
               (content-type "text/html")))})
(defn handler [request]
  (println request)
  (let [uri (request :uri)]
    (if (= "/favicon.ico" uri)
      (-> (response (to-json {:hi "hi"}))
          (content-type "application/json"))
      (let [resp (router/route request)]
        ((response-type-mp (resp :type)) (resp :data))))))
(def app
  (-> handler
      wrap-params
      wrap-json-body
      ;wrap-json-response
      ))
