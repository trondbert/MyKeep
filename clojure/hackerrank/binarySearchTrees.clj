(declare trees)
(def modBase 100000007)

(println (% 17 7))
(def subResults [1 1])

;; Number of trees with the given number of nodes
(defn trees [nodes]
    (let [lastNode (- nodes 1)
          leftTrees  (subvec subResults 0 nodes)
          rightTrees (rseq (subvec subResults 0 nodes))
          result (reduce + (map * leftTrees rightTrees))]
         (println [leftTrees rightTrees result]) 
         (def subResults (conj subResults result))
         (println ["subresults: " subResults])
         result
    )
)

(defn solve [n]
    (if (contains? subResults n)
        (get subResults n)
        (let []
            (doseq [subSize (range 0 n)]
                (if (contains? subResults subSize) (get subResults subSize) (trees subSize))
            )
            (trees n)
        )
    )
)


(doseq [line (rest (line-seq (java.io.BufferedReader. *in*)))]
    (println (solve (read-string line)))
)

;(def subResults (conj subResults 5))
;(println (subvec subResults 1 2))
