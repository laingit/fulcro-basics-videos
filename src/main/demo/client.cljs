(ns demo.client
  (:require [fulcro.client :as fc]
            [fulcro.client.data-fetch :as df]
            [demo.ui.geoppr :as root]))

(defonce app (atom (fc/new-fulcro-client
                     :started-callback
                     (fn [app]
                       (df/load app :geoppr/gerarchia root/Root)))))
