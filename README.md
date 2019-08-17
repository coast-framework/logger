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

Log stuff like this

```clojure
(logger/log {:request-method :get :uri "/"}
  {:status 200 :headers {"Content-Type" "text/plain"}}
  (logger/now))
```

Use the middleware like this

```clojure
(def app (-> (your-ring-app)
             (logger/logger)))
```

## Testing

```sh
cd logger && make test
```

## License

MIT

## Contribution

Create an issue, star it or make a pull request.
