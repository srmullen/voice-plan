(ns voice-plan.utils)

(defn product [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (product (rest colls))]
      (cons x more))))
