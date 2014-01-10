(ns pail-fressian.serialize
  "Defines a Pail serialize for Fressian objects."
  (:require [clojure.data.fressian :as fress]
            [byte-streams :as bs]))


(defn serializer
  "Returns a Fressian serializer."
  []
  (fn [x] ( bs/to-byte-array (fress/write x))))

(defn deserializer
  "Returns a Fressian deserializer."
  []
  (fn [x] (fress/read (bs/to-byte-buffer x))))


;; ## Serialization

(defn serialize
  "Serializes a an object. If provided with a serializer, that serializer will be used.
  Otherwise, a default serializer will be constructed. The serializer can be curried into a closure
  to reuse the same instance and reduce stress on the garbage collector.

    (serialize object)
    (serialize (serializer) object)

    ; reuses serializer for each object being serialized
    (def f (partial serialize (serializer)))
    (map f objects)"
  ([object]
     (bs/to-byte-array (fress/write object)))
  ([serializer object]
    (serializer object)))

(defn deserialize
  "Deserializes an Fressian object from a byte array.

  One can provide a deserializer as the first argument to customize the serialization protocol. If
  it's not provided, the default deserializer will be used. The deserializer can be curried into a
  closure to reuse the same instance and reduce stress on the garbage collector.

    (deserialize Name buffer)
    (deserialize (deserializer) Name buffer)

    ; reuses deserializer for each object being deserialized
    (def f (partial deserialize (deserializer) Name))
    (map f buffers)"
  ([buffer]
     (fress/read (bs/to-byte-buffer buffer)))

  ([deserializer buffer]
                                        ; Hide from the user the fact that Thrift deserialization is impure.
     (deserializer buffer))

  #_(letfn [(deserialize-obj [object buffer]
             (deserializer object buffer))]
     (doto (.newInstance type)
       (deserialize-obj buffer))))
