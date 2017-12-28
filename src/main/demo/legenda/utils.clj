(ns demo.legenda.utils
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.pprint :as pp]))

(defn pprint-to-edn-resource-geoppr-file [{:keys [nomefile-edn data]}]
  "Inspirazione da: 'clojure cookbook'
   Scrive usando pprint(formattando) clojure data
   output in cartella: 'resources/litofeme'
   E' anche più efficiente della funzione clojure 'spit'

   Se data è una lista puoi usare (into [] data) per scriverlo come vettore
  "
  (let [dir-nomefile (str "resources/geoppr/" nomefile-edn ".edn")]
    (with-open [w (clojure.java.io/writer dir-nomefile)]
      (binding [*out* w]
        (pp/pprint data)))))


(defn- read-from-geoppr [nomefile-edn]
  (let [load #(-> (io/file "resources/geoppr" %) slurp edn/read-string)]
    (load (str nomefile-edn ".edn"))))

(defn get-legenda []
  (read-from-geoppr "legenda"))

(defn get-gerarchia []
  (read-from-geoppr "gerarchia"))

#_(get-legenda)
#_(get-gerarchia)