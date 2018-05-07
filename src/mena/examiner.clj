(ns mena.examiner
  (:require [clojure.spec.alpha :as s]
            [mena.question-generator :as q]))

(defn- question-text
  [{:keys [operator operands]}]
  (case operator
    `* (clojure.string/join " x " operands)))

(defn- ask-question
  [question]
  (println (question-text question))
  [(read-line) question])

(defn prompt-questions
  "Prompt for an answer to each question, displaying the results at the end."
  [questions]
  (let [start               (System/currentTimeMillis)
        answers             (doall (map ask-question questions))
        end                 (System/currentTimeMillis)

        total-questions     (count questions)
        total-time          (bigdec (/ (- end start) 1000))
        incorrect-questions (->> answers
                                 (filter (fn [[given-answer {:keys [answer]}]]
                                           (not= given-answer (str answer))))
                                 (map second))
        total-correct       (- total-questions (count incorrect-questions))]
    (println (format "You got %s / %s questions correct in %ss"
                     total-correct
                     (count questions)
                     total-time))

    (when (not= total-correct total-questions)
      (prompt-questions incorrect-questions))))

(s/fdef prompt-questions
        :args (s/cat :questions (s/coll-of ::q/question)))

(defn print-questions
  "Print the questions to sysout."
  [questions]
  (doseq [question questions]
    (println (question-text question))))

(s/fdef print-questions
        :args (s/cat :questions (s/coll-of ::q/question)))