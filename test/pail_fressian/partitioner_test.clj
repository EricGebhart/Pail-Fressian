(ns pail-thrift.partitioner-test
  (:require [pail-fressian.partitioner :as p]
            [clj-pail.partitioner :as pail])
  (:use midje.sweet))

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

(facts "UnionPartitioner"
  (let [partitioner (p/fressian-partitioner)]

    (tabular "make a partition for the current structure"
           (fact
            (pail/make-partition partitioner ?attributes) => ?result)

           ?attributes                ?result
           {:name "Eric" :property
            {:phone "555-555-5555"}}   ["name"]

           {:location
            {:city "Asheville"}}       ["location"]

           {:firstName "Eric"
            :lastName "Gebhart"}       ["firstName"])))


(facts "UnionPropertyPartitioner"
       (let [partitioner (p/fressian-property-partitioner)]

         (tabular "make a partition for the current structure"
            (fact (pail/make-partition partitioner ?attributes) => ?result)

                ?attributes                                               ?result
                (person-property "123" (first-name "Eric"))               ["PersonProperty" "FirstName"]
                (person-property "123" (last-name "Gebhart"))             ["PersonProperty" "LastName"]
                (person-property "123" (location {:address "1 Pack Place"
                                                  :city "Asheville"
                                                  :state "NC"}))          ["PersonProperty" "Location"]
                (person-property "123" (first-name "frederick"))          ["PersonProperty" "FirstName"]
                (person-property "123" (last-name "Gebhart"))             ["PersonProperty" "LastName"]
                (person-property "123" (location {:address "1 Wall Street"
                                                :city "Asheville"
                                                :state "NC"})))           ["PersonProperty" "Location"]))
