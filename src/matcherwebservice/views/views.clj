(ns matcherwebservice.views.views)

(defn home []
  (clojure.string/join
   ["<html><body>"
    "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js\"></script>"
    "<script src=\"resources/json/underscore.js\"></script>"
    "<script src=\"resources/json/home.js\"></script>"
    "<a href='https://gist.github.com/ewilson/f27ba22368c35d101d86'>https://gist.github.com/ewilson/f27ba22368c35d101d86</h1>"
    "</body></html>"]))
