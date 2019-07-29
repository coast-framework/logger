# logger
12 factor ring logging middleware

## Installation

Make your `deps.edn` look like this:

```clojure
{:deps {coast-framework/logger {:git/url "https://github.com/coast-framework/logger"
                                :sha "6911e0f927211efb168877504e03290c7462ee40"}}}
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
