(ns logger.core-test
  (:require [logger.core :as logger]
            [clojure.test :refer [deftest testing is]])
  (:import (java.time Instant)))


(deftest log-str-test
  (with-redefs [logger/now (fn [] (-> Instant/EPOCH .toEpochMilli))]
    (testing "log-str logs response and request maps"
      (is (= (logger/log-str
                {:request-method :get :uri "/"}
                {:status 200 :route :routes.home/index :headers {"Content-Type" "text/plain"}}
                (logger/now))
             "1970-01-01 00:00:00 +0000 GET \"/\" :routes.home/index 200 text/plain 0 ms")))

    (testing "log-str logs response and request maps"
      (is (= (logger/log-str
                {:request-method :get :uri "/"}
                {:status 200 :route :routes.home/index :headers {"Content-Type" "text/plain"}}
                (logger/now))
             "1970-01-01 00:00:00 +0000 GET \"/\" :routes.home/index 200 text/plain 0 ms")))))
