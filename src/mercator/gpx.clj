(ns mercator.gpx
  (:use [clojure.core.match :only [match]])

  (:require [clojure.zip :as zip]
            [clj-time.format :as time]
            [clojure.data.zip.xml :as xzip]
            [clojure.data.xml :as xml]))

(defn parse-double [d]
  (Double/parseDouble d))

(defn- coordinates [point]
  (let [lon (get-in point [:attrs :lon])
        lat (get-in point [:attrs :lat])
        elevation (first
                    (xzip/xml-> (zip/xml-zip point) :ele xzip/text))]
    (into []
          (remove nil?  [(parse-double lon)
                         (parse-double lat)
                         (when elevation
                           (parse-double elevation))]))))

(defn- segment-coords [seg]
  (let [points (xzip/xml-> seg :trkpt)]
    (map coordinates
         (map zip/node points))))

(defn- single-segment [trk]
  (let [segment (xzip/xml1-> trk :trkseg)]
     {:type "LineString"
      :coordinates (segment-coords segment)}))

(defn- multi-segment [trk]
  (let [segments (xzip/xml-> trk :trkseg)]
    {:type "MultiLineString"
     :coordinates (map segment-coords segments)}))

(defn- track [trk]
  (let [segments (xzip/xml-> trk :trkseg)]
    {:type "Feature"
     :properties {:name (xzip/xml1-> trk
                                     :name xzip/text)}
     :geometry (if (= (count segments) 1)
                 (single-segment trk)
                 (multi-segment trk))}))

(defn- gpx->geojson [feature]
  (match (:tag feature)
    [:trk] (track feature)))

(defn- features [xml]
  (let [gpx (-> xml
              (xml/parse)
              (zip/xml-zip))]
    (map track (xzip/xml-> gpx :trk))))

(defn parse [xml]
  {:type "FeatureCollection"
   :features (features xml)})
