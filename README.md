# pail-thrift

[![Build Status](https://travis-ci.org/dcuddeback/pail-thrift.png?branch=master)](https://travis-ci.org/dcuddeback/pail-thrift)

Serialization and partitioning strategies for using [Thrift](https://thrift.apache.org/) with
[`clj-pail`](https://github.com/dcuddeback/clj-pail). Makes defining `PailStructure` classes for
Thrift schemas easier.

## Usage

Add `pail-thrift` to your project's dependencies. If you're using Leiningen, your `project.clj`
should look something like this:

~~~clojure
(defproject ...
  :dependencies [[pail-thrift VERSION]])
~~~

Where `VERSION` is the latest version on [Clojars](https://clojars.org/pail-thrift).

### Defining a `PailStructure`

`PailStructure` classes are defined with the `gen-structure` macro from `clj-pail`. `pail-thrift`
provides serializers and partitioners that can be used with the `gen-structure` macro.

~~~clojure
(ns example.pail
  (:require [clj-pail.structure :refer [gen-structure]]
            [pail-thrift.serializer :as s]
            [pail-thrift.partitioner :as p])
  (:import (example.thrift MyUnion))
  (:gen-class))

(gen-structure example.pail.PailStructure
               :type MyUnion
               :serializer (s/thrift-serializer MyUnion)
               :partitioner (p/union-partitioner MyUnion))
~~~

In the above example, we define a `PailStructure` that serializes the `example.thrift.MyUnion` type
using the default Thrift serialization protocol. The `PailStructure` will also be vertically
partitioned by the active field of each union.

#### Controlling the Serialization Protocol

The previous example uses the default Thrift serialization protocol. The protocol can be specified
as an additional argument to the `thrift-serializer` function. The protocols are defined in
[`clj-thrift`](https://github.com/dcuddeback/clj-thrift):

~~~clojure
(require '[clj-thrift.protocol.factory :as protocol])

(s/thrift-serializer MyUnion (protocol/compact))
~~~

#### Vertical Partitioning

A `PailStructure` is vertically partitioned according to the partitioner supplied as the
`:partitioner` keyword argument of `gen-structure`. `pail-thrift` provides generic partitioners, but
you may want the partitioner to be specific to your application. For this reason, the generalized
partitioners are designed to be composed by application-specific ones.

Generalized partitioners are defined in
[`pail-thrift.partitioner`](src/clojure/pail_thrift/partitioner.clj). Currently, there are 4 partitioners.
Two that use field names, two that use field id's. Two of which that read one level down into any field ending
in 'property' and containing a union to create a second level of partitioning.


## License

Copyright © 2013 David Cuddeback

Distributed under the [MIT License](LICENSE).
