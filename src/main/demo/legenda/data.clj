(ns demo.legenda.data)


(def table [{:id 1 :lv 1 :n "a"}
            {:id 2 :lv 2 :n "b"}
            {:id 3 :lv 3 :n "c"}
            {:id 4 :lv 3 :n "d"}
            {:id 5 :lv 1 :n "e"}
            {:id 6 :lv 2 :n "f"}
            {:id 7 :lv 3 :n "g"}])

(comment
  (def t-fc-out [{:id 1 :lv 1 :n "a" :fc 2}
                 {:id 2 :lv 2 :n "b" :fc 3}
                 {:id 3 :lv 3 :n "c"}
                 {:id 4 :lv 3 :n "d"}
                 {:id 5 :lv 1 :n "e" :fc 6}
                 {:id 6 :lv 2 :n "f" :fc 7}
                 {:id 7 :lv 3 :n "g"}])

  (def t-ns-out [{:id 1 :lv 1 :n "a" :ns 5}
                 {:id 2 :lv 2 :n "b"}
                 {:id 3 :lv 3 :n "c" :ns 4}
                 {:id 4 :lv 3 :n "d"}
                 {:id 5 :lv 1 :n "e"}
                 {:id 6 :lv 2 :n "f"}
                 {:id 7 :lv 3 :n "g"}])

  [[0 {:id 1, :lv 1, :n "a"}]                                 ; [i m]
   [1 {:id 2, :lv 2, :n "b"}]]                                ; tb-idx

  (defn find-all-ns [tb-idx]
    (letfn [(find-first=lv [m-a t>=]
              (let [lv-a (m-a :lv)

                    altri
                    (drop-while
                      (fn [[_ m-b]]
                        (let [lv-b (get m-b :lv)]
                          (> lv-b lv-a)))
                      t>=)]

                (if (> (count altri) 0)
                  (let
                    [[_i m-b] (first altri)]
                    (assoc m-a :ns (m-b :id)))
                  :not-found
                  )))

            (find-ns [m-a ts]
              (let [lv-a (m-a :lv)

                    maggiore-o-uguale
                    (take-while
                      (fn [[_ m-b]]
                        (let [lv-b (get m-b :lv)]
                          (>= lv-b lv-a)))
                      ts)]

                (find-first=lv m-a maggiore-o-uguale)))]

      (reduce
        (fn [acc v]
          (let [[i m] v
                ts (drop (+ 1 i) tb-idx)
                ns (find-ns m ts)]
            (if (= ns :not-found)
              acc
              (conj acc ns))))
        []                                                    ; acc
        tb-idx)))
  (->> table
       (into [] (map-indexed (fn [i v] [i v])))
       (find-all-ns))

  ; input  [1 2 3 4]
  ; output [[1 2][2 3][3 4]]
  (defn affianca [t]
    (map
      (fn [a b] [a b])
      t
      (drop 1 t)))

  (defn- find-first-child [table]
    (->> table

         #_(map (fn [{:keys [id lv]}] {:id id :lv lv}))

         affianca

         (filter
           (fn [[a b]]
             (let [lv-a (get a :lv)
                   lv-b (get b :lv)]
               (> lv-b lv-a))))

         (map
           (fn [[a b]]
             (assoc a :fc (get b :id))))))

  (find-first-child table)
  )