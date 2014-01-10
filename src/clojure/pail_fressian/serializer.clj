(ns pail-fressian.serializer
  "Defines a Pail serializer for Fressian objects."
  (:require [clojure.data.fressian :as fress]
            [pail-fressian.serialize :as s]
            [clj-pail.serializer :as pail]))


;; ## FressianSerializer

(defrecord ^{:doc "A Pail serializer for Fressian objects"}
  FressianSerializer
  [serializer deserializer]

  pail/Serializer
  (pail/serialize [this object]
    (s/serialize (:serializer this) object))

  (pail/deserialize [this buffer]
    (s/deserialize (:deserializer this) buffer)))


(defn fressian-serializer
  "Returns a `FressianSerializer`"
  []
   (FressianSerializer. (s/serializer)
                        (s/deserializer)))
