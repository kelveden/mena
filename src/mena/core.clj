(ns mena.core
  (:require [clojure.string :as string]
            [clojure.tools.cli :as cli]
            [mena.examiner :as examiner]
            [mena.question-generator :as q]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest])
  (:gen-class))

(stest/instrument)

(defn- parse-int
  [x]
  (Integer/parseInt x))

(def ^:private cli-options
  [["-n" "--count NUMBER" "Number of questions to generate."
    :default 10
    :parse-fn parse-int]

   [nil "--max NUMBER" "Maximum operand"
    :default 12
    :parse-fn parse-int]

   [nil "--min NUMBER" "Minimum operand"
    :default 1
    :parse-fn parse-int]

   ["-t" "--tables NUMBER" "Comma-separated list of times tables to generate against"
    :default (range 1 13)
    :parse-fn #(map parse-int (clojure.string/split % #","))]

   ["-h" "--help"]])

(defn- usage [options-summary]
  (str "Usage: mena [options]\n\nOptions:\n" options-summary))

(defn exit [status msg]
  (when msg (println msg))
  (System/exit status))

(defn -main [& args]
  (let [{:keys [summary errors options]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (clojure.string/join "\n" errors))
      :else (do
              (->> options
                   (q/generate-questions `*)
                   (examiner/prompt-questions))
              (exit 0 nil)))))
