# Mercator reads &amp; converts GPX and KML (soon) to GeoJSON

## Installation

Include in your `project.clj`

    [mercator "0.1.0"]

## Usage

    (-> "secret-hideout.gpx"
        (java.io.File.)
        (io/input-stream)
        (gpx/parse))

    ;; {:type "FeatureCollection"
    ;;   ...

