(defproject matcherwebservice "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [ring/ring-json "0.2.0"]
                 [org.apache.openejb/openejb-bonecp "4.6.0"]
                 [postgresql "9.1-901.jdbc4"]
                 [org.clojure/java.jdbc "0.3.2"]
                 [cheshire "5.3.1"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler matcherwebservice.core/app}
  :target-path "target/%s")
