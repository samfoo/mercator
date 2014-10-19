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
    [(parse-double lon)
     (parse-double lat)
     (parse-double elevation)]))

(defn- track [trk]
  (let [points (xzip/xml-> trk :trkseg :trkpt)]
    {:type "Feature"
     :properties {:name (xzip/xml1-> trk
                                     :name xzip/text)}
     :geometry {:type "LineString"
                :coordinates (map coordinates
                                  (map zip/node points))}}))

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
