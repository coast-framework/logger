(ns logger.core
  (:require [clojure.string :as string])
  (:import (java.time Instant ZoneId ZonedDateTime)
           (java.time.format DateTimeFormatter)
           (java.util Locale)))


(defn now []
  (-> (Instant/now) .toEpochMilli))


(defn zoned
  ([ms]
   (zoned ms "UTC"))
  ([ms timezone]
   (let [zone-id (ZoneId/of (or timezone "UTC"))]
     (-> (Instant/ofEpochMilli ms)
         (ZonedDateTime/ofInstant zone-id)))))


(defn fmt
  ([date-time]
   (fmt date-time "MM/dd/YYYY HH:mm:ss"))
  ([date-time pattern]
   (let [formatter (DateTimeFormatter/ofPattern pattern Locale/ENGLISH)]
     (.format date-time formatter))))


(defn log-str [request response start-time]
  (let [ms (- (now) start-time)
        uri (:uri request)
        status (:status response)
        route (:route response)
        method (some-> request :request-method name string/upper-case)
        headers (:headers response)
        content-type (or (get headers "content-type")
                         (get headers "Content-Type"))
        timestamp (-> (now) (zoned) (fmt "yyyy-MM-dd HH:mm:ss xx"))]
    (->> [timestamp method (str "\"" uri "\"")
          route status content-type
          ms "ms"]
         (filter some?)
         (map str)
         (filter #(not (string/blank? %)))
         (string/join " "))))


(defn log [request response start-time]
  (println (log-str request response start-time)))


(defn logger [handler]
  (fn [request]
    (let [now (now)
          response (handler request)]
      (log request response now)
      response)))
