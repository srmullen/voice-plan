(ns voice-plan.music
  (:require [voice-plan.planning :refer :all]
            [overtone.live :refer :all]))

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
(defn whole-step-up [note]
  (let [previous (expr :at (- note 2))]
   (action (expr :move note)
           {:positive #{previous}
            :add      #{(expr :at note)}
            :remove   #{previous}})))

(defn whole-step-down [note]
  (let [previous (expr :at (+ note 2))]
   (action (expr :move note)
           {:positive #{previous}
            :add      #{(expr :at note)}
            :remove   #{previous}})))

(defn step-action
  "Given a step and a note value, returns an action that takes
  moves by the given step to the note."
  [step note]
  (let [previous (expr :at (+ note step))]
   (action (expr :move note)
           {:positive #{previous}
            :add      #{(expr :at note)}
            :remove   #{previous}})))

(def action-set
  (into #{} (mapcat (juxt (partial step-action 1) (partial step-action -1))
                    (range 50 70))))

(def problem (planning-problem [] [] action-set))

(defsynth syn [freq 440]
  (out 0 (pan2 (* (env-gen (perc) :action FREE)
                  (sin-osc freq)))))

; Playing a melody from an action set
(defn noodle []
  (let [action-set (into #{} (mapcat (juxt (partial step-action 1)
                                           (partial step-action -1)
                                           (partial step-action 5)
                                           (partial step-action -6)))
                             (range 30 100))
        problem (planning-problem [] [] action-set)
        start-note (+ 30 (rand-int 70))
        duration 100
        iterations 200]
    (loop [iteration 0
           state #{(expr :at start-note)}]
      (let [note (first state)
            midi (first (:args note))]
        (syn (midi->hz midi)))
      (Thread/sleep duration)
      (if (not (> iteration iterations))
        (recur (inc iteration)
          (result state (rand-nth (into [] (actions problem state)))))))))

(noodle)

(comment
  (syn))
