(ns voice-plan.planning
  (:require [voice-plan.utils :refer [product]]
            [clojure.set :refer [difference union]]))

(defn action
  ([op [positive negative] [add remove]]
   {:type :action
    :op op
    :positive (or positive [])
    :negative (or negative [])
    :add (or add [])
    :remove (or remove [])})
  ([op {positive :positive
        negative :negative
        add :add
        remove :remove}]
   {:type :action
    :op op
    :positive (or positive [])
    :negative (or negative [])
    :add (or add [])
    :remove (or remove [])}))


(defn expr [name & args]
  {:type :expr
   :name name
   :args args})

(defn meets-preconditions?
  "Return true if all the actions preconditions are met by the state."
  [action state]
  (if (and (= 0 (count (:positive action)))
           (= 0 (count (:negative action))))
    true
    (and (every? (fn [precond] (get state precond))
                 (:positive action))
         (every? (fn [precond] (not (get state precond)))
                 (:negative action)))))

(defn negate [expr]
  (assoc expr :not (not (:not expr))))

(defn goal? [goal state]
  (every? (fn [exp]
            (contains? state exp))
          goal))

(defn actions [action-set state]
  (reduce (fn [acc action]
            (if (meets-preconditions? action state)
              (conj acc action)
              acc))
          #{} action-set))

(defn result
  [state action]
  (set (union (difference state (:remove action))
              (:add action))))

(defn planning-problem
  ([init goal]
   {:init (set init)
    :goal (set goal)})
  ([init goal action-list]
   {:init (set init)
    :goal (set goal)
    :action-list (set action-list)}))

(defn start-action [state]
  (action (expr :START (into [] state)) {}))

(defn make-relations [name & args]
  (map #(apply (partial expr name) %)
       (product args)))

(defn- str-expr [expr]
  (str (:name expr) (:args expr)))

(defn sentence [item]
  (cond
    (= (:type item) :expr) (str-expr item)
    (= (:type item) :action) (str-expr (:op item))))
