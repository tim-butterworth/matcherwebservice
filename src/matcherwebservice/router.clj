(ns matcherwebservice.router
  (:use  ring.util.response)
  (:require [matcherwebservice.util.routerutil :as routerutil]
            [matcherwebservice.getresources.getresources :as resources]
            [matcherwebservice.data.databaseio :as data]
            [matcherwebservice.views.views :as views]
            [cheshire.core :as cjson]))
(import java.util.UUID)
(def default-response
  {:type :json
   :data (cjson/parse-string "{\n  \"data\": {\n \"city\": {\n \"G1A\": {\n \"last-week\": 91,\n \"last-4-weeks\": 1277,\n        \"last-6-months\": 5366,\n        \"last-12-months\": 5366\n      },\n \"Columbus, OH\": {\n \"last-week\": 24523,\n \"last-4-weeks\": 113345,\n \"last-6-months\": 961940,\n        \"last-12-months\": 961940\n      }\n    }\n  }\n}")})
(def columbus-str
  "{\n  \"data\": {\n \"city\": {\n \"G1A\": {\n \"last-week\": 91,\n \"last-4-weeks\": 1277,\n        \"last-6-months\": 5366,\n        \"last-12-months\": 5366\n      },\n \"Columbus, OH\": {\n \"last-week\": 24523,\n \"last-4-weeks\": 113345,\n \"last-6-months\": 961940,\n        \"last-12-months\": 961940\n      }\n    }\n  }\n}")
(def bartlesville-str
  "{\n  \"data\": {\n    \"city\": {\n      \"GR6\": {\n        \"last-week\": 1,\n        \"last-4-weeks\": 4,\n        \"last-6-months\": 4,\n        \"last-12-months\": 4\n      }\n    }\n  }\n}")
(def city-json-mp
  {
   "Cypress" "{\n  \"data\": {\n    \"emid\": {\n      \"mm020qj\": {\n        \"last-week\": 3,\n        \"last-4-weeks\": 44,\n        \"last-6-months\": 828,\n        \"last-12-months\": 828\n      }\n    },\n    \"city\": {\n      \"KCJ\": {\n        \"last-week\": 1,\n        \"last-4-weeks\": 7,\n        \"last-6-months\": 15,\n        \"last-12-months\": 15\n      }\n    }\n  }\n}"
   "Columbus" "{\n  \"data\": {\n \"city\": {\n \"G1A\": {\n \"last-week\": 91,\n \"last-4-weeks\": 1277,\n        \"last-6-months\": 5366,\n        \"last-12-months\": 5366\n      },\n \"Columbus, OH\": {\n \"last-week\": 24523,\n \"last-4-weeks\": 113345,\n \"last-6-months\": 961940,\n        \"last-12-months\": 961940\n      }\n    }\n  }\n}"
   "Bartlesville" "{\n  \"data\": {\n    \"city\": {\n      \"GR6\": {\n        \"last-week\": 1,\n        \"last-4-weeks\": 4,\n        \"last-6-months\": 4,\n        \"last-12-months\": 4\n      }\n    }\n  }\n}"
   })
(defn analytic-response [params]
  (let [key (params :city_name)]
    (println key)
    {:type :json
     :data (cjson/parse-string
            (city-json-mp key))}))
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
   [{:get "/superadmin/home"}
    (fn [params]
      {:type :html :data (views/superadmin-home)})]
   [{:get "/*/resources/:type/:resource"}
    (fn [params]
      {:type :js-resource
       :data (resources/fetch-resource params)})]
   [{:get "/pageviews/rolledup"}
    (fn [params]
      (println params)
      (analytic-response params))]
   ])
(defn extract-from-rest-uri-fn-lst [i]
  (vec
   (map
    (fn [n] (n i))
    rest-uri-fn-lst)))
(def rest-uris (extract-from-rest-uri-fn-lst 0))
(def rest-fns (extract-from-rest-uri-fn-lst 1))
(def rest-uri-lst
  (-> rest-uris routerutil/map-to-vec routerutil/flatten-vecs))
(defn route [request]
  (println request)
  (println (request :uri))
  (let [uri (request :uri)
        method (request :request-method)
        params (request :params)
        method-uri (routerutil/combine-request-method method uri)]
    (let [match-mp (routerutil/find-match rest-uri-lst method-uri)
          i (match-mp :index)]
                                        ;-1 indicates that no match was found for the given uri
      (println match-mp)
      (if (= -1 i)
        default-response
        ((rest-fns i)
         (routerutil/print-return
          (routerutil/keyword-merge-maps
           params
           (match-mp :params))))))))
