(ns mercator.gpx-test
  (:require [mercator.gpx :as gpx]
            [clj-time.format :as time]
            [clojure.java.io :as io])
  (:use midje.sweet))

(defn t [s]
  (time/parse (time/formatters :date-time-no-ms) s))

(facts "about gpx parsing"
  (fact "a track can be extracted from a map"
    (-> "test/gpx/track.gpx"
      (io/resource)
      (io/input-stream)
      (gpx/parse))
    => {:type "FeatureCollection"
        :features [{:type "Feature"
                    :properties {:name "Arethusa Canyon (Alpheus Exit)"}
                    :geometry {:type "LineString"
                               :coordinates [[150.34908089786768
                                              -33.669379418715835
                                              895.12]
                                             [150.349082155153155
                                              -33.669372377917171
                                              898.97]]}}]}))

