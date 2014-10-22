(ns mercator.kml
  (:use [clojure.core.match :only [match]])

  (:require [clojure.zip :as zip]
            [clj-time.format :as time]
            [clojure.data.zip.xml :as xzip]
            [clojure.data.xml :as xml]))

(defn parse-double [d]
  (Double/parseDouble d))

(defn- coordinates [point]
  (let [[lon lat elevation] (clojure.string/split point #",")]
    (into []
          (remove nil? [(parse-double lon)
                        (parse-double lat)
                        (when elevation
                          (parse-double elevation))]))))

(defn- points-for [raw]
  (-> raw
    (clojure.string/trim)
    (clojure.string/split #"\s+")))

(defn- parse-coords [elem]
  (when elem
    (let [points (points-for (xzip/xml1-> elem :coordinates xzip/text))]
      (map coordinates points))))

(defn- feature-for-placemark [pm f]
  (let [geometry (parse-coords (xzip/xml1-> pm f))]
    (when geometry
      {:type "Feature"
       :properties {:name (xzip/xml1-> pm :name xzip/text)}
       :geometry {:type (name f)
                  :coordinates geometry}})))

(defn- placemark [pm]
  (first
    (remove nil?
            (map #(feature-for-placemark pm %1) [:LineString :Point]))))

(defn- features [xml]
  (let [kml (-> xml
              (xml/parse)
              (zip/xml-zip))]
    (map placemark (xzip/xml-> kml :Placemark))))

(defn parse [xml]
  {:type "FeatureCollection"
   :features (features xml)})

