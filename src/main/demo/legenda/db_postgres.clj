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


(defn get-gerarchia []
  (let [query "SELECT \n
              id, cod_gerarchia, sfalsa, desc_gerarchia
              FROM public.dominio_gerarchia
              ORDER BY id;"]
    (j/query (get-connection-from-pool) [query])))


(k/defentity legenda_ppr)

(defn- get-legenda []
  (k/select legenda_ppr
            (k/order :id)
            (k/order :geoppr)
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

(defn- write-legenda-edn []
  (utils/pprint-to-edn-resource-geoppr-file
    {:nomefile-edn "legenda"
     :data         (into [] (get-legenda))}))

(defn- write-gerachia-edn []
  (utils/pprint-to-edn-resource-geoppr-file
    {:nomefile-edn "gerarchia"
     :data         (into [] (get-gerarchia))}))

(write-legenda-edn)
#_(write-gerachia-edn)

