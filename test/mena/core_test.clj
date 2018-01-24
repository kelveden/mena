(ns mena.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec.test.alpha :as stest]
            [mena.core :as sut]))

(stest/instrument)

(deftest can-generate-question-text
  (is (= "3 x 5" (sut/question-text {:operator `* :operands [3 5] :answer 15}))))

(deftest question-text-keeps-operands-in-order
  (is (= "3 x 5" (sut/question-text {:operator `* :operands [3 5] :answer 15})))
  (is (= "5 x 3" (sut/question-text {:operator `* :operands [5 3] :answer 15}))))