(ns voice-plan.utils-test
  (:require [clojure.test :refer :all]
            [voice-plan.utils :refer :all]))

(deftest utils
  (testing "product return cartesian product"
    (is (= (product [["a" "b" "c"] [1 2 3]])
           [["a" 1] ["a" 2] ["a" 3]
            ["b" 1] ["b" 2] ["b" 3]
            ["c" 1] ["c" 2] ["c" 3]]))))
