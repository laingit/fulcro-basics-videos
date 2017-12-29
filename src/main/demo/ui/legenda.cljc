(ns demo.ui.legenda
  (:require
            [fulcro.client.mutations :as m :refer [defmutation]]
            [fulcro.client.dom :as dom]
            [fulcro.events :refer [enter-key?]]
            [fulcro.client.cards :refer [defcard-fulcro]]
            [fulcro.client.primitives :as prim :refer [defsc]]
            [demo.ui.components :as comp]
            [fulcro.client.data-fetch :as df]))

(defsc LegendaItem [this {:keys [id sort lv n]}]
       {:query [:id :sort :lv :n]
        :ident [:legenda-item/by-id :id]}
       (dom/div nil
                (dom/button #js {:onClick (fn [evt]
                                            (prim/transact! this `[(legenda-down-lv {:id ~id})]))}
                            "lv-down")

                (dom/button #js {:onClick (fn [evt]
                                            (prim/transact! this `[(legenda-up-lv {:id ~id})]))}
                            "lv-up")
                (dom/label nil (repeat lv " - "))
                (dom/label nil "id:" id " sort: " sort" lv: " lv " n: " n)))

(def ui-legenda-table-item (prim/factory LegendaItem {:keyfn :id}))

(defsc LegendaTable [this {:keys [leg/id leg/name leg/items] :as props}]
  {:query [:leg/id :leg/name {:leg/items (prim/get-query LegendaItem)}]
   :ident [:legenda-list/by-id :leg/id]}
  (dom/div nil
           (dom/label nil "ID | LV | NOME")
           (dom/br nil)
           (dom/label nil "---------------")
           (mapv ui-legenda-table-item items)))