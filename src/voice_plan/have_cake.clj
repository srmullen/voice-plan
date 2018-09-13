(ns voice-plan.have-cake
  (:use [voice-plan.planning :refer [expr action planning-problem negate]]
        [voice-plan.utils])
  (:require [voice-plan.search :as search]
            [clojure.data.priority-map :refer [priority-map]]))

(defn get-actions []
  (let [precond-pos [(expr :Have :Cake)]
        precond-neg []
        effect-add [(expr :Eaten :Cake)]
        effect-rem [(expr :Have :Cake) (negate (expr :Eaten :Cake))]
        eat-action (action (expr :Eat :Cake)
                           [precond-pos precond-neg]
                           [effect-add effect-rem])
        precond-pos []
        precond-neg [(expr :Have :Cake)]
        effect-add [(expr :Have :Cake)]
        effect-rem []
        bake-action (action (expr :Bake :Cake)
                            [precond-pos precond-neg]
                            [effect-add effect-rem])]
    #{eat-action bake-action}))

(defn have-cake []
  (planning-problem [(expr :Have :Cake) (negate (expr :Eaten :Cake))]
                    [(expr :Have :Cake) (expr :Eaten :Cake)]
                    (get-actions)))

(def problem (have-cake))

(defn get-solution [tree]
  (loop [node tree
         actions '()]
    (if node
      (recur (:parent node)
        (conj actions (:action node)))
      (pop actions))))

; (get-solution (search/uniform-cost (have-cake)))
; (get-solution (search/breadth-first (have-cake)))

(comment
  (println *e)

  (into (priority-map)
        (map (juxt identity (constantly 1)) ["hello" {:hello :world}])))
