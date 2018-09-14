(ns voice-plan.have-cake
  (:use [voice-plan.utils])
  (:require [voice-plan.planning :refer [expr action negate]]
            [voice-plan.planning :as planning]
            [voice-plan.search :as search]
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

; (get-solution (search/uniform-cost (have-cake)))
(def problem {:goal? (partial planning/goal? #{(expr :Have :Cake)
                                               (expr :Eaten :Cake)})
              :result planning/result
              :actions (partial planning/actions (get-actions))
              :cost (constantly 1)})
(def init #{(expr :Have :Cake) (negate (expr :Eaten :Cake))})

(search/get-solution (search/uniform-cost problem init))
