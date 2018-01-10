(ns demo.legenda.db-postgres
  (:require
    [korma.core :as k :only (defentity
                              set-fields
                              insert
                              update
                              values)]
    [korma.db :as kDB :only (defdb get-connection)]
    [clojure.java.jdbc :as j]
    [clojure.pprint :as pp]
    [java-jdbc.ddl :as ddl]
    [demo.legenda.utils :as utils]))


(def db-spec
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     "//192.168.18.51:5432/rumbl_dev"
   :user        "postgres"
   :password    "postgres"})

(kDB/defdb korma-db db-spec)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))

(k/defentity dominio_gerarchia)
(k/defentity legenda_ppr)
(k/defentity colori_geoppr_v001_21marzo2016)


(defn get-gerarchia []
  (k/select dominio_gerarchia
            (k/order :id)
            (k/fields :id
                      :cod_gerarchia
                      :sfalsa
                      :desc_gerarchia)))

(defn- get-legenda []
  (k/select legenda_ppr
            (k/order :id) (k/order :geoppr)
            (k/fields :id
                      :geoppr
                      :tipo
                      :sigla
                      :unita_nome
                      :unita_desc
                      :unita_eta
                      :gerar
                      :desc_gerar
                      :eta)))

(defn get-colori []
  (k/select colori_geoppr_v001_21marzo2016
            (k/order :GEOPPR)
            (k/fields :GEOPPR
                      :TIPO
                      :SIGLA
                      :COLORE_SOLID
                      :RED
                      :GREEN
                      :BLUE
                      :Cartografato)))


(defn- add-level-gerarchia []
  (->> (get-gerarchia)
       (map (fn [m]
              (let [sfalsa (m :sfalsa)]
                (assoc m :level (utils/classe? sfalsa)))))))

(def MAP-GEOPPR-COLORI
  (reduce (fn [acc {:keys [GEOPPR] :as row}]
            (assoc acc GEOPPR row))
          {} (get-colori)))

(defn- add-rgb-legenda-cartografato []
    (->> (get-legenda)
         (map (fn [m]
                (let [geoppr (m :geoppr)
                      row-colore (MAP-GEOPPR-COLORI geoppr)]
                  (if row-colore
                    (assoc m :cartografato true :rgb (utils/to-hex-rgb row-colore))
                    (assoc m :cartografato false)))))))

(doseq [file-data [{:nomefile-edn "gerarchia" :data (into [] (add-level-gerarchia))} ; data da seq a vector
                   {:nomefile-edn "legenda" :data (into [] (add-rgb-legenda-cartografato))}]]
  (utils/pprint-to-edn-resource-geoppr-file file-data))

