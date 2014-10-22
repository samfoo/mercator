(ns mercator.kml-test
  (:require [mercator.kml :as kml]
            [clj-time.format :as time]
            [clojure.java.io :as io])
  (:use midje.sweet))

(facts "about kml parsing"
  (fact "a point is extracted as a point feature"
    (-> "test/kml/waypoint.kml"
      (io/resource)
      (io/input-stream)
      (kml/parse))
    => {:type "FeatureCollection"
        :features [{:type "Feature"
                    :properties {:name "Red Team's Flag"}
                    :geometry {:type "Point"
                               :coordinates [[-86.1296598
                                              38.8760649
                                              260.0]]}}]})

  (fact "a line string is extracted as a line string feature"
    (-> "test/kml/line-string.kml"
      (io/resource)
      (io/input-stream)
      (kml/parse))
    => {:type "FeatureCollection"
        :features [{:type "Feature"
                    :properties {:name "Secret Location of Death Star"}
                    :geometry {:type "LineString"
                               :coordinates [[-86.1296598
                                              38.8760649
                                              260.0]
                                             [-97.7430608
                                              30.267153
                                              260.0]]}}]}))
