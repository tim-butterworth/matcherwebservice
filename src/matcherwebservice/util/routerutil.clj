(ns matcherwebservice.util.routerutil)
(defn uri-split [uri]
  (vec
   (filter
    (fn [n] (not (= "" n)))
    (clojure.string/split uri #"/"))))
(defn is-param [key]
  (if (> (. key length) 0)
    (= ":" (. key subSequence 0 1))
    false))
(defn safe-keyword [key]
  (if (keyword? key)
    key
    (if (is-param key)
      (keyword (. key substring 1))
      (keyword key))))
(defn assoc-path-param [mp val key]
  (if (is-param key)
    (assoc mp (safe-keyword key) val)
    mp))
(defn to-uri-list [lst]
  (vec
   (rest
    (uri-split lst))))
(defn prep-uri [uri mtch]
  uri)
(defn get-params [in mtch]
  (let [incoming (prep-uri in mtch)
        ilist (to-uri-list incoming)
        blist (to-uri-list mtch)]
    (loop
        [result {} i 0]
      (if (< i (count ilist))
        (let
            [mp (assoc-path-param result (ilist i) (blist i))]
          (recur mp (inc i)))
        result))))
(defn uri-matches [in option]
  (let [incoming (prep-uri in option)]
    (if (= (count incoming) (count option))
      (reduce (fn [result n] (if result n result)) true
              (map
               (fn [i o] (or
                          (= i o)
                          (is-param o)))
               incoming
               option))
      false)))
(defn find-match [urilist uri]
  (loop [i 0]
    (if (< i (count urilist))
      (if (uri-matches (uri-split uri) (uri-split (urilist i)))
        i
        (recur (inc i)))
     -1)))
;try to make this tail recursive I guess
(defn flatten-lists-vecs [lst i result]
  (if (< i (count lst))
      (let [c (lst i)]
        (if (= (. c getClass) (. [] getClass))
          (flatten-lists-vecs lst (inc i) (flatten-lists-vecs c 0 result))
          (flatten-lists-vecs lst (inc i) (conj result c))))
      result))
(defn flatten-vecs [lst]
  (flatten-lists-vecs lst 0 []))
(defn map-to-vec [uri-mp]
  (vec (map
        (fn [m] (vec
                 (map
                  (fn [k] (str (name k) (m k)))
                  (keys m))))
        uri-mp)))
(defn print-return [v]
  (println v)
  v)
(defn combine-request-method [method uri]
  (str (name method) uri))
(defn keywordize [mp]
  (reduce (fn [r key] (assoc r (safe-keyword key) (mp key))) {} (keys mp)))
(defn keyword-merge-maps [path request]
  (clojure.core/merge (keywordize path) (keywordize request)))
(defn makehash [un pw]
  (str un pw))
