(defproject pail-fressian "0.1.0"
  :description "Serialization and partitioning strategies for using Fressian with clj-pail."
  :url "https://github.com/EricGebhart/pail-fressian"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :min-lein-version "2.0.0"

  :source-paths ["src/clojure"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.apache.hadoop/hadoop-core "1.2.0" ]
                 [cascalog "2.0.0" ]
                 [clj-pail "0.1.3"]
                 [byte-streams "0.1.7"]
                 [pail-cascalog "0.1.0"]
                 [org.clojure/data.fressian "0.2.0"]]

  :aot [pail-fressian.fressian-pail-structure]
  :profiles {:1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0-master-SNAPSHOT"]]}

             :dev {:dependencies [[midje "1.5.1"]]
                   :plugins [[lein-midje "3.0.1"]]
                   :prep-tasks ["javac"]}
             }

  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo" :username :gpg :password :gpg}]
                        ["snapshots" {:url "https://clojars.org/repo" :username :gpg :password :gpg}]])
