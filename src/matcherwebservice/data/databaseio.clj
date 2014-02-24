(ns matcherwebservice.data.databaseio
  (:require [matcherwebservice.data.database :as db])
  (:require [matcherwebservice.util.utils :as utils]))
(import java.util.UUID)
(defn makehash [un pw]
  (str "hasedup" un pw))
(defn get-people [adminhash]
  (db/read-from-db "Select people.* from pm.admin_users admin join pm.people people on people.admin_id = admin.admin_id where admin.admin_hash = ? "
   [adminhash]))
(defn get-groups [adminhash]
  (db/read-from-db "Select gr.* from pm.admin_users admin join pm.group gr on admin.admin_id = gr.admin_id where admin.admin_hash = ? "
                   [adminhash]))
(defn attach-children [parent-lst child-fn]
  (map
   child-fn
   parent-lst))
(defn attach-people [admin-lst]
  (attach-children
   admin-lst
   (fn [n]
     (assoc n :people (get-people (:admin_hash n))))))
(defn attach-groups [admin-lst]
  (attach-children
   admin-lst
   (fn [n]
     (assoc n :groups (get-groups (:admin_hash n))))))
(defn get-admins []
  (->
   (db/read-from-db "select * from pm.admin_users" [])
   attach-people
   attach-groups))
(defn get-admin [adminhash]
  (first
   (->
    (db/read-from-db
     "select * from pm.admin_users where admin_hash = ? "
     [adminhash])
    attach-people
    attach-groups)))
(defn create-admin [username password]
  (db/write-to-db!
   :pm.admin_users
   {:admin_id (. UUID randomUUID)
    :admin_username username
    :admin_hash (makehash
                 username
                 password)}))
(defn delete-admin [adminhash]
  (db/delete!
   "DELETE from pm.admin_users where admin_hash = ? "
   [adminhash]))
(defn get-person [adminhash name]
  (first
   (db/read-from-db "Select people.* from pm.admin_users admin join pm.people people on people.admin_id = admin.admin_id where admin.admin_hash = ? and people.name = ? "
                    [adminhash name])))
(defn create-person [adminhash name email]
  (let [admin-id (:admin_id (get-admin adminhash))]
   (if (not (= nil admin-id))
     (db/write-to-db!
      :pm.people
      {:admin_id admin-id
       :people_id (. UUID randomUUID)
       :name name
       :email email})
     {"error" "admin does not exist"})))
(defn get-group [adminhash name]
  (first
   (db/read-from-db "Select gr.* from")))
(defn create-group [adminhash name]
  (let [admin-id (:admin_id (get-admin adminhash))]
   (if (not (= nil admin-id))
     (db/write-to-db!
      :pm.group
      {:admin_id admin-id
       :group_id (. UUID randomUUID)
       :group_description name
       })
     {"error" "admin does not exist"})))
