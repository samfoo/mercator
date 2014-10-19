(defproject mercator "0.1.0"
  :description "Mercator parses KML, and GPX files into a geojson structure"
  :url "http://github.com/samfoo"
  :min-lein-version "2.1.2"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/core.match "0.2.1"]
                 [midje "1.6.3"]
                 [clj-time "0.8.0"]]

  :plugins [[lein-midje "3.1.3"]])
