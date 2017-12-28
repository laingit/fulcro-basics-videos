(ns demo.api.geoppr-mutations
  (:require
    [taoensso.timbre :as timbre]
    [fulcro.server :refer [defmutation]]))

(defmutation test1
             "Server test"
             [params]
             (action [env]
                     (timbre/info "Server test" params)))
