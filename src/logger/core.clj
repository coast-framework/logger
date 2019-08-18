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


(defn stringify [m]
  (string/join " "
    (->> (filter (fn [[k v]] (every? some? [k v])) m)
         (map (fn [[k v]] (format "%s=%s" k v))))))


(defn timestamp []
  (-> (now) (zoned) (fmt "yyyy-MM-dd HH:mm:ss xx")))


(defn line [message data]
  (format "[%s] %s %s"
    (timestamp) message (stringify data)))


(defn content-type [headers]
  (get
    (->> (map (fn [[k v]] [(-> k name string/lower-case) v]) headers)
         (into {}))
    "content-type"))


(defn logger [handler]
  (fn [{:keys [request-method uri route] :as request}]
    (println
     (line "Request started"
       {"request-method" (some-> request-method name string/upper-case)
        "route" (some-> route name)
        "uri" uri}))
    (let [start-time (now)
          {:keys [status headers] :as response} (handler request)
          duration (- (now) start-time)]
      (println
        (line "Request finished"
          {"status" status
           "content-type" (content-type headers)
           "duration" (format "%sms" duration)}))
      response)))
