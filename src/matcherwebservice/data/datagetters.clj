(ns matcherwebservice.data.datagetters
  (:require [matcherwebservice.data.database :as db]))
(import java.util.UUID)
(defn makehash [un pw]
  (str "hasedup" un pw))
(defn get-people [adminhash]
  (db/read-from-db "Select people.* from pm.admin_users admin join pm.people people on people.admin_id = admin.admin_id where admin.admin_hash = ? "
   [adminhash]))
(defn attach-people [admin-lst]
  (map
   (fn [n]
     (assoc n :people (get-people (:admin_hash n))))
   admin-lst))
(defn get-admins []
  (attach-people
   (db/read-from-db "select * from pm.admin_users" [])))
(defn get-admin [adminhash]
  (first
   (attach-people
    (db/read-from-db
     "select * from pm.admin_users where admin_hash = ? "
     [adminhash]))))
(defn create-admin [username password]
  (db/write-to-db!
   :pm.admin_users
   {:admin_id (. UUID randomUUID)
    :admin_username username
    :admin_hash (makehash
                 username
                 password)}))
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
(defn get-groups [adminhash]
  (db/read-from-db "Select gr.* from pm.admin_users admin join pm.group gr on admin.admin_id = gr.admin_id where admin.admin_hash = ? "
                   [adminhash]))
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
