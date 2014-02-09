(ns matcherwebservice.router
  (:require [matcherwebservice.data.database :as db])
  (:require [matcherwebservice.util.routerutil :as routerutil])
  (:require [matcherwebservice.data.datagetters :as data]))
(import java.util.UUID)

(defn destruct [in-uri]
  (filter
   (fn [n]
     (not
      (= n "")))
   (clojure.string/split in-uri #"/")))
(def rest-uri-fn-lst
  [[{:get "/admin"}
    (fn [params] (data/get-admins))]
   [{:get "/admin/:userhash"}
    (fn [params]
      (data/get-admin (params :userhash)))]
   [{:post "/admin/:username"}
    (fn [params] (data/create-admin (params :username) (params :password)))]
   [{:put "/admin"}
    (fn [params] "")]
   [{:delete "/admin/:userhash"}
    (fn [params]
      (db/delete! "DELETE from pm.admin_users where admin_hash = ? "
                  [(params :userhash)]))]
   [{:get "/admin/:userhash/people"}
    (fn [params]
      (data/get-people (params :userhash)))]
   [{:get "/admin/:userhash/person/:name"}
    (fn [params]
      (data/get-person (params :userhash) (params :name)))]
   [{:post "/admin/:userhash/person/:name"}
    (fn [params]
      (data/create-person (params :userhash) (params :name) (params :email)))]
   [{:post "/admin/:userhash/group/:name"}
    (fn [params]
      (data/create-group (params :userhash) (params :name)))]
   [{:get "/admin/:userhash/groups"}
    (fn [params]
      (data/get-groups (params :userhash)))]
   ])
(defn extract-from-rest-uri-fn-lst [i]
  (vec
   (map
    (fn [n] (n i))
    rest-uri-fn-lst)))
(def rest-uris (extract-from-rest-uri-fn-lst 0))
(def rest-fns (extract-from-rest-uri-fn-lst 1))
(def default-response
  {"ok" true
   "name" "people match service"})
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
