(ns matcherwebservice.views.views
  (:use hiccup.core))

(defn insert-javascript [nm]
  [:script {:type "text/javascript" :src nm}])
(defn core-javascript []
  [(insert-javascript "//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js")
   (insert-javascript "resources/javascript/underscore.js")])
(defn header []
  [:span {:class "links"}
   [:a {:href "/home"} "Home"]])
(defn login []
  [:html
   [:head]
   [:body
    [:br]
    [:span "Username" [:input {:type "text" :class "username"}]]
    [:br]
    [:span "Password" [:input {:type "text" :class "password"}]]
    [:br]
    [:button {:type "button" :class "login"} "login"]]])
(defn append [v content]
  (if (not (= content nil))
    (reduce
     (fn [r n] (conj r n))
     v
     content)
    v))
(defn buildhtml [mp]
  [:html
   [:head]
   (-> [:body]
       ((fn [n] (append n (core-javascript))))
       ((fn [n] (append n (mp :javascript))))
       ((fn [n] (append n (mp :body)))))])
(defn superadmin-home []
  (html
   (buildhtml
    {:javascript [(insert-javascript "resources/javascript/home.js")]
     :body [
            [:h1 "Super Admin HOME"]
            [:div {:class "content"}
             (header)
             [:div {:class "data"}]]
            ]})))
