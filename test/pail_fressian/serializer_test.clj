(ns pail-fressian.serializer-test
  (:require [pail-fressian.serializer :as s]
            [clojure.data.fressian :as fress]
            [byte-streams :as bs]
            [clj-pail.serializer :as pail])
  (:use midje.sweet))


(facts "FressianSerializer"

  (facts "serialize"
    (tabular "serializes"
      (let [object ?object
            pail-serializer (s/fressian-serializer)]
        (seq (pail/serialize pail-serializer object)) => (seq (bs/to-byte-array (fress/write object))))

      ?object
      [:Name "John" :last-name "Doe"]
      [:Name "Joe" :last-name "Example"]))



  (facts "deserialize"
    (tabular "deserializes"
      (let [object ?object
            pail-serializer (s/fressian-serializer)]

        (pail/deserialize pail-serializer (bs/to-byte-array (fress/write object))) => object)

      ?object
      [:Name "John" :last_name "Doe"]
      [:Name "Joe" :last_name "Example"])))
