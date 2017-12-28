(ns demo.client
  (:require [fulcro.client :as fc]
            [fulcro.client.data-fetch :as df]               ; (1)
            [demo.ui.geoppr :as root]))

(defonce app (atom (fc/new-fulcro-client
                     :started-callback
                     (fn [app]                              ; (2)
                       (df/load app :geoppr/test root/Root))

                     :initial-state {:geoppr/inizio 42})))
