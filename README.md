# logger
12 factor ring logging middleware

## Installation

Make your [deps.edn](https://clojure.org/guides/deps_and_cli) look like this:

```clojure
coast-framework/logger {:mvn/version "1.0.0"}
```

## Usage

Require it like this

```clojure
(ns your-project
  (:require [logger.core :as logger]))
```

Log stuff like this, returns a string

```clojure
(logger/line "Request started" {"request-method" "GET" "uri" "/"})
; => "[timestamp] Request started request-method=GET" uri=/

(logger/line "Request finished" {"status" 200 "content-type" "text/plain"})
; => "[timestamp] Request finished status=200 content-type=text/plain"
```

Use the middleware like this

```clojure
(def app (-> (your-ring-app)
             (logger/logger)))
```

This calls `println` twice and logs *two* ring request/response lines that look like this:

```
[timestamp] Request started request-method=GET route=home/index uri=/
[timestamp] Request finished status=200 duration=10ms content-type=text/html
```

*Note* route is only logged if there is a `:route` keyword in the request map

## Testing

```sh
cd logger && make test
```

## License

MIT

## Contribution

Create an issue, star it or make a pull request.
