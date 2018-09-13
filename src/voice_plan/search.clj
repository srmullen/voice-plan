(ns voice-plan.search
  (:require [voice-plan.planning-problem :refer [result actions goal?]]
            [voice-plan.utils :refer [queue]]
            [clojure.data.priority-map :as d]))

(defn node
  ([problem]
   (node problem (:init problem)))
  ([problem state]
   (node problem state nil nil nil))
  ([problem state parent action cost]
   {:problem problem
    :state state
    :parent parent
    :action action
    :cost cost}))

(defn expand
  "Return a list of nodes reachable in one step from the given node n."
  [n]
  (let [state (:state n)]
    (map (fn [action]
           (node (:problem n) (result state action) n action nil))
         (actions (:problem n) state))))

(defn breadth-first [problem]
  (loop [current (node problem)
         frontier (queue)
         explored #{}
         iteration 0]
    (if (or (goal? problem (current :state))
            (> iteration 1000))
      current
      (let [expansion (expand current)
            filtered (remove #(contains? explored (:state %))
                             expansion)
            new-frontier (into frontier filtered)]
        (if (not (empty? new-frontier))
          (recur (peek new-frontier)
            (pop new-frontier)
            (conj explored (current :state))
            (inc iteration)))))))

(defn depth-first [problem]
  (loop [current (node problem)
         frontier []
         explored #{}
         iteration 0]
    (if (or (goal? problem (current :state))
            (> iteration 1000))
      current
      (let [expansion (expand current)
            filtered (remove #(contains? explored (:state %))
                             expansion)
            new-frontier (into frontier filtered)]
        (if (not (empty? new-frontier))
          (recur (peek new-frontier)
            (pop new-frontier)
            (conj explored (current :state))
            (inc iteration)))))))

(defn uniform-cost [problem])

(defn a* [problem])

; (loop [q (d/priority-map :a 1 :b 1 :d 2 :c 1 :f 3 :e 3)]
;   (println (peek q))
;   (if (not (empty? q))
;     (recur (pop q))))
