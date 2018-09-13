(ns voice-plan.utils)

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
