(ns voice-plan.melodies
  (:require [voice-plan.search :as search]
            [voice-plan.planning :refer :all]
            [voice-plan.music :refer [step-action play-expr]]
            [voice-plan.instruments :as inst]
            [overtone.music.pitch :refer [note]]
            [overtone.music.time :refer [now]]))

; Using search algorithms to find melodies.
; Should write boethius code.

(defn play-melody [states]
  (let [time (now)]
    (loop [events states
           iter 0]
      (when (not (empty? events))
        ; (first events) is a set, so just play the first expression
        ; for now by taking the first element in the set.
        (play-expr (first (first events)) (+ time (* 1000 iter)))
        (recur (rest events) (inc iter))))))

; Find a melody from c3 to c4 composed of half steps
(let [init #{(expr :at (note :c3))}
      goal #{(expr :at (note :c4))}
      notes (range (note :c3) (+ 1 (note :c4)))
      action-set (mapcat (juxt (partial step-action 1)
                               (partial step-action -1))
                         notes)]
  (map sentence
    (search/get-solution (search/breadth-first
                          {:goal? (partial goal? goal)
                           :result result
                           :actions (partial actions action-set)}
                          init))))

; Find a melody from d3 to c4 composed of half-step down and major third up.
(let [init #{(expr :at (note :d3))}
      goal #{(expr :at (note :c4))}
      notes (range (note :c3) (+ 1 (note :c4)))
      action-set (mapcat (juxt (partial step-action 1)
                               (partial step-action -4))
                         notes)
      solution (search/breadth-first
                          {:goal? (partial goal? goal)
                           :result result
                           :actions (partial actions action-set)}
                          init)]
  ; (map sentence (search/get-solution solution))
  (play-melody (search/get-states solution)))
