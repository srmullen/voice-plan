(ns voice-plan.utils
  (:require [clojure.data.priority-map :refer [priority-map]]))

(defn product [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (product (rest colls))]
      (cons x more))))

(defn queue
  ([] (clojure.lang.PersistentQueue/EMPTY))
  ([coll]
   (reduce conj (queue) coll)))

(defn priority-queue
  ([]
   (priority-map)))
