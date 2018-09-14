(ns voice-plan.search
  (:require [voice-plan.planning
             :refer [result actions goal? start-action]]
            [voice-plan.utils :refer [queue]]
            [clojure.data.priority-map :refer [priority-map priority-map-by]]))

(defn node
  ([state]
   (node state nil nil nil))
  ([state parent action cost]
   {:state state
    :parent parent
    :action action
    :cost cost}))

(defn expand
  "Return a list of nodes reachable in one step from the given node n."
  [result actions n]
  (let [state (:state n)]
    (map (fn [action]
           (node (result state action) n action nil))
         (actions state))))

(defn breadth-first [{goal? :goal?
                      actions :actions
                      result :result}
                     state]
  (loop [current (node state)
         frontier (queue)
         explored #{}
         iteration 0]
    (if (or (goal? (current :state))
            (> iteration 1000))
      current
      (let [expansion (expand result actions current)
            filtered (remove #(contains? explored (:state %))
                             expansion)
            new-frontier (into frontier filtered)]
        (if (not (empty? new-frontier))
          (recur (peek new-frontier)
            (pop new-frontier)
            (conj explored (current :state))
            (inc iteration)))))))

(defn depth-first [{goal? :goal?
                    actions :actions
                    result :result}
                   state]
  (loop [current (node state)
         frontier []
         explored #{}
         iteration 0]
    (if (or (goal? (current :state))
            (> iteration 1000))
      current
      (let [expansion (expand result actions current)
            filtered (remove #(contains? explored (:state %))
                             expansion)
            new-frontier (into frontier filtered)]
        (if (not (empty? new-frontier))
          (recur (peek new-frontier)
            (pop new-frontier)
            (conj explored (current :state))
            (inc iteration)))))))

(defn uniform-cost
  ([{goal? :goal?
     actions :actions
     result :result
     cost :cost}
    state]
   (loop [current (node state)
          frontier (priority-map)
          explored #{}
          iteration 0]
     (if (or (goal? (current :state))
             (> iteration 1000))
       current
       (let [expansion (expand result actions current)
             filtered (map (juxt identity cost)
                           (remove #(contains? explored (:state %))
                             expansion))
             new-frontier (into frontier filtered)]
         (if (not (empty? new-frontier))
           ; peeking priority-map returns key value pair
           (recur (get (peek new-frontier) 0)
             (pop new-frontier)
             (conj explored (current :state))
             (inc iteration))))))))

(defn a* [problem])

(defn get-solution [tree]
  (loop [node tree
         actions '()]
    (if node
      (recur (:parent node)
        (conj actions (or (:action node)
                          (start-action (:state node)))))
      actions)))

(defn get-states [tree]
  (loop [node tree
         states '()]
    (if node
      (recur (:parent node)
        (conj states (:state node)))
      states)))
