(ns demo.api.geoppr-mutations
  (:require
    [fulcro.client.mutations :refer [defmutation]]
    [fulcro.client.logging :as log]))

(defmutation test1
  "Client geoppr test"
  [params]
  (action [env]
          (log/info "Client geoppr test"))
  (remote [env] true))

