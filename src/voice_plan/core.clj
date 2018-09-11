(ns voice-plan.core
  (:require ;[overtone.live :as o]
            [clojure.core.logic :as l])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(l/run* [q]
        (l/membero q [1 2 3 4])
        (l/membero q [2 3 4]))

(l/run* [q p]
        (l/== q p)
        (l/membero q [2 3 4 5])
        (l/membero p [4 5 6]))

(l/run* [q]
  (l/fresh [a]
   (l/membero q [1 2 3])
   (l/membero a [3 4 5])
   (l/== a q)))

(l/run* [q]
        (l/conde
         [(l/== q (/ 5.0 2.0))]
         [(l/== q 2)]))

(l/run* [q]
        (l/conde
         [(l/conso 1 [2 3] q)]
         [(l/conso 4 [2 3] q)]))

(l/run* [q]
        (l/resto [1 q 3 4] [1 2 3 4]))
