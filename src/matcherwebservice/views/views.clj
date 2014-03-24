(ns matcherwebservice.views.views
  (:use hiccup.core))

(defn insert-javascript [nm]
  [:script {:type "text/javascript" :src nm}])
(defn header []
  [:span {:class "lonks"}
   [:a {:href "/home"} "Home"]])
(defn home []
  (html
   [:html
    [:head]
    [:body
     (insert-javascript "//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js")
     (insert-javascript "resources/javascript/underscore.js")
     (insert-javascript "resources/javascript/home.js")
     [:h1 "HOME"]
     [:div {:class "content"}
      (header)
      [:br]
      [:span "Username" [:input {:type "text" :class "username"}]]
      [:br]
      [:span "Password" [:input {:type "text" :class "password"}]]
      [:br]
      [:button {:type "button" :class "login"} "login"]
      [:div {:class "data"}]
      ]
     ]
    ]))
