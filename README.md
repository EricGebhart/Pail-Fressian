# pail-fressian

Serialization and partitioning strategies for using [`data.Fressian`](https://github.com/clojure/data.fressian) with
[`clj-pail`](https://github.com/dcuddeback/clj-pail).  This library is modeled after [`pail-thrift`](https://github.com/dcuddeback/clj-pail)
which it shares many commonalities. Serialization is not currently set up to use different Fressian handlers and unlike pail-thrift there are
no protocols to be had.

## Usage

Add `pail-fressian` to your project's dependencies. If you're using Leiningen, your `project.clj`
should look something like this:

~~~clojure
(defproject ...
  :dependencies [[pail-fressian VERSION]])
~~~

Where `VERSION` is the latest version on [Clojars](https://clojars.org/pail-fressian).

### Defining a `PailStructure`

`PailStructure` classes are defined with the `gen-structure` macro from `clj-pail`. `pail-fressian`
provides serializers and partitioners that can be used with the `gen-structure` macro.

~~~clojure
(ns example.pail
  (:require [clj-pail.structure :refer [gen-structure]]
            [pail-fressian.serializer :as s]
            [pail-fressian.partitioner :as p])
  (:gen-class))

(gen-structure example.pail.PailStructure
               :serializer (s/fressian-serializer)
               :partitioner (p/fressian-partitioner))
~~~

In the above example, we define a `PailStructure` that can serialize any native data type using
Fressian read and write. The `PailStructure` will also be vertically
partitioned by the first field of each data object.

#### Vertical Partitioning

A `PailStructure` is vertically partitioned according to the partitioner supplied as the
`:partitioner` keyword argument of `gen-structure`. `pail-fressian` provides somewhat generic partitioners, but
you will most likely want to create a partitioner specific to your application.

Generalized partitioners are defined in
[`pail-fressian.partitioner`](src/clojure/pail_fressian/partitioner.clj). Currently, there are 2 partitioners. Both of
them are somewhat contrived.

One uses the first property name of the data object to create a single level partitioning scheme.
The other uses the type name of the data object, while also
looking specifically for anything ending in [Pp]roperty, When found it looks for a possible second level of partitioning in
a :property field. Due to the open nature of a fressian pail these partitioners are truly only examples of how to proceed
in making your own application specific partitioner. Both partitioners short-circuit partition validation as almost anything
goes here, and there is no type to check.

If you have ideas about better generic partitioners, validation or improvements please fork me.


## License

Copyright © 2014 Eric Gebhart

Distributed under the [MIT License](LICENSE).
