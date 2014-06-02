(ns pail-fressian.fressian-pail-structure
  (:require [clj-pail-tap.structure :refer [gen-structure]]
            [pail-fressian.serializer :as s]
            [pail-fressian.partitioner :as p]
            [clj-pail-tap.tapmapper :as t])
  (:gen-class))

(gen-structure pail-fressian.FressianPailStructure
               :type (type {})
               :serializer (s/fressian-serializer)
               ;:partitioner (p/fressian-partitioner)
               :partitioner (p/fressian-property-partitioner)
               :tapmapper   (t/null-tapmapper)
               :property-path-generator (t/null-path-generator))
