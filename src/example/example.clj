(ns pail-fressian.example
  (:require
   [clj-pail.core :as pl]
   [pail-cascalog.core :as pcas]
   [byte-streams :as bs]
   [clojure.data.fressian :as fress])
  (:use cascalog.api)
  (:import [pail-fressian FressianPailStructure]))


;; data
(defn person-property [id property]
  ^{:type ::PersonProperty}
  {:id id :property property})

(defn first-name [name]
  ^{:type ::FirstName}
  {:first_name name})

(defn last-name [name]
  ^{:type ::LastName}
  {:last_name name})

(defn location [{:keys [address city county state country zip]}]
  ^{:type ::Location}
  {:address address :city city :county county :state state :country country :zip zip})

(defn friendshipedge [id1 id2]
  ^{:type ::friendshipedge}
  {:id1 id1 :id2 id2})


(def du1-1 (person-property "123" (first-name "Eric")))
(def du1-2 (person-property "123" (last-name "Gebhart")))
(def du1-3 (person-property "123" (location {:address "1 Pack Place"
                                                   :city "Asheville"
                                                   :state "NC"})))
(def du2-1 (person-property "123" (first-name "Frederick")))
(def du2-2 (person-property "123" (last-name "Gebhart")))
(def du2-3 (person-property "123" (location {:address "1 Wall Street"
                                                   :city "Asheville"
                                                   :state "NC"})))
(def du3 ( friendshipedge "123" "abc"))

(def objectlist [du1-1 du1-2 du1-3 du2-1 du2-2 du2-3 du3])

(defn find-or-create [pstruct path & {:as create-key-args}]
  "Get a pail from a path, or create one if not found"
  (try (pl/pail path)
       (catch Exception e
          (apply pl/create (pl/spec pstruct) path (mapcat identity create-key-args)))))

(defn write-objects
  "Write a list of objects to a pail"
  [pail objects]
  (with-open [writer (.openWrite pail)]
    (doseq [o objects]
      (.writeObject writer o))))

(def pail-struct (FressianPailStructure.))
(def mypail (find-or-create pail-struct "example_pail"))

; partitioner is working.
(.getTarget pail-struct du1-1)

;serializer looks right.
(.getSerializer pail-struct)

;get the  serializer and serialize something.
((:serializer (.getSerializer pail-struct)) du1-1)

;the easy way.
(.serialize pail-struct du1-1)

;round trip.
(-> du1-1
    (fress/write)
    (fress/read))

;round trip with the serializer - good.
(-> du1-1
    ((:serializer (.getSerializer pail-struct)))
    ((:deserializer (.getSerializer pail-struct))))

; write them to the pail.
(write-objects mypail objectlist)

(defmapfn sprop [du]
    "Deconstruct a property object"
    (into [(:id du)] (vals (:property du))))

(sprop du1-1)
(sprop du1-2)
(sprop du1-3)

; set up function to get cascalog taps.
(defn prop-tap [pail-connection] (pcas/pail->tap pail-connection :attributes [["PersonProperty"]]))
(defn fn-tap [pail-connection] (pcas/pail->tap pail-connection :attributes [["PersonProperty" "FirstName"]]))
(defn ln-tap [pail-connection] (pcas/pail->tap pail-connection :attributes [["PersonProperty" "LastName"]]))
(defn loc-tap [pail-connection] (pcas/pail->tap pail-connection :attributes [["PersonProperty" "Location"]]))

(defn raw-query [pail-connection]
  (let [ptap (prop-tap pail-connection)]
    (??<- [?data] (ptap _ ?data))))

(defn get-names [pail-connection]
    (let [fntap (fn-tap pail-connection)]
      (??<- [?id ?first-name]
            (fntap _ ?fn-data)
            (sprop ?fn-data :> ?id ?first-name))))


(defn get-full-names [pail-connection]
  (let [fntap (fn-tap pail-connection)
        lntap (ln-tap pail-connection)]
    (??<- [?first-name ?last-name]
          (fntap _ ?fn-data)
          (lntap _ ?ln-data)
          (sprop ?fn-data :> ?id ?first-name)
          (sprop ?ln-data :> ?id ?last-name))))

(defn get-everything [pail-connection]
  (let [fntap (fn-tap pail-connection)
        lntap (ln-tap pail-connection)
        loctap (loc-tap pail-connection)]
    (??<- [?first-name ?last-name !address !city !county !state !country !zip]
          (fntap _ ?fn-data)
          (lntap _ ?ln-data)
          (loctap _ ?loc-data)
          (sprop ?fn-data :> ?id ?first-name)
          (sprop ?ln-data :> ?id ?last-name)
          (sprop ?loc-data :> ?id !address !city !county !state !country !zip))))

(defn -main
  "I don't do a whole lot."
  [x]
  (println "Hello, World!"))
