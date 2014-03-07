(ns pail-fressian.tapmapper
  "Defines Pail Tap mappers for vertically partitioned Pails of thrift objects")

;---- Tap Mappers ------
;Takes a thrift property_path vector and creates a valid partition path for that property path.
;   [ :location ["property" "location"]]
;
;
; Fressian taps - returns crap. because Fressian objects don't have a type we can use.
(defn fressian-taps
  "Fressian tap mapper that does nothing"
  [path]
   nil)

(defn fressian-tap-mapper
  "returns a fressian-tap-mapper"
  []
  fressian-taps)
