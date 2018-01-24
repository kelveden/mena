(ns mena.examiner-test
  (:require [clojure.test :refer :all]
            [mena.examiner :as sut]
            [clojure.spec.test.alpha :as stest]))

(stest/instrument)

(def ^:private dummy-operator `*)

(deftest generated-question-always-has-one-operand-from-selected-tables
  (let [{:keys [operands]} (sut/generate-question dummy-operator {:min 1 :max 12 :count 5 :tables [2]})]
    (is (contains? (set operands) 2))))

(deftest generated-questions-are-unique
  (let [result (sut/generate-questions `* {:count 500 :min 1 :max 30 :tables (range 1 31)})]
    (is (set? result))
    (is (= 500 (count result)))))

(deftest attempting-to-generate-more-questions-than-possible-causes-exception
  (is (= 4 (count (sut/generate-questions `* {:count 4 :min 1 :max 2 :tables [1 2]})))))