(ns demo.ui.geoppr
  (:require
    [fulcro.client.mutations :as m]
    [fulcro.client.data-fetch :as df]
    [fulcro.client.dom :as dom]
    [demo.api.geoppr-mutations :as api]
    [fulcro.client.primitives :as prim :refer [defsc]]))

(defsc Root [this {:keys [ui/react-key  geoppr/gerarchia] :or {react-key "ROOT"}}]
  {:initial-state (fn [p] {})
   :query         [:ui/locale :ui/react-key  :geoppr/gerarchia]}
  (dom/div #js {:key react-key}
           (dom/div nil (str "hi" (test :a)))
           (dom/button
             #js {:onClick #(prim/transact! this `[(api/test1 {:x 1})])} "uno")
           (gerarchia :nome)))