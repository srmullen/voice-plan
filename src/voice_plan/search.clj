(ns voice-plan.search
  (:require [voice-plan.planning-problem :refer [result]]))

; Perhaps this should be a Node protocol rather than something
; implemented by PlanningProblem.
(defprotocol Searchable
  (goal? [self state])
  (actions [self state])
  (path-cost [self c state1 action state2])
  (value [self]))

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
  [problem n]
  (let [state (:state n)]
    (map (fn [action]
           (node problem (result state action)))
         (actions (:problem n) state))))

(defn breadth-first [problem]
  (let [current (node problem)]
    (if (goal? problem (current state))
      node
      (loop [frontier (conj (clojure.lang.PersistentQueue/EMPTY) current)
             explored #{}]
        (if (not (empty? frontier))
          (let [next (peek frontier)
                rest (pop frontier)]
            (expand node)))))))
