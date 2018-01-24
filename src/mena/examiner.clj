(ns mena.examiner
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(s/def :question/operator #{`*})
(s/def :question/operand int?)
(s/def :question/operands (s/coll-of :question/operand))
(s/def :question/answer int?)
(s/def ::question
  (s/keys :req-un [:question/operands :question/operator :question/answer]))

(s/def :params/min int?)
(s/def :params/max int?)
(s/def :params/tables (s/coll-of int?))
(s/def :params/count nat-int?)
(s/def ::question-parameters
  (s/and (s/keys :req-un [:params/min :params/max :params/tables :params/count])
         (fn [{:keys [max min tables] :as opts}]
           (let [table-count (count tables)
                 max-possible-values (- (* 2 table-count (+ max (- min) 1))
                                        (* table-count table-count))]
             (<= (:count opts) max-possible-values)))))

(defn- random-from
  [candidates]
  (nth candidates (rand-int (count candidates))))

(defn- random-operands
  [{:keys [tables min max]}]
  (let [full-domain (range min (+ max 1))]
    (-> []
        (conj (random-from (or tables full-domain)))
        (conj (random-from full-domain))
        (shuffle))))

(defn generate-question
  [operator opts]
  (let [operands (random-operands opts)]
    {:operands operands
     :operator operator
     :answer   (apply * operands)}))

(s/fdef generate-question
        :args (s/cat :operator :question/operator
                     :config ::question-parameters)
        :ret ::question)

(defn generate-questions
  [operator opts]
  (let [add-next-question
        (fn [acc]
          (loop [q nil]
            (if (or (not q) (contains? acc q))
              (recur (generate-question operator opts))
              (conj acc q))))]
    (loop [acc #{}]
      (if (= (count acc) (:count opts))
        acc
        (recur (add-next-question acc))))))
(s/fdef generate-questions
        :args (s/cat :operator :question/operator
                     :config ::question-parameters)
        :ret (s/coll-of ::question))
