(ns voice-plan.instruments
  (:use [overtone.live]))

(defsynth syn [freq 440]
  (out 0 (pan2 (* (env-gen (perc) :action FREE)
                  (sin-osc freq)))))
