(defproject kelveden/mena "0.1.0-SNAPSHOT"
  :description "Simple mental arithmetic command line app"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.3.5"]]
  :profiles {:dev     {:plugins [[lein-binplus "0.6.3"]]}
             :uberjar {:aot :all}}
  :main mena.core)
