(ns matcherwebservice.core
  (:use ring.middleware.json
        ring.util.response
        ring.middleware.params)
  (:require [matcherwebservice.router :as router]))

(defn handler [request]
  (let [uri (request :uri)]
    (if (= "/favicon.ico" uri)
      (response {:foo "bar"})
;if not a request for favicon.ico we should route the request in a cool way
      (response
       (router/route request)))))
(def app
  (-> handler
      wrap-params
      wrap-json-response))
