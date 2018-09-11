(ns voice-plan.planning-problem-test
  (:require [clojure.test :refer :all]
            [voice-plan.planning-problem :refer :all]
            [voice-plan.search :refer :all]))

(def enter-bath (action (expr :bath)
                        {:add [(expr :is :clean) (expr :is :wet)]}))

(def use-towel (action (expr :towel) {:add [(expr :is :dry)]}))


(def get-dirty (action (expr :get :dirty)
                       {:positive [(expr :is :clean)]
                        :add [(expr :is :dirty)]}))

(defn dirty-problem
  "So dirty! How to get clean?"
  []
  (planning-problem [(expr :is :dirty)]
                    [(expr :is :clean) (expr :is :dry)]
                    #{enter-bath use-towel get-dirty}))

(def problem (dirty-problem))

(deftest goal-test
  (testing "goal?"
    (testing "returns true when state is the same as problem goal"
      (is (goal? problem #{(expr :is :clean)
                           (expr :is :dry)})))

    (testing "return false when goal has not been reached"
      (is (not (goal? problem #{(negate (expr :is :dirty))
                                (expr :is :wet)}))))))

(deftest meets-preconditions-test
  (testing "meet-preconditions?"
    (testing "positive preconditions"
      (let [request-water (action (expr :request :water)
                                  {:positive [(expr :is :thirsty)]
                                   :remove [(expr :is :thirsty)]})]
        (is (meets-preconditions? request-water #{(expr :is :thirsty)}))))
    (testing "negative preconditions")
    (let [request-water (action (expr :request :water)
                                {:negative [(expr :in :dessert)]
                                 :add [(expr :needsto :pee)]})]
      (is (not (meets-preconditions? request-water #{(expr :in :dessert)})))
      (is (meets-preconditions? request-water #{(expr :in :bar)})))))

(deftest actions-test
  (testing "actions returns a set of possible actions"
    (is (= (actions problem #{(expr :is :happy)})
           #{enter-bath use-towel}))
    (is (= (actions problem #{(expr :is :clean)})
           #{enter-bath use-towel get-dirty}))))

(deftest result-test
  (testing "result"
    (testing "adds the add effects to the state"
      (let [state #{}
            act (action :add-me {:add [(expr :be :cool)]})]
        (is (= (result state act)
               #{(expr :be :cool)}))))
    (testing "removes the remove effects from the state"
      (let [state #{(expr :is :lame) (negate (expr :is :cool))}
            act (action :be-better {:remove [(expr :is :lame)]})]
        (is (= (result state act)
               #{(negate (expr :is :cool))}))))))

(deftest goal?-test
  (testing "goal?"
    (testing "returns true if all goals are in state"
      (is (goal? problem #{(expr :is :clean)
                           (expr :is :dry)}))
      (is (goal? problem #{(expr :is :clean)
                           (expr :is :dry)
                           (expr :is :smelly)})))))
