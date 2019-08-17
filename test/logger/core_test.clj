(ns logger.core-test
  (:require [logger.core :as logger]
            [clojure.test :refer [deftest testing is]]))


(deftest stringify-test
  (testing "stringify map with strings"
    (is (= (logger/stringify {:a "a" :b "b"})
           ":a=a :b=b")))

  (testing "stringify map with numbers"
    (is (= (logger/stringify {:a 1 :b -1})
           ":a=1 :b=-1")))

  (testing "stringify map with booleans"
    (is (= (logger/stringify {:a false})
           ":a=false"))))


(deftest log-line-test
  (with-redefs [logger/timestamp (fn [] "timestamp")]
    (testing "log-line with strings"
      (is (= (logger/line "Request started" {:request-method "GET" :uri "/"})
             "[timestamp] Request started :request-method=GET :uri=/")))

    (testing "log-line with number"
      (is (= (logger/line "User authenticated" {:user-id 123})
             "[timestamp] User authenticated :user-id=123")))))


(deftest middleware-test
  (let [log-lines (atom [])]
    (with-redefs [logger/timestamp (fn [] "timestamp")
                  println (fn [& [arg]] (swap! log-lines conj arg))
                  logger/now (fn [] 0)]

      (testing "middleware test"
        (reset! log-lines [])

        ((logger/logger (fn [request] {:status 200 :headers {"Content-Type" "text/html"}}))
         {:request-method :get
          :uri "/"
          :route "home/index"})

        (is (= @log-lines
               ["[timestamp] Request started request-method=GET route=home/index uri=/"
                "[timestamp] Request finished status=200 content-type=text/html duration=0ms"])))

      (testing "middleware test without route"
        (reset! log-lines [])

        ((logger/logger (fn [request] {:status 200 :headers {"Content-Type" "text/html"}}))
         {:request-method :get
          :uri "/"})

        (is (= @log-lines
               ["[timestamp] Request started request-method=GET uri=/"
                "[timestamp] Request finished status=200 content-type=text/html duration=0ms"]))))))
