(ns mercator.gpx-test
  (:require [mercator.gpx :as gpx]
            [clj-time.format :as time]
            [clojure.java.io :as io])
  (:use midje.sweet))

(facts "about gpx parsing"
  (fact "a route can be extracted as a line string feature"
    (-> "test/gpx/route.gpx"
      (io/resource)
      (io/input-stream)
      (gpx/parse))
    => {:type "FeatureCollection"
        :features [{:type "Feature"
                    :properties {:name "Way to Mordor"}
                    :geometry {:type "LineString"
                               :coordinates [[90.86062421614008
                                              34.9328621088893
                                              0.0]
                                             [90.86092208681491
                                              24.93293237320851
                                              0.0]]}}]})

  (fact "a waypoint can be extracted as a point feature"
    (-> "test/gpx/waypoint.gpx"
      (io/resource)
      (io/input-stream)
      (gpx/parse))
    => {:type "FeatureCollection"
        :features [{:type "Feature"
                    :properties {:name "End of Starlight Tunnel"}
                    :geometry {:type "Point"
                               :coordinates [150.28315999545157,
                                             -33.15484598278999,
                                             684.0]}}]})

  (fact "a track with more than on segment is a multi line string feature"
    (-> "test/gpx/track-segs.gpx"
      (io/resource)
      (io/input-stream)
      (gpx/parse)
      (:features)
      (first)
      (:geometry))
    => {:type "MultiLineString"
        :coordinates [[[150.34908089786768
                        -33.669379418715835]]
                      [[150.349082155153155
                        -33.669372377917171]]]})

  (fact "track points without elevation are only lon / lat coords"
    (-> "test/gpx/track-no-ele.gpx"
      (io/resource)
      (io/input-stream)
      (gpx/parse)
      (:features)
      (first)
      (get-in [:geometry :coordinates]))
    => [[150.34908089786768
         -33.669379418715835]
        [150.349082155153155
         -33.669372377917171]])

  (fact "a track can be extracted as a line string feature"
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

