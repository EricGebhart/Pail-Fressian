(ns pail-fressian.partitioner
  "Defines a Pail partitioner for Thrift unions."
  (:require [clj-pail.partitioner :as p]))


(defrecord ^{:doc "A pail partitioner for Fressian objects.
                  The partitioner will partition based on the first key in the object.
                  all objects with that same first keyword will go in that same directory."}

  FressianPartitioner
  []

  p/VerticalPartitioner
  (p/make-partition
    [this object]
    (vector (name (first (keys object)))))

  ; Anything is ok for now.
  (p/validate
    [this dirs]
    [true (rest dirs)]))

(defn fressian-partitioner
  "Returns a `FressianPartitioner`."
  []
  (FressianPartitioner.))


(defrecord ^{:doc "A pail partitioner for Fressian objects.
                  This partitioner will partition based on datatype. If the name of the datatype
                  ends in [Pp]roperty the partitioner will look at the type found in that data type's
                  :property field, and add that name to the path as well."}

  FressianPropertyPartitioner
  []

  p/VerticalPartitioner
  (p/make-partition
    [this object]
    (let [res (vector (name (type object)))]
      (if (re-find #"^.*[Pp]roperty$" (first res))
        (let [prop (name (type (:property object)))]
          (conj res prop))
        res)))

  ; Anything is ok for now.
  (p/validate
    [this dirs]
    [true (rest dirs)]))

(defn fressian-property-partitioner
  "Returns a `FressianPropertyPartitioner`."
  []
  (FressianPropertyPartitioner.))
