(ns matcherwebservice.router
  (:use  ring.util.response)
  (:require [matcherwebservice.util.routerutil :as routerutil]
            [matcherwebservice.getresources.getresources :as resources]
            [matcherwebservice.data.databaseio :as data]
            [matcherwebservice.views.views :as views]))
(import java.util.UUID)

(defn destruct [in-uri]
  (filter
   (fn [n]
     (not
      (= n "")))
   (clojure.string/split in-uri #"/")))
(def rest-uri-fn-lst
  [[{:get "/admin"}
    (fn [params] {:type :json :data (data/get-admins)})]
   [{:get "/admin/:userhash"}
    (fn [params]
      {:type :json :data (data/get-admin (params :userhash))})]
   [{:post "/admin/:username"}
    (fn [params]
      {:type :json :data (data/create-admin (params :username) (params :password))})]
   [{:put "/admin"}
    (fn [params] "")]
   [{:delete "/admin/:userhash"}
    (fn [params]
      {:type :json :data (data/delete-admin (params :userhash))})]
   [{:get "/admin/:userhash/people"}
    (fn [params]
      {:type :json :data (data/get-people (params :userhash))})]
   [{:get "/admin/:userhash/person/:name"}
    (fn [params]
      {:type :json :data (data/get-person (params :userhash) (params :name))})]
   [{:post "/admin/:userhash/person/:name"}
    (fn [params]
      {:type :json :data (data/create-person (params :userhash) (params :name) (params :email))})]
   [{:post "/admin/:userhash/group/:name"}
    (fn [params]
      {:type :json :data (data/create-group (params :userhash) (params :name))})]
   [{:get "/admin/:userhash/groups"}
    (fn [params]
      {:type :json :data (data/get-groups (params :userhash))})]
   [{:get "/home"}
    (fn [params]
      {:type :html :data (views/home)})]
   [{:get "/resources/:type/:resource"}
    (fn [params]
      {:type :js-resource
       :data (resources/fetch-resource params)})]
   ])
(defn extract-from-rest-uri-fn-lst [i]
  (vec
   (map
    (fn [n] (n i))
    rest-uri-fn-lst)))
(def rest-uris (extract-from-rest-uri-fn-lst 0))
(def rest-fns (extract-from-rest-uri-fn-lst 1))
(def default-response
  {:type :json
   :data {"ok" true
          "name" "people match service"}})
(def rest-uri-lst
  (-> rest-uris routerutil/map-to-vec routerutil/flatten-vecs))
(defn route [request]
  (println request)
  (let [uri (request :uri)
        method (request :request-method)
        params (request :params)
        method-uri (routerutil/combine-request-method method uri)]
    (let [i (routerutil/find-match rest-uri-lst method-uri)]
                                        ;-1 indicates that no match was found for the given uri
      (if (= -1 i)
        default-response
        ((rest-fns i)
         (routerutil/print-return
          (routerutil/keyword-merge-maps
           params
           (routerutil/get-params
            (vec (rest (routerutil/uri-split method-uri)))
            (vec (rest (routerutil/uri-split (rest-uri-lst i))))))))))))
