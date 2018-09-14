(ns voice-plan.music
  (:require [voice-plan.planning :refer :all]
            [overtone.live :refer :all]
            [clojure.core.async
             :as async
             :refer [>! <! >!! <!! go chan close!]]
            [voice-plan.instruments :refer [syn]]))

;
; What are the actions for voice leading and melodies? They are steps
; of a certain degree/interval. The effect of the step depends on where
; the step was taken from.

; It's not clear what the pre and post conditions should be here.
; (def whole-step-up (action (expr :step 1)
;                            {}))
; (def whole-step-down (action (expr :step -1)
;                              {}))

; Have each note as an action. The preconditions can be notes the voice is
; allowed to move from.

(defn step-action
  "Given a step and a note value, returns an action that takes
  moves by the given step to the note."
  [step note]
  (let [previous (expr :at (+ note step))]
   (action (expr :move note)
           {:positive #{previous}
            :add      #{(expr :at note)}
            :remove   #{previous}})))

(defn play-expr [expr time]
  (let [midi (first (:args expr))]
    (at time (syn (midi->hz midi)))))

; Playing a melody from an action set
(defn noodle []
  (let [action-set (into #{} (mapcat (juxt (partial step-action -5)
                                           (partial step-action 5)
                                           (partial step-action 1)))
                             (range 30 100))
        start-note (+ 30 (rand-int 70))
        duration 200
        iterations 200
        time (now)]
    (loop [iteration 0
           state #{(expr :at start-note)}]
      (let [note (first state)
            midi (first (:args note))]
        (at (+ time (* iteration duration))
            (syn (midi->hz midi))))
      (if (not (> iteration iterations))
        (recur (inc iteration)
          (result state (rand-nth (into [] (actions action-set state)))))))))

(comment
  (noodle)
  (syn))
