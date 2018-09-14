(defproject voice-plan "0.1.0-SNAPSHOT"
  :description "Planning algorithms for voice leading"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [overtone "0.10.3"]
                 [leipzig "0.10.0"]
                 [org.clojure/core.logic "0.8.11"]
                 [proto-repl "0.3.1"]
                 [eftest "0.5.3"]
                 [org.clojure/data.priority-map "0.0.10"]]
  :main ^:skip-aot voice-plan.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
