(ns voice-plan.planning-problem
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

(defrecord PlanningProblem [init goal action-list])

(defprotocol Planning
  (goal? [self state])
  (actions [self state])
  (path-cost [self c state1 action state2])
  (value [self]))

(extend-protocol Planning
  PlanningProblem
  (goal? [self state]
         ; (= (get-counts (:goal self)) (get-counts state)))
         (every? (fn [exp]
                   (contains? state exp))
                 (:goal self)))
  (actions [self state]
   (let [action-list (:action-list self)]
     (reduce (fn [acc action]
               (if (meets-preconditions? action state)
                 (conj acc action)
                 acc))
             #{} action-list))))

(defn result
  [state action]
  (set (union (difference state (:remove action))
              (:add action))))

(defn planning-problem
  ([init goal]
   (PlanningProblem. (set init) (set goal) #{}))
  ([init goal action-list]
   (PlanningProblem. (set init) (set goal) action-list)))

(defn make-relations [name & args]
  (map #(apply (partial expr name) %)
       (product args)))

(let [ex (expr :have :good :cake)])


(defn- str-expr [expr]
  (str (:name expr) (:args expr)))

(defn str [item]
  (cond
    (= (:type item) :expr) (str-expr item)
    (= (:type item) :action) (str-expr (:op item))))

(str (action (expr :be :good) {}))
