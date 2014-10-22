# Mercator reads GPX and KML

... into a clojure persistent map in the format of GeoJSON.

## Installation

Include in your `project.clj`

    [mercator "0.1.1"]

## Usage

    (require 'mercator.gpx)

    (-> "secret-hideout.gpx"
        (java.io.File.)
        (io/input-stream)
        (mercator.gpx/parse))

    ;; {:type "FeatureCollection"
    ;;  :features [{:type "Feature"
    ;;              :properties {:name "Way to Mordor"}
    ;;              :geometry {:type "LineString"
    ;;                         :coordinates [[90.86062421614008
    ;;                                        34.9328621088893
    ;;                                        0.0]
    ;;                                       [90.86092208681491
    ;;                                        24.93293237320851
    ;;                                        0.0]]}}]}
